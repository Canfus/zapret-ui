export interface GithubAsset {
  name: string;
  browser_download_url: string;
  size: string;
  content_type: string;
}

export interface GithubRelease {
  tag_name: string;
  name: string;
  body: string;
  published_at: string;
  assets: GithubAsset[];
  html_url: string;
}
