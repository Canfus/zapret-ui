import type { DownloadResult } from "../types";

export class DownloadService {
  public async download(url: string): Promise<DownloadResult> {
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`HTTP error ${response.status}: ${response.statusText}`);
    }

    const buffer = await response.arrayBuffer();
    const filename = decodeURIComponent(new URL(url).pathname.split("/").pop() ?? "");

    return {
      buffer,
      url,
      filename
    };
  }
}
