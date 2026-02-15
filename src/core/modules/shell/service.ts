import { execFile, spawn } from "child_process";

import { TIMEOUTS } from "../../config/constants";
import type { ShellInteractiveOptions, ShellStreamOptions, ShellResult } from "./types";
import { clearTimeout } from "node:timers";

export class ShellService {
  exec(
    command: string,
    args: string[] = [],
    cwd?: string,
    timeout: number = TIMEOUTS.SHELL_DEFAULT
  ): Promise<ShellResult> {
    return new Promise((resolve, reject) => {
      execFile(
        command,
        args,
        {
          cwd,
          timeout,
          windowsHide: true,
          encoding: "utf-8",
          env: { ...process.env, CHCP: "65001" }
        },
        (error, stdout, stderr) => {
          if (error && !("code" in error)) {
            reject(error);
            return;
          }

          resolve({
            exitCode: (error as NodeJS.ErrnoException & { code?: number })?.code ?? 0,
            stdout: stdout ?? "",
            stderr: stderr ?? ""
          });
        }
      );
    });
  }

  execInteractive(batFile: string, options: ShellInteractiveOptions): Promise<ShellResult> {
    return new Promise((resolve, reject) => {
      const { menuChoices, cwd, env, timeout = TIMEOUTS.SERVICE_BAT } = options;

      const echoChain = menuChoices.map((choice) => `echo ${choice}`).join(" & ");
      const fullCmd = `chcp 65001 >nul & (${echoChain}) | ${batFile}`;

      const child = spawn("cmd", ["/c", fullCmd], {
        cwd,
        env: { ...process.env, ...env },
        windowsHide: true,
        stdio: ["pipe", "pipe", "pipe"]
      });

      let stdout = "";
      let stderr = "";
      let timedOut = false;

      const timer = setTimeout(() => {
        timedOut = true;
        child.kill("SIGTERM");
      }, timeout);

      child.stdout?.on("data", (chunk: Buffer) => {
        stdout += chunk.toString("utf-8");
      });

      child.stderr?.on("data", (chunk: Buffer) => {
        stderr += chunk.toString("utf-8");
      });

      child.on("close", (code) => {
        clearTimeout(timer);

        if (timedOut) {
          reject(new Error(`Command timed out after ${timeout}ms`));
          return;
        }

        resolve({
          exitCode: code ?? 1,
          stdout,
          stderr
        });
      });

      child.on("error", (err) => {
        clearTimeout(timer);
        reject(err);
      });
    });
  }

  execStream(
    command: string,
    args: string[],
    onLine: (line: string) => void,
    options: ShellStreamOptions = {}
  ): Promise<ShellResult> {
    return new Promise((resolve, reject) => {
      const { cwd, env, timeout = TIMEOUTS.TESTS } = options;

      const child = spawn(command, args, {
        cwd,
        env: { ...process.env, ...env },
        windowsHide: true,
        stdio: ["pipe", "pipe", "pipe"]
      });

      let stdout = "";
      let stderr = "";
      let buffer = "";
      let timedOut = false;

      const timer = setTimeout(() => {
        timedOut = true;
        child.kill("SIGTERM");
      }, timeout);

      child.stdout?.on("data", (chunk: Buffer) => {
        const text = chunk.toString("utf-8");
        stdout += text;
        buffer += text;

        const lines = buffer.split("\n");
        buffer = lines.pop() ?? "";

        for (const line of lines) {
          const trimmed = line.replace(/\r$/, "");

          if (trimmed.length > 0) {
            onLine(trimmed);
          }
        }
      });

      child.stderr?.on("data", (chunk: Buffer) => {
        stderr += chunk.toString("utf-8");
      });

      child.on("close", (code) => {
        clearTimeout(timer);

        if (buffer.trim().length > 0) {
          onLine(buffer.trim());
        }

        if (timedOut) {
          reject(new Error(`Command timed out after ${timeout}ms`));
          return;
        }

        resolve({
          exitCode: code ?? 1,
          stdout,
          stderr
        });
      });

      child.on("error", (err) => {
        clearTimeout(timer);
        reject(err);
      });
    });
  }
}
