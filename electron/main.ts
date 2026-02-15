import { app, BrowserWindow } from "electron";
import { join, dirname } from "node:path";
import { fileURLToPath } from "url";

import { registerIpcHandlers, removeIpcHandlers } from "../src/core/ipc/handlers";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

let window: BrowserWindow | null = null;

function createWindow() {
  window = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: join(__dirname, "preload.mjs"),
      nodeIntegration: false,
      contextIsolation: true
    }
  });

  registerIpcHandlers(window);

  if (process.env.VITE_DEV_SERVER_URL) {
    window.loadURL(process.env.VITE_DEV_SERVER_URL);
    window.webContents.openDevTools();
  } else {
    window.loadFile(join(__dirname, "../dist/index.html"));
  }
}

app.whenReady().then(createWindow);

app.on("window-all-closed", () => {
  removeIpcHandlers();
  if (process.platform !== "darwin") app.quit();
});

app.on("activate", () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow();
});
