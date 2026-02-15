import { ipcMain, type BrowserWindow } from "electron";

import { IpcChannel } from "./channels";
import { ZAPRET_DIR } from "../config/constants";
import { ShellService } from "../modules/shell";
import { FilesystemService } from "../modules/filesystem";
import { RegistryService } from "../modules/registry";
import { DownloadService } from "../modules/download";
import { ArchiveService } from "../modules/archive";
import { GithubService } from "../modules/github";
import { CliService } from "../services/cli";
import { InstallZapretUseCase } from "../use-cases/install-zapret";
import { GetConfigsUseCase } from "../use-cases/get-configs";
import { InstallServiceUseCase } from "../use-cases/install-service";
import { RemoveServiceUseCase } from "../use-cases/remove-service";
import { CheckStatusUseCase } from "../use-cases/check-status";
import { ToggleGameFilterUseCase } from "../use-cases/toggle-game-filter";
import { ToggleCheckUpdatesUseCase } from "../use-cases/toggle-check-updates";
import { CycleIpsetUseCase } from "../use-cases/cycle-ipset";
import { UpdateIpsetUseCase } from "../use-cases/update-ipset";
import { UpdateHostsUseCase } from "../use-cases/update-hosts-use-case";
import { CheckUpdatesUseCase } from "../use-cases/check-updates";
import { RunDiagnosticsUseCase } from "../use-cases/run-diagnostics";
import { RunTestsUseCase } from "../use-cases/run-tests";

export function registerIpcHandlers(mainWindow: BrowserWindow) {
  // === services init ===
  const shell = new ShellService();
  const filesystem = new FilesystemService();
  const registry = new RegistryService(shell);
  const download = new DownloadService();
  const archive = new ArchiveService();
  const github = new GithubService();

  const cli = new CliService(shell, ZAPRET_DIR);

  const installZapret = new InstallZapretUseCase(github, download, archive, filesystem, ZAPRET_DIR);
  const getConfigs = new GetConfigsUseCase(filesystem, ZAPRET_DIR);
  const installService = new InstallServiceUseCase(cli);
  const removeService = new RemoveServiceUseCase(cli);
  const checkStatus = new CheckStatusUseCase(cli, registry);
  const toggleGameFilter = new ToggleGameFilterUseCase(cli);
  const toggleCheckUpdates = new ToggleCheckUpdatesUseCase(cli);
  const cycleIpset = new CycleIpsetUseCase(cli);
  const updateIpset = new UpdateIpsetUseCase(cli);
  const updateHosts = new UpdateHostsUseCase(cli);
  const checkUpdates = new CheckUpdatesUseCase(cli);
  const runDiagnostics = new RunDiagnosticsUseCase(cli);
  const runTests = new RunTestsUseCase(cli);

  // === register services ===
  ipcMain.handle(IpcChannel.CHECK_INSTALLED, installZapret.isInstalled);
  ipcMain.handle(IpcChannel.INSTALL_ZAPRET, async () => {
    await installZapret.execute((percent) => {
      mainWindow.webContents.send(IpcChannel.INSTALL_PROGRESS, percent);
    });
  });
  ipcMain.handle(IpcChannel.GET_CONFIGS, getConfigs.execute);
  ipcMain.handle(IpcChannel.INSTALL_SERVICE, async (_event, args: { configIndex: number }) => {
    await installService.execute(args.configIndex);
  });
  ipcMain.handle(IpcChannel.REMOVE_SERVICE, removeService.execute);
  ipcMain.handle(IpcChannel.CHECK_STATUS, checkStatus.execute);

  // === register settings ===
  ipcMain.handle(IpcChannel.TOGGLE_GAME_FILTER, toggleGameFilter.execute);
  ipcMain.handle(IpcChannel.TOGGLE_CHECK_UPDATES, toggleCheckUpdates.execute);
  ipcMain.handle(IpcChannel.CYCLE_IPSET, cycleIpset.execute);

  // === register updates ===
  ipcMain.handle(IpcChannel.UPDATE_IPSET, updateIpset.execute);
  ipcMain.handle(IpcChannel.UPDATE_HOSTS, updateHosts.execute);
  ipcMain.handle(IpcChannel.CHECK_APP_UPDATES, checkUpdates.execute);

  // === register tools ===
  ipcMain.handle(IpcChannel.RUN_DIAGNOSTICS, runDiagnostics.execute);
  ipcMain.handle(IpcChannel.RUN_TESTS, async () => {
    return runTests.execute((line) => {
      mainWindow.webContents.send(IpcChannel.TEST_LOG_LINE, line);
    });
  });
}

export function removeIpcHandlers() {
  for (const channel of Object.values(IpcChannel)) {
    ipcMain.removeHandler(channel);
  }
}
