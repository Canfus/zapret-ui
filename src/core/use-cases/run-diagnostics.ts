import type { CliService } from "../services/cli";
import type { DiagnosticResult } from "../../shared/types";

export class RunDiagnosticsUseCase {
  constructor(private _cli: CliService) {}

  execute = async (): Promise<DiagnosticResult[]> => {
    return this._cli.runDiagnostics();
  };
}
