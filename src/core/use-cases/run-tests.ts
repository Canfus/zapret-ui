import type { CliService } from "../services/cli";
import type { TestResult } from "../../shared/types";

export class RunTestsUseCase {
  constructor(private _cli: CliService) {}

  async execute(onLine?: (line: string) => void): Promise<TestResult> {
    return this._cli.runTests(onLine);
  }
}
