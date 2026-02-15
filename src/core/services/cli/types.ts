export enum ServiceBatMenuChoice {
  INSTALL = "1",
  REMOVE = "2",
  CHECK_STATUS = "3",
  GAME_FILTER = "4",
  IPSET_FILTER = "5",
  CHECK_UPDATES_TOGGLE = "6",
  UPDATE_IPSET = "7",
  UPDATE_HOSTS = "8",
  CHECK_UPDATES = "9",
  DIAGNOSTICS = "10",
  RUN_TESTS = "11",
  EXIT = "0"
}

export enum ServiceBatExternalCommand {
  STATUS_ZAPRET = "status_zapret",
  CHECK_UPDATES = "check_updates",
  LOAD_GAME_FILTER = "load_game_filter"
}

export interface CliCommandResult {
  success: boolean;
  output: string;
  errors: string;
  exitCode: number;
}
