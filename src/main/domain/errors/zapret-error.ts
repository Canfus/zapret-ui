export enum ZapretErrorCode {
  NOT_INSTALLED = "NOT_INSTALLED",
  UNCAUSED = "UNCAUSED"
}

export class ZapretError extends Error {
  public readonly name: string = "ZapretError";

  constructor(
    message: string,
    public code?: ZapretErrorCode
  ) {
    super(message);
  }
}

export class ZapretNotInstalledError extends ZapretError {
  constructor() {
    super("Zapret not installed", ZapretErrorCode.NOT_INSTALLED);
  }
}
