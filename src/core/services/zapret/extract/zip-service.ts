import JSZip from "jszip";
import { mkdir, access, writeFile } from "fs/promises";
import { constants } from "fs";
import { join, dirname } from "node:path";

import type { ExtractResult } from "../types";

export class ZipService {
  public async extract(buffer: ArrayBuffer, targetDir: string): Promise<ExtractResult> {
    try {
      await access(targetDir, constants.F_OK);
    } catch {
      await mkdir(targetDir, { recursive: true });
    }

    const zip = await JSZip.loadAsync(buffer);
    let filesExtracted = 0;

    for (const [relativePath, zipEntry] of Object.entries(zip.files)) {
      if (zipEntry.dir) {
        const dirPath = join(targetDir, relativePath);
        await mkdir(dirPath, { recursive: true });
      } else {
        const filePath = join(targetDir, relativePath);
        const fileDir = dirname(filePath);

        await mkdir(fileDir, { recursive: true });

        const content = await zipEntry.async("uint8array");
        await writeFile(filePath, content);
        filesExtracted++;
      }
    }

    return { targetDir, filesExtracted };
  }
}
