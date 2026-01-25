export interface ZapretStatus {
  activeConfig: string | null;
  flags: FlagRecord<"checkUpdated" | "gameFilter" | "ipsetLoaded">;
  serviceRunning: boolean;
}
