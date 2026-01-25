export enum ServiceCommand {
  InstallService = "install-service",
  RemoveService = "remove-service",
  CheckStatus = "check-status",
  RunDiagnostics = "run-diagnostics",
  CheckUpdates = "check-updates",
  SwitchCheckUpdates = "switch-check-updates",
  SwitchGameFilter = "switch-game-filter",
  SwitchIpset = "switch-ipset",
  UpdateIpsetList = "update-ipset-list",
  UpdateHostsFile = "update-hosts-file",
  RunTests = "run-tests"
}
