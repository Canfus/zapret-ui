import type { ZapretConfig as ZapretConfigBase } from "../types";
import { DEFAULT_ZAPRET_DIR, ZAPRET_REPO, ZAPRET_OWNER, RELEASE_ASSET } from "./consts";

export class ZapretConfig implements ZapretConfigBase {
  readonly zapretDir: string;
  readonly githubOwner: string;
  readonly githubRepo: string;
  readonly releaseAsset: string;

  private static _instance: ZapretConfig;

  private constructor(config: ZapretConfigBase) {
    this.zapretDir = config.zapretDir;
    this.githubOwner = config.githubOwner;
    this.githubRepo = config.githubRepo;
    this.releaseAsset = config.releaseAsset;
  }

  public static fromEnv(): ZapretConfig {
    if (!ZapretConfig._instance) {
      this._instance = new ZapretConfig({
        zapretDir: process.env.ZAPRET_DIR ?? DEFAULT_ZAPRET_DIR,
        githubOwner: ZAPRET_OWNER,
        githubRepo: ZAPRET_REPO,
        releaseAsset: RELEASE_ASSET
      });
    }

    return this._instance;
  }
}
