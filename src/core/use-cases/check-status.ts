import type { CliService } from "../services/cli";
import { RegistryService } from "../modules/registry";
import { REGISTRY_KEY_PATH, REGISTRY_VALUE_NAME } from "../config/constants";
import { ServiceStatus, type ServiceStatusInfo } from "../../shared/types";

export class CheckStatusUseCase {
  constructor(
    private _cli: CliService,
    private _registry: RegistryService
  ) {}

  async execute(): Promise<ServiceStatusInfo> {
    const status = await this._cli.checkStatus();

    if (!status.currentConfig && status.status !== ServiceStatus.NOT_INSTALLED) {
      const regConfig = await this._registry.queryValue(REGISTRY_KEY_PATH, REGISTRY_VALUE_NAME);

      if (regConfig) {
        status.currentConfig = regConfig;
      }
    }

    return status;
  }
}
