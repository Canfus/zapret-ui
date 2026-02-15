import type { CliService } from "../services/cli";

export class ToggleCheckUpdatesUseCase {
  constructor(private _cli: CliService) {}

  async execute() {
    const result = await this._cli.toggleCheckUpdates();

    if (!result.success) {
      throw new Error(
        `Failed to toggle check updates (exit code ${result.exitCode}):\n${result.errors || result.output}`
      );
    }
  }
}
