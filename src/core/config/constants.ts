import path from "path";
import os from "os";

// TODO: подумать что можно вынести в env

export const ZAPRET_DIR = path.join(
  process.env.APPDATA || path.join(os.homedir(), "AppData", "Roaming"),
  "zapret-discord-youtube"
);

export const SERVICE_NAME = "zapret";
export const SERVICE_BAT = "service.bat";
export const CONFIG_GLOB = "general*.bat";

export const GITHUB_OWNER = "Flowseal";
export const GITHUB_REPO = "zapret-discord-youtube";
export const GITHUB_API_BASE = "https://api.github.com";

export const REGISTRY_KEY_PATH = `HKLM\\System\\CurrentControlSet\\Services\\${SERVICE_NAME}`;
export const REGISTRY_VALUE_NAME = "zapret-discord-youtube";

export const TIMEOUTS = {
  SHELL_DEFAULT: 30_000,
  SERVICE_BAT: 120_000,
  TESTS: 600_000,
  DOWNLOAD: 120_000,
  STDIN_INITIAL_DELAY: 500,
  STDIN_STEP_DELAY: 300
} as const;

export const TEST_SCRIPT_PATH = "utils\\test zapret.ps1";
