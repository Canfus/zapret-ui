export interface DownloadResult {
  buffer: ArrayBuffer;
  url: string;
  filename: string;
}

export interface ExtractResult {
  filesExtracted: number;
  targetDir: string;
}

export interface ZapretConfig {
  zapretDir: string;
  githubOwner: string;
  githubRepo: string;
  releaseAsset: string;
}
