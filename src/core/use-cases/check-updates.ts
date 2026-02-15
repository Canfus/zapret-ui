import type { CliService } from "../services/cli";

export class CheckUpdatesUseCase {
  constructor(private _cli: CliService) {}

  execute = async () => {
    const result = await this._cli.checkForUpdates();

    if (!result.success) {
      throw new Error(`Failed to check for updates (exit code ${result.exitCode}):\n${result.errors || result.output}`);
    }
  };
}
