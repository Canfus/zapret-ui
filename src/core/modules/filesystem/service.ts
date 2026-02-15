import fs from "fs/promises";
import path from "path";

import type { FileInfo } from "./types";

export class FilesystemService {
  async exists(path: string): Promise<boolean> {
    try {
      await fs.access(path);
      return true;
    } catch {
      return false;
    }
  }

  async readFile(path: string, encoding: BufferEncoding = "utf-8"): Promise<string> {
    return fs.readFile(path, { encoding });
  }

  async readFileBuffer(path: string): Promise<Buffer> {
    return fs.readFile(path);
  }

  async writeFile(filePath: string, content: string): Promise<void> {
    await fs.mkdir(path.dirname(filePath), { recursive: true });
    await fs.writeFile(filePath, content, "utf-8");
  }

  async writeFileBuffer(filePath: string, content: string): Promise<void> {
    await fs.mkdir(path.dirname(filePath), { recursive: true });
    await fs.writeFile(filePath, content);
  }

  async mkdir(path: string): Promise<void> {
    await fs.mkdir(path, { recursive: true });
  }

  async remove(path: string): Promise<void> {
    await fs.rm(path, { recursive: true, force: true });
  }

  async glob(dir: string, pattern: string): Promise<string[]> {
    const exists = await this.exists(dir);

    if (!exists) return [];

    const regexStr = `^${pattern.replace(/\./g, "\\.").replace(/\*/g, ".*")}$`;
    const regex = new RegExp(regexStr, "i");

    const entries = await fs.readdir(dir, { withFileTypes: true });

    return entries
      .filter((entry) => entry.isFile() && regex.test(entry.name))
      .map((entry) => path.join(dir, entry.name))
      .sort();
  }

  async readDir(dir: string): Promise<FileInfo[]> {
    const entries = await fs.readdir(dir, { withFileTypes: true });

    return entries.map((entry) => ({
      name: entry.name,
      path: path.join(dir, entry.name),
      isDirectory: entry.isDirectory()
    }));
  }
}
