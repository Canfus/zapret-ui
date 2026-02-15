import type { CliService } from "../services/cli";

export class UpdateIpsetUseCase {
  constructor(private _cli: CliService) {}

  execute = async () => {
    const result = await this._cli.updateIpset();

    if (!result.success) {
      throw new Error(`Failed to update ipset (exit code ${result.exitCode}):\n${result.errors || result.output}`);
    }
  };
}
