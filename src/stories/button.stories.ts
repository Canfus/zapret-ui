import type { Meta, StoryObj } from "@storybook/react";

import { Button } from "@common/ui";
const meta = {
  title: "UI/Button",
  component: Button,
  parameters: {
    layout: "centered"
  },
  tags: ["autodocs"],
  args: {
    size: "medium",
    variant: "primary"
  }
} satisfies Meta<typeof Button>;

export default meta;
type Story = StoryObj<typeof meta>;

export const MediumPrimary: Story = {
  args: {}
};
