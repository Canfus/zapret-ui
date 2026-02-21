import cn from "classnames";
import { motion, AnimatePresence } from "framer-motion";

import { Spinner } from "../spinner";
import { TurnOnIcon } from "../icons";
import type { SwitchButtonProps } from "./types";
import styles from "./switch-button.module.css";

export const SwitchButton = ({ loading, active, className, disabled, ...props }: SwitchButtonProps) => {
  const isDisabled = disabled || loading;

  return (
    <div className={styles.wrapper}>
      <motion.button
        {...props}
        whileHover={!isDisabled ? { scale: 1.05 } : undefined}
        whileTap={!isDisabled ? { scale: 0.95 } : undefined}
        disabled={isDisabled}
        className={cn(
          styles.button,
          {
            [styles["button--active"]]: active,
            [styles["button--loading"]]: loading
          },
          className
        )}
      >
        <TurnOnIcon />
      </motion.button>
      {loading && (
        <AnimatePresence>
          <motion.div initial={{ scale: 0.5 }} animate={{ scale: 1 }} className={styles.spinner}>
            <Spinner size={170} />
          </motion.div>
        </AnimatePresence>
      )}
    </div>
  );
};
