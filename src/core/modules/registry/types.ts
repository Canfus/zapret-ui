export enum RegistryValueType {
  REG_SZ = "REG_SZ",
  REG_DWORD = "REG_DWORD",
  REG_EXPAND_SZ = "REG_EXPAND_SZ",
  REG_MULTI_SZ = "REG_MULTI_SZ"
}

export interface RegistryValue {
  name: string;
  type: RegistryValueType;
  data: string;
}
