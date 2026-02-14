import { DownloadService } from "./download-service";
import { ZapretConfig } from "../config/zapret-config";
import type { DownloadResult } from "../types";

interface GithubReleaseAsset {
  name: string;
  browser_download_url: string;
  size: number;
}

export interface GithubRelease {
  tag_name: string;
  name: string;
  assets: GithubReleaseAsset[];
}

export class GithubService {
  private readonly GITHUB_BASE_URL: string = "https://api.github.com/repos";

  constructor(
    private readonly downloadService: DownloadService,
    private readonly config: ZapretConfig
  ) {}

  public async getLatestRelease(): Promise<GithubRelease> {
    const url = [this.GITHUB_BASE_URL, this.config.githubOwner, this.config.githubRepo, "releases", "latest"].join("/");

    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`Github API error ${response.status}: ${response.statusText}`);
    }

    return await response.json();
  }

  public async downloadLatestAsset(): Promise<DownloadResult> {
    const release = await this.getLatestRelease();

    const asset = release.assets.find((asset) =>
      asset.name.toLowerCase().includes(this.config.releaseAsset.toLowerCase())
    );

    if (!asset) {
      throw new Error("Cannot find asset");
    }

    return this.downloadService.download(asset.browser_download_url);
  }
}
