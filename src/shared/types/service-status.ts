export enum ServiceStatus {
  RUNNING = "RUNNING",
  STOPPED = "STOPPED",
  NOT_INSTALLED = "NOT_INSTALLED",
  UNKNOWN = "UNKNOWN"
}

export interface ServiceStatusInfo {
  status: ServiceStatus;
  currentConfig: string | null;
  rawOutput?: string;
}
