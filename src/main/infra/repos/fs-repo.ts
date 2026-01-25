import {mkdir, readdir, access} from 'fs/promises';
import {join} from 'path';

import {ZapretConfig, ZapretLogger} from '@domain/entites';
import {ZapretPath} from '@domain/value-objects';
import {ZapretError, ZapretErrorCode} from '@domain/errors';

export interface FileRepository {
  ensureZapretDir(): Promise<void>;
  listConfigs(): Promise<ZapretConfig[]>;
  zapretExists(): Promise<boolean>;
  writeLog(content: string): Promise<void>;
}

export class FsRepository implements FileRepository {
  private readonly _zapretDir = ZapretPath.BASE_DIR;
  private readonly _logger = new ZapretLogger(FsRepository.name);

  async ensureZapretDir() {
    await mkdir(this._zapretDir, { recursive: true });
  }

  async listConfigs(): Promise<ZapretConfig[]> {
    try {
      const files = await readdir(this._zapretDir);

      return files
        .filter((file) => /^general/i.test(file))
        .map<ZapretConfig>((file) => ({
          name: file,
          path: join(this._zapretDir, file),
          displayName: file.replace(/^general-?/, '').replace(/\.bat$/i, '')
        }));
    } catch (error) {
      if (error instanceof NodeJS.ErrnoException) {
        if (error.code === 'ENOENT') {
          return [];
        }

      }

      throw new ZapretError('Failed to load list of configs', ZapretErrorCode.UNCAUSED);
    }
  }

  async zapretExists() {
    try {
      await access(ZapretPath.serviceBat());

      return true;
    } catch {
      return false;
    }
  }

  async writeLog(content: string) {
    await this._logger.write(content);
  }
}
