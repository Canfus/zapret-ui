export enum IpcChannel {
  CHECK_INSTALLED = "zapret:check-installed",
  INSTALL_ZAPRET = "zapret:install",
  INSTALL_PROGRESS = "zapret:install-progress:event",

  GET_CONFIGS = "service:get-configs",
  INSTALL_SERVICE = "service:install",
  REMOVE_SERVICE = "service:remove",
  CHECK_STATUS = "service:check-status",

  TOGGLE_GAME_FILTER = "service:toggle-game-filter",
  TOGGLE_CHECK_UPDATES = "service:toggle-check-updates",
  CYCLE_IPSET = "service:cycle-ipset",

  UPDATE_IPSET = "updates:update-ipset",
  UPDATE_HOSTS = "updates:update-hosts",
  CHECK_APP_UPDATES = "updates:check-updates",

  RUN_DIAGNOSTICS = "tools:run-diagnostics",
  RUN_TESTS = "tools:run-tests",
  TEST_LOG_LINE = "tools:test-log-line:event"
}
