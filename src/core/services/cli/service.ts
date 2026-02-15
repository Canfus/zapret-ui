import path from "path";

import type { ShellService, ShellResult } from "../../modules/shell";
import { SERVICE_BAT, TIMEOUTS, TEST_SCRIPT_PATH } from "../../config/constants";
import { CliParser } from "./parser";
import { ServiceBatMenuChoice, ServiceBatExternalCommand, type CliCommandResult } from "./types";
import type { ServiceStatusInfo, DiagnosticResult, TestResult } from "../../../shared/types";

export class CliService {
  constructor(
    private _shell: ShellService,
    private _zapretDir: string
  ) {}

  async installService(configIndex: number): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.INSTALL, String(configIndex)], TIMEOUTS.SERVICE_BAT);
  }

  async removeService(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.REMOVE]);
  }

  async checkStatus(): Promise<ServiceStatusInfo> {
    const result = await this.runMenuCommand([ServiceBatMenuChoice.CHECK_STATUS]);
    return CliParser.parseStatus(result.output);
  }

  async toggleGameFilter(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.GAME_FILTER]);
  }

  async toggleCheckUpdates(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.CHECK_UPDATES_TOGGLE]);
  }

  async cycleIpset(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.IPSET_FILTER]);
  }

  async updateIpset(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.UPDATE_IPSET], TIMEOUTS.SERVICE_BAT);
  }

  async updateHosts(): Promise<CliCommandResult> {
    return this.runMenuCommand([ServiceBatMenuChoice.UPDATE_HOSTS], TIMEOUTS.SERVICE_BAT);
  }

  async checkForUpdates(): Promise<CliCommandResult> {
    return this.runExternalCommand(ServiceBatExternalCommand.CHECK_UPDATES, ["soft"]);
  }

  async runDiagnostics(): Promise<DiagnosticResult[]> {
    const result = await this.runMenuCommand([ServiceBatMenuChoice.DIAGNOSTICS, "N", "N"], TIMEOUTS.SERVICE_BAT);

    return CliParser.parseDiagnostics(result.output);
  }

  async runTests(onLine?: (line: string) => void): Promise<TestResult> {
    const testScriptPath = path.join(this._zapretDir, TEST_SCRIPT_PATH);

    const result = await this._shell.execStream(
      "powershell",
      ["-NoProfile", "-ExecutionPolicy", "Bypass", "-File", testScriptPath],
      onLine ?? (() => {}),
      {
        cwd: this._zapretDir,
        timeout: TIMEOUTS.TESTS
      }
    );

    return CliParser.parseTestResults(result.stdout);
  }

  private async runMenuCommand(
    menuChoices: string[],
    timeout: number = TIMEOUTS.SERVICE_BAT
  ): Promise<CliCommandResult> {
    const result = await this._shell.execInteractive(SERVICE_BAT, {
      menuChoices,
      cwd: this._zapretDir,
      timeout
    });

    return this.toCliResult(result);
  }

  private async runExternalCommand(command: ServiceBatExternalCommand, args: string[] = []): Promise<CliCommandResult> {
    const result = await this._shell.exec(
      "cmd",
      ["/c", SERVICE_BAT, command, ...args],
      this._zapretDir,
      TIMEOUTS.SERVICE_BAT
    );

    return this.toCliResult(result);
  }

  private toCliResult(result: ShellResult): CliCommandResult {
    return {
      success: result.exitCode === 0,
      output: result.stdout,
      errors: result.stderr,
      exitCode: result.exitCode
    };
  }
}
