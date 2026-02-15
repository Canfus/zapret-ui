import path from "path";
import type { FilesystemService } from "../modules/filesystem";
import { CONFIG_GLOB } from "../config/constants";
import type { ConfigFile } from "../../shared/types";

export class GetConfigsUseCase {
  constructor(
    private _filesystem: FilesystemService,
    private _zapretDir: string
  ) {}

  async execute(): Promise<ConfigFile[]> {
    const files = await this._filesystem.glob(this._zapretDir, CONFIG_GLOB);

    return files
      .sort((a, b) => {
        const numA = this.extractNumber(path.basename(a));
        const numB = this.extractNumber(path.basename(b));

        return numA - numB;
      })
      .map((filePath, index) => ({
        name: path.basename(filePath, ".bat"),
        path: filePath,
        index: index + 1
      }));
  }

  private extractNumber(filename: string): number {
    const match = filename.match(/(\d+)/);

    return match ? parseInt(match[1], 10) : 0;
  }
}
