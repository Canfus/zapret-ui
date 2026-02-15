import JSZip from "jszip";
import fs from "fs/promises";
import path from "path";

import type { ExtractResult } from "./types";

export class ArchiveService {
  async extractZip(buffer: Buffer, outputDir: string): Promise<ExtractResult> {
    const zip = await JSZip.loadAsync(buffer);

    const rootPrefix = this.detectSingleRootFolder(zip);

    await fs.mkdir(outputDir, { recursive: true });

    let fileCount = 0;
    let totalSize = 0;

    const entries = Object.entries(zip.files);

    for (const [relativePath, entry] of entries) {
      if (entry.dir) continue;

      let targetPath = relativePath;

      if (rootPrefix && targetPath.startsWith(rootPrefix)) {
        targetPath = targetPath.slice(rootPrefix.length);
      }

      const fullPath = path.join(outputDir, targetPath);

      if (!fullPath.startsWith(outputDir)) continue;

      await fs.mkdir(path.dirname(fullPath), { recursive: true });

      const content = await entry.async("nodebuffer");
      await fs.writeFile(fullPath, content);

      fileCount += 1;
      totalSize += content.length;
    }

    return { fileCount, totalSize, outputDir };
  }

  private detectSingleRootFolder(zip: JSZip): string | null {
    const paths = Object.keys(zip.files);

    if (paths.length === 0) return null;

    const rootFolders = new Set<string>();

    for (const path of paths) {
      const firstSlash = path.indexOf("/");

      if (firstSlash > 0) {
        rootFolders.add(path.slice(0, firstSlash + 1));
      } else {
        return null;
      }
    }

    if (rootFolders.size === 1) {
      return rootFolders.values().next().value ?? null;
    }

    return null;
  }
}
