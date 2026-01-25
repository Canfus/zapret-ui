import js from "@eslint/js";
import tseslint from "@typescript-eslint/eslint-plugin";
import tsParser from "@typescript-eslint/parser";
import globals from "globals";

export default [
  {
    files: ["**/*.{js,mjs,cjs}"],
    languageOptions: {
      globals: globals.node,
      sourceType: "commonjs"
    },
    rules: {
      ...js.configs.recommended.rules
    }
  },

  {
    files: ["**/*.{ts}"],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        project: "./tsconfig.json",
        tsconfigRootDir: import.meta.dirname
      },
      globals: globals.node,
      sourceType: "commonjs"
    },
    plugins: {
      "@typescript-eslint": tseslint
    },
    rules: {
      ...js.configs.recommended.rules,
      ...tseslint.configs.recommended.rules,

      "@typescript-eslint/no-unused-vars": "error",
      "@typescript-eslint/no-explicit-any": ["warn", { ignoreRestArgs: true }],
      "@typescript-eslint/consistent-type-imports": "warn",
      "@typescript-eslint/prefer-nullish-coalescing": "warn",

      "no-console": "warn",
      "node/no-missing-import": "off",
      "node/no-unsupported-features/es-syntax": "off"
    },
    settings: {
      "import/resolver": {
        node: { extensions: [".ts", ".js"] }
      }
    }
  },

  {
    ignores: ["dist/**", "node_modules/**", "*.config.{js,mjs,cjs}"]
  }
];
