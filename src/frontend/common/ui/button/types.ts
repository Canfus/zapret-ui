import type { motion } from "framer-motion";

import type { Variant, Size } from "../types";

export interface ButtonProps extends React.ComponentPropsWithRef<typeof motion.button> {
  variant?: Variant;
  size?: Size;
}
