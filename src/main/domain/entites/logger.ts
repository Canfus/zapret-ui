import { appendFile } from 'fs/promises';

import { ZapretPath } from '../value-objects';

export class ZapretLogger {
  constructor(private _classExecutor: string) {}

  async write(message: string) {
    await appendFile(
      ZapretPath.logsDir(),
      `[${new Date().toISOString()}] ${this._classExecutor}: ${message}`
    );
  }
}
