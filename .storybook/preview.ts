import type { Preview } from "@storybook/react-vite";

import "../src/frontend/common/assets/main.css";

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i
      }
    }
  }
};

export default preview;
