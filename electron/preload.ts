import { contextBridge, ipcRenderer } from "electron";

contextBridge.exposeInMainWorld("electronAPI", {
  runCommand: (cmd: string, args: string[]) => ipcRenderer.invoke("run-command", cmd, args)
});
