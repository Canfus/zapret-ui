import { net } from "electron";

import { GITHUB_API_BASE, GITHUB_OWNER, GITHUB_REPO } from "../../config/constants";
import type { GithubAsset, GithubRelease } from "./types";

export class GithubService {
  private readonly baseUrl: string;

  constructor(owner: string = GITHUB_OWNER, repo: string = GITHUB_REPO) {
    this.baseUrl = `${GITHUB_API_BASE}/repos/${owner}/${repo}`;
  }

  async getLatestRelease(): Promise<GithubRelease> {
    return await this.fetchJson<GithubRelease>(`${this.baseUrl}/releases/latest`);
  }

  async getReleases(perPage: number = 5): Promise<GithubRelease[]> {
    return this.fetchJson<GithubRelease[]>(`${this.baseUrl}/releases?per_page=${perPage}`);
  }

  findZipAsset(release: GithubRelease): GithubAsset | null {
    return (
      release.assets.find(
        (asset) => asset.name.endsWith(".zip") && asset.content_type !== "application/x-rar-compressed"
      ) ?? null
    );
  }

  private fetchJson<T>(url: string): Promise<T> {
    return new Promise((resolve, reject) => {
      const request = net.request({
        url,
        method: "GET"
      });

      request.setHeader("User-Agent", "zapret-ui"); // TODO: посмотреть на это ещё
      request.setHeader("Accept", "application/vnd.github.v3+json");

      request.on("response", (response) => {
        if (response.statusCode && response.statusCode >= 400) {
          reject(new Error(`Github API returned ${response.statusCode} for ${url}`));
          return;
        }

        const chunks: Buffer[] = [];

        response.on("data", (chunk) => {
          chunks.push(chunk);
        });

        response.on("end", () => {
          try {
            const body = Buffer.concat(chunks).toString("utf-8");

            resolve(JSON.parse(body) as T);
          } catch (err) {
            reject(new Error(`Failed to parse Github API response: ${err}`));
          }
        });

        response.on("error", reject);
      });

      request.on("error", reject);

      request.end();
    });
  }
}
