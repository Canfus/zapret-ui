import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { App } from "./frontend";
import "@assets/main.css";

const root = document.getElementById("root");

if (!root) {
  throw new Error("Could not find root node");
}

createRoot(root).render(
  <StrictMode>
    <App />
  </StrictMode>
);
