export interface ShellResult {
  exitCode: number;
  stdout: string;
  stderr: string;
}

export interface ShellInteractiveOptions {
  cwd?: string;
  env?: Record<string, string>;
  menuChoices: string[];
  timeout?: number;
}

export interface ShellStreamOptions {
  cwd?: string;
  env?: Record<string, string>;
  timeout?: number;
}
