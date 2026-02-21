import type { motion } from "framer-motion";

export interface SwitchButtonProps extends React.ComponentPropsWithRef<typeof motion.button> {
  loading?: boolean;
  active?: boolean;
}
