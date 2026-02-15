import {
  ServiceStatus,
  type ServiceStatusInfo,
  DiagnosticSeverity,
  type DiagnosticResult,
  type ConfigTestEntry,
  type TestResult
} from "../../../shared/types";

export class CliParser {
  static parseStatus(stdout: string): ServiceStatusInfo {
    const info: ServiceStatusInfo = {
      status: ServiceStatus.UNKNOWN,
      currentConfig: null,
      rawOutput: stdout
    };

    const configMatch = stdout.match(/installed from "([^"]+)"/i);

    if (configMatch) {
      info.currentConfig = configMatch[1];
    }

    switch (true) {
      case /service is RUNNING/i.test(stdout):
        info.status = ServiceStatus.RUNNING;
        break;
      case /service is NOT running/i.test(stdout):
        info.status = ServiceStatus.STOPPED;
        break;
      case /is not installed/i.test(stdout):
        info.status = ServiceStatus.NOT_INSTALLED;
    }

    if (info.status === ServiceStatus.RUNNING && /winws\.exe\) is NOT running/i.test(stdout)) {
      info.status = ServiceStatus.STOPPED;
    }

    return info;
  }

  static parseDiagnostics(stdout: string): DiagnosticResult[] {
    const results: DiagnosticResult[] = [];
    const lines = stdout.split("\n");

    for (const line of lines) {
      const trimmed = line.trim();

      if (!trimmed) continue;

      const writeHostMatch = trimmed.match(/Write-Host\s+"([^"]+)"\s-ForegroundColor\s+(\W+)/i);

      if (writeHostMatch) {
        const message = writeHostMatch[1];
        const color = writeHostMatch[2].toLowerCase();

        let severity: DiagnosticSeverity;

        switch (color) {
          case "green":
            severity = DiagnosticSeverity.OK;
            break;
          case "yellow":
            severity = DiagnosticSeverity.WARNING;
            break;
          case "red":
            severity = DiagnosticSeverity.FAIL;
            break;
          default:
            continue;
        }

        const name = this.extractCheckName(message);

        results.push({ name, severity, message });
        continue;
      }

      if (trimmed.includes("check passed")) {
        const name = trimmed.replace(/\s*check passed*$/i, "").trim();

        results.push({
          name: name || "Unknown check",
          severity: DiagnosticSeverity.OK,
          message: trimmed
        });
      } else if (trimmed.startsWith("[X]")) {
        const message = trimmed.replace(/^\[X]\s*/i, "");
        const name = this.extractCheckName(message);

        results.push({
          name,
          severity: DiagnosticSeverity.FAIL,
          message
        });
      } else if (trimmed.startsWith("[?]")) {
        const message = trimmed.replace(/^\[?]\s*/i, "");
        const name = this.extractCheckName(message);

        results.push({
          name,
          severity: DiagnosticSeverity.WARNING,
          message
        });
      }
    }

    return this.deduplicateDiagnostics(results);
  }

  static parseTestResults(stdout: string): TestResult {
    const result: TestResult = {
      entries: [],
      bestConfig: null,
      rawOutput: stdout
    };

    const lines = stdout.split("\n");
    let currentEntry: ConfigTestEntry | null = null;

    for (const line of lines) {
      const trimmed = line.trim();

      const configMatch = trimmed.match(/testing\s+(?:config[:\s]*)?(\S+)/i);
      if (configMatch) {
        if (currentEntry) {
          currentEntry.passed = currentEntry.siteResults.every((r) => r.success);
          result.entries.push(currentEntry);
        }
        currentEntry = {
          configName: configMatch[1].replace(/\.bat$/i, ""),
          passed: false,
          siteResults: []
        };
        continue;
      }

      if (currentEntry) {
        const siteMatch = trimmed.match(/(\S+):\s*(\d+)\s*\((\d+)\s*ms\)/i);
        if (siteMatch) {
          const statusCode = parseInt(siteMatch[2], 10);
          currentEntry.siteResults.push({
            site: siteMatch[1],
            statusCode,
            responseTimeMs: parseInt(siteMatch[3], 10),
            success: statusCode >= 200 && statusCode < 400
          });
          continue;
        }

        const altMatch = trimmed.match(/(\S+):\s*(OK|FAIL|TIMEOUT|ERROR)/i);
        if (altMatch) {
          currentEntry.siteResults.push({
            site: altMatch[1],
            statusCode: altMatch[2].toUpperCase() === "OK" ? 200 : -1,
            responseTimeMs: 0,
            success: altMatch[2].toUpperCase() === "OK"
          });
          continue;
        }
      }

      const bestMatch = trimmed.match(/best\s+config[:\s]*(\S+)/i);
      if (bestMatch) {
        result.bestConfig = bestMatch[1].replace(/\.bat$/i, "");
      }
    }

    if (currentEntry) {
      currentEntry.passed = currentEntry.siteResults.every((r) => r.success);
      result.entries.push(currentEntry);
    }

    return result;
  }

  private static extractCheckName(message: string): string {
    const cleaned = message
      .replace(/^\[X\]\s*/i, "")
      .replace(/^\[\?]\s*/, "")
      .replace(/https?:\/\/\S+/g, "")
      .trim();

    const sentence = cleaned.split(/[.!]/).filter(Boolean)[0] ?? cleaned;

    return sentence.trim().slice(0, 80);
  }

  private static deduplicateDiagnostics(results: DiagnosticResult[]): DiagnosticResult[] {
    const seen = new Map<string, DiagnosticResult>();

    for (const result of results) {
      const exists = seen.get(result.name);

      if (!exists) {
        seen.set(result.name, result);
      } else {
        const severityOrder = { OK: 0, WARNING: 1, FAIL: 2 };

        if (severityOrder[result.severity] > severityOrder[exists.severity]) {
          seen.set(result.name, result);
        }
      }
    }

    return Array.from(seen.values());
  }
}
