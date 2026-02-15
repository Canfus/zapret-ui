import type { CliService } from "../services/cli";

export class CycleIpsetUseCase {
  constructor(private _cli: CliService) {}

  execute = async () => {
    const result = await this._cli.cycleIpset();

    if (!result.success) {
      throw new Error(`Failed to cycle IPSet (exit code ${result.exitCode}):\n${result.errors || result.output}`);
    }
  };
}
