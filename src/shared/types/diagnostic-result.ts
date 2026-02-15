export enum DiagnosticSeverity {
  OK = "OK",
  WARNING = "WARNING",
  FAIL = "FAIL"
}

export interface DiagnosticResult {
  name: string;
  severity: DiagnosticSeverity;
  message: string;
}
