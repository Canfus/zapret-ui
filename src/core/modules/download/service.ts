import { net } from "electron";

import { TIMEOUTS } from "../../config/constants";
import type { DownloadProgressCallback } from "./types";

export class DownloadService {
  downloadWithProgress(url: string, onProgress: DownloadProgressCallback): Promise<Buffer> {
    return new Promise((resolve, reject) => {
      const request = net.request({
        url,
        method: "GET"
      });

      const timeout = setTimeout(() => {
        request.abort();
        reject(new Error(`Download timed out after ${TIMEOUTS.DOWNLOAD}ms`));
      }, TIMEOUTS.DOWNLOAD);

      request.on("response", (response) => {
        if (response.statusCode && response.statusCode >= 300 && response.statusCode < 400) {
          const location = response.headers["location"];
          const redirectUrl = Array.isArray(location) ? location[0] : location;

          if (redirectUrl) {
            this.downloadWithProgress(redirectUrl, onProgress).then(resolve).catch(reject);

            return;
          }
        }

        if (response.statusCode && response.statusCode >= 400) {
          clearTimeout(timeout);
          reject(new Error(`HTTP ${response.statusCode} downloading ${url}`));

          return;
        }

        const contentLength = response.headers["content-length"];
        const total = contentLength ? parseInt(Array.isArray(contentLength) ? contentLength[0] : contentLength, 10) : 0;

        const chunks: Buffer[] = [];
        let loaded = 0;

        response.on("data", (chunk: Buffer) => {
          chunks.push(chunk);
          loaded += chunk.length;

          if (onProgress && total > 0) {
            onProgress(loaded, total);
          }
        });

        response.on("end", () => {
          clearTimeout(timeout);
          resolve(Buffer.concat(chunks));
        });

        response.on("error", (err: Error) => {
          clearTimeout(timeout);
          reject(err);
        });
      });

      request.on("error", (err: Error) => {
        clearTimeout(timeout);
        reject(err);
      });

      request.end();
    });
  }
}
