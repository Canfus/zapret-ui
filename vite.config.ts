import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";
import electron from "vite-plugin-electron/simple";

export default defineConfig({
  plugins: [
    react(),
    tsconfigPaths(),
    electron({
      main: { entry: "./electron/main.ts" },
      preload: { input: "./electron/preload.ts" }
      // renderer: {}
    })
  ],
  base: "./"
});
