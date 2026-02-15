import type { CliService } from "../services/cli";

export class ToggleGameFilterUseCase {
  constructor(private _cli: CliService) {}

  execute = async () => {
    const result = await this._cli.toggleGameFilter();

    if (!result.success) {
      throw new Error(
        `Failed to toggle game filter (exit code ${result.exitCode}):\n${result.errors || result.output}`
      );
    }
  };
}
