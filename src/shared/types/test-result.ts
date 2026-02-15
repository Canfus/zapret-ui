export interface ConfigTestEntry {
  configName: string;
  passed: boolean;
  siteResults: SiteTestResult[];
}

export interface SiteTestResult {
  site: string;
  statusCode: number;
  responseTimeMs: number;
  success: boolean;
}

export interface TestResult {
  entries: ConfigTestEntry[];
  bestConfig: string | null;
  rawOutput: string;
}
