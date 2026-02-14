import { ZapretConfig } from "./zapret-config";
import { DownloadService } from "../download";
import { GithubService } from "../download";
import { ZipService } from "../extract";

export class ServiceRegistry {
  public static getConfig() {
    return ZapretConfig.fromEnv();
  }

  public static getDownloadService() {
    return new DownloadService();
  }

  public static createGithubService() {
    const config = this.getConfig();
    const downloadService = this.getDownloadService();

    return new GithubService(downloadService, config);
  }

  public static getZipService() {
    return new ZipService();
  }
}
