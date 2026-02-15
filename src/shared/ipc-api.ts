import type { ServiceStatusInfo, ConfigFile, DiagnosticResult, TestResult } from "./types";

export interface ElectronAPI {
  checkInstalled(): Promise<boolean>;
  installZapret(): Promise<void>;
  onInstallProgress(callback: (percent: number) => void): () => void;
  getConfigs(): Promise<ConfigFile[]>;
  installService(configIndex: number): Promise<void>;
  removeService(): Promise<void>;
  checkStatus(): Promise<ServiceStatusInfo>;
  toggleGameFilter(): Promise<void>;
  toggleCheckUpdates(): Promise<void>;
  cycleIpset(): Promise<void>;
  updateIpset(): Promise<void>;
  updateHosts(): Promise<void>;
  checkAppUpdates(): Promise<void>;
  runDiagnostics(): Promise<DiagnosticResult[]>;
  runTests(): Promise<TestResult>;
  onTestLogLine(callback: (line: string) => void): () => void;
}

declare global {
  interface Window {
    electronAPI: ElectronAPI;
  }
}
