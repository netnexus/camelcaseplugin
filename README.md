# Camel Case Plugin
CamelCasePlugin for IntelliJ IDEs (e.g. PhpStorm, IntelliJ IDEA, ...)

Switch easily between kebab-case, SNAKE_CASE, PascalCase, camelCase, snake_case or space case. See Edit menu or use ⇧ + ⌥ + U / Shift + Alt + U.

Allows to disable some conversions or change their order in the preferences.

Please see this example for a demonstration:

![Demonstration](https://github.com/user-attachments/assets/72001e9b-402d-4971-8a82-3375c70d858d)

## Install

Install from the JetBrains Marketplace: open **Preferences/Settings → Plugins → Marketplace** and search for `camelcase`.

To install a locally built version:

1. Build the plugin with `make build`.
2. In your IDE, open **Preferences/Settings → Plugins → ⚙ → Install Plugin from Disk…**.
3. Select the ZIP file created under `build/distributions/`.
4. Restart the IDE when prompted.

## Build
```
make build
```
