import type { IpcChannel } from "./channels";
import type { ServiceStatusInfo, ConfigFile, DiagnosticResult, TestResult } from "../../shared/types";

export interface IpcRequestMap {
  [IpcChannel.CHECK_INSTALLED]: void;
  [IpcChannel.INSTALL_ZAPRET]: void;
  [IpcChannel.GET_CONFIGS]: void;
  [IpcChannel.INSTALL_SERVICE]: { configIndex: number };
  [IpcChannel.REMOVE_SERVICE]: void;
  [IpcChannel.CHECK_STATUS]: void;
  [IpcChannel.TOGGLE_GAME_FILTER]: void;
  [IpcChannel.TOGGLE_CHECK_UPDATES]: void;
  [IpcChannel.CYCLE_IPSET]: void;
  [IpcChannel.UPDATE_IPSET]: void;
  [IpcChannel.UPDATE_HOSTS]: void;
  [IpcChannel.CHECK_APP_UPDATES]: void;
  [IpcChannel.RUN_DIAGNOSTICS]: void;
  [IpcChannel.RUN_TESTS]: void;
}

export interface IpcResponseMap {
  [IpcChannel.CHECK_INSTALLED]: boolean;
  [IpcChannel.INSTALL_ZAPRET]: void;
  [IpcChannel.GET_CONFIGS]: ConfigFile[];
  [IpcChannel.INSTALL_SERVICE]: void;
  [IpcChannel.REMOVE_SERVICE]: void;
  [IpcChannel.CHECK_STATUS]: ServiceStatusInfo;
  [IpcChannel.TOGGLE_GAME_FILTER]: void;
  [IpcChannel.TOGGLE_CHECK_UPDATES]: void;
  [IpcChannel.CYCLE_IPSET]: void;
  [IpcChannel.UPDATE_IPSET]: void;
  [IpcChannel.UPDATE_HOSTS]: void;
  [IpcChannel.CHECK_APP_UPDATES]: void;
  [IpcChannel.RUN_DIAGNOSTICS]: DiagnosticResult[];
  [IpcChannel.RUN_TESTS]: TestResult;
}

export interface IpcEventMap {
  [IpcChannel.INSTALL_PROGRESS]: number;
  [IpcChannel.TEST_LOG_LINE]: string;
}
