import { homedir } from "os";
import { join } from "path";

export class ZapretPath {
  static readonly BASE_DIR = join(
    homedir(),
    "AppData",
    "Roaming",
    "zapret-discrord-youtube"
  );

  static serviceBat() {
    return join(this.BASE_DIR, "service.bat");
  }

  static utilsDir() {
    return join(this.BASE_DIR, "utils");
  }

  static logsDir() {
    return join(this.BASE_DIR, "gui-logs.txt");
  }
}
