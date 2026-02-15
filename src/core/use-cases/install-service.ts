import type { CliService } from "../services/cli";

export class InstallServiceUseCase {
  constructor(private _cli: CliService) {}

  execute = async (configIndex: number) => {
    if (configIndex < 1) {
      throw new Error(`Invalid config index: ${configIndex}. Must be gte 1.`);
    }

    const result = await this._cli.installService(configIndex);

    if (!result.success) {
      throw new Error(`Failed to install service (exit code ${result.exitCode}):\n${result.errors || result.output}`);
    }
  };
}
