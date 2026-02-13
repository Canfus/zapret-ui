import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

const root = document.getElementById("root");

if (!root) {
  throw new Error("Could not find root node");
}

createRoot(root).render(
  <StrictMode>
    <div>hello world</div>
  </StrictMode>
);
