import cn from "classnames";
import { motion } from "framer-motion";

import type { ButtonProps } from "./types";
import styles from "./button.module.css";

export const Button = ({ className, variant = "primary", size = "medium", ...props }: ButtonProps) => {
  return (
    <motion.button
      {...props}
      whileHover={{ scale: 1.05 }}
      whileTap={{ scale: 0.95 }}
      className={cn(
        styles.button,
        {
          [styles["button--primary"]]: variant === "primary",
          [styles["button--secondary"]]: variant === "secondary",
          [styles["button--plain"]]: variant === "plain",
          [styles["button--large"]]: size === "large",
          [styles["button--medium"]]: size === "medium",
          [styles["button--small"]]: size === "small"
        },
        className
      )}
    />
  );
};
