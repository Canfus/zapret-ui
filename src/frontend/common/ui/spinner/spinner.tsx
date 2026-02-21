import type { SpinnerProps } from "./types";
import styles from "./spinner.module.css";

export const Spinner = ({ size = 24 }: SpinnerProps) => (
  <div style={{ "--size": `${size}px` } as React.CSSProperties} className={styles.spinner} />
);
