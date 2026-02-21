import packageJson from "../package.json" with { type: "json" };

const { ["scripts:available"]: current, ...availableScripts } = packageJson.scripts;

console.table(availableScripts);
