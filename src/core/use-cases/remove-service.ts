import type { CliService } from "../services/cli";

export class RemoveServiceUseCase {
  constructor(private _cli: CliService) {}

  execute = async () => {
    const result = await this._cli.removeService();

    if (!result.success) {
      throw new Error(`Failed to remove service (exit code ${result.exitCode}):\n${result.errors || result.output}`);
    }
  };
}
