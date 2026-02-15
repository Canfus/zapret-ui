import { contextBridge, ipcRenderer } from "electron";

import { IpcChannel } from "../src/core/ipc/channels.ts";
import type { ElectronAPI } from "../src/shared/ipc-api.ts";

const electronAPI: ElectronAPI = {
  // === Lifecycle ===
  checkInstalled: () => ipcRenderer.invoke(IpcChannel.CHECK_INSTALLED),
  installZapret: () => ipcRenderer.invoke(IpcChannel.INSTALL_ZAPRET),
  onInstallProgress: (callback: (percent: number) => void) => {
    const handler = (_event: Electron.IpcRendererEvent, percent: number) => callback(percent);
    ipcRenderer.on(IpcChannel.INSTALL_PROGRESS, handler);
    // Возвращаем функцию отписки
    return () => {
      ipcRenderer.removeListener(IpcChannel.INSTALL_PROGRESS, handler);
    };
  },

  // === Service ===
  getConfigs: () => ipcRenderer.invoke(IpcChannel.GET_CONFIGS),
  installService: (configIndex: number) => ipcRenderer.invoke(IpcChannel.INSTALL_SERVICE, { configIndex }),
  removeService: () => ipcRenderer.invoke(IpcChannel.REMOVE_SERVICE),
  checkStatus: () => ipcRenderer.invoke(IpcChannel.CHECK_STATUS),

  // === Settings ===
  toggleGameFilter: () => ipcRenderer.invoke(IpcChannel.TOGGLE_GAME_FILTER),
  toggleCheckUpdates: () => ipcRenderer.invoke(IpcChannel.TOGGLE_CHECK_UPDATES),
  cycleIpset: () => ipcRenderer.invoke(IpcChannel.CYCLE_IPSET),

  // === Updates ===
  updateIpset: () => ipcRenderer.invoke(IpcChannel.UPDATE_IPSET),
  updateHosts: () => ipcRenderer.invoke(IpcChannel.UPDATE_HOSTS),
  checkAppUpdates: () => ipcRenderer.invoke(IpcChannel.CHECK_APP_UPDATES),

  // === Tools ===
  runDiagnostics: () => ipcRenderer.invoke(IpcChannel.RUN_DIAGNOSTICS),
  runTests: () => ipcRenderer.invoke(IpcChannel.RUN_TESTS),
  onTestLogLine: (callback: (line: string) => void) => {
    const handler = (_event: Electron.IpcRendererEvent, line: string) => callback(line);
    ipcRenderer.on(IpcChannel.TEST_LOG_LINE, handler);
    return () => {
      ipcRenderer.removeListener(IpcChannel.TEST_LOG_LINE, handler);
    };
  }
};

contextBridge.exposeInMainWorld("electronAPI", electronAPI);
