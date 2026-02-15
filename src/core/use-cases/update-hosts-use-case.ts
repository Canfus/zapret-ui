import type { CliService } from "../services/cli";

export class UpdateHostsUseCase {
  constructor(private _cli: CliService) {}

  async execute() {
    const result = await this._cli.updateHosts();

    if (!result.success) {
      throw new Error(`Failed to update hosts (exit code ${result.exitCode}):\n${result.errors || result.output}}`);
    }
  }
}
