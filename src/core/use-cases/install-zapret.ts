import type { GithubService } from "../modules/github";
import type { DownloadService } from "../modules/download";
import type { ArchiveService } from "../modules/archive";
import type { FilesystemService } from "../modules/filesystem";

export class InstallZapretUseCase {
  constructor(
    private _github: GithubService,
    private _download: DownloadService,
    private _archive: ArchiveService,
    private _filesystem: FilesystemService,
    private _zapretDir: string
  ) {}

  async isInstalled(): Promise<boolean> {
    return this._filesystem.exists(this._zapretDir);
  }

  async execute(onProgress?: (percent: number) => void) {
    const report = onProgress ?? (() => {});

    const installed = await this.isInstalled();

    if (installed) return;

    report(0);

    const release = await this._github.getLatestRelease();
    report(5);

    const zipAsset = this._github.findZipAsset(release);

    if (!zipAsset) {
      throw new Error(`No zip asset found for ${release.tag_name}`);
    }

    report(10);

    const buffer = await this._download.downloadWithProgress(zipAsset.browser_download_url, (loaded, total) => {
      const downloadProgress = total > 0 ? loaded / total : 0;
      report(10 + Math.round(downloadProgress * 75));
    });
    report(85);

    await this._archive.extractZip(buffer, this._zapretDir);
    report(100);
  }
}
