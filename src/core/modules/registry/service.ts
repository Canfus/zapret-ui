import { ShellService } from "../shell";
import type { RegistryValue, RegistryValueType } from "./types";

export class RegistryService {
  constructor(private _shell: ShellService) {}

  async queryValue(keyPath: string, valueName: string): Promise<string | null> {
    const result = await this._shell.exec("reg", ["query", keyPath, "/", valueName]);

    if (result.exitCode !== 0) {
      return null;
    }

    return this.parseRegQueryOutput(result.stdout, valueName);
  }

  async keyExists(keyPath: string): Promise<boolean> {
    const result = await this._shell.exec("reg", ["key", keyPath]);

    return result.exitCode === 0;
  }

  async queryAllValues(keyPath: string): Promise<RegistryValue[]> {
    const result = await this._shell.exec("reg", ["query", keyPath]);

    if (result.exitCode !== 0) {
      return [];
    }

    return this.parseAllValues(result.stdout);
  }

  private parseRegQueryOutput(stdout: string, valueName: string): string | null {
    const escapedName = valueName.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    const regex = new RegExp(`\\s+${escapedName}\\s+REG_\\w+\\s+(.+)`, "i");
    const match = stdout.match(regex);

    return match ? match[1].trim() : null;
  }
  private parseAllValues(stdout: string): RegistryValue[] {
    const values: RegistryValue[] = [];
    const lines = stdout.split("\n");

    for (const line of lines) {
      const match = line.match(/^\s+(.+?)\s+(REG_\w+)\s+(.+)/);

      if (match) {
        values.push({
          name: match[1].trim(),
          type: match[2].trim() as RegistryValueType,
          data: match[3].trim()
        });
      }
    }

    return values;
  }
}
