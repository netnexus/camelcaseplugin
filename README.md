# Camel Case Plugin
CamelCasePlugin for IntelliJ IDEs (e.g. PhpStorm, IntelliJ IDEA, ...)

Switch easily between kebab-case, SNAKE_CASE, PascalCase, camelCase, snake_case or space case. See Edit menu or use ⇧ + ⌥ + U / Shift + Alt + U.

Allows to disable some conversions or change their order in the preferences.

Please see this example for a demonstration:

![Demonstration](https://github.com/user-attachments/assets/72001e9b-402d-4971-8a82-3375c70d858d)

## Install
Use your IDE. Preferences/Plugins/Browse repositories and search for "camelcase".

## Build
Just clone this repo and open the project it in IntelliJ IDEA.

### Local WordUtils hotfix (IDEA 2026.2)

The conversion code now capitalizes words without Apache Commons Lang, fixing
[upstream #36](https://github.com/netnexus/camelcaseplugin/issues/36).

On macOS, build and test with the installed IDEA JDK and original CamelCase 3.0.12.
Download the original Marketplace artifact once (or pass its path with `--base-jar`):

```sh
mkdir -p build/base
curl -fL -o build/base/CamelCasePlugin-3.0.12.jar https://plugins.jetbrains.com/files/7160/153616/CamelCasePlugin.jar
python3 scripts/build-local.py
```

Optional arguments: `--ide /path/to/IDE.app/Contents` and `--base-jar /path/to/CamelCasePlugin.jar`.
Keep a copy of the original 3.0.12 JAR for rebuilding and rollback.

This is a hotfix package, not a full source build: the script replaces only
`Conversion.class` and updates the version/platform metadata in the original JAR.
For packaging it uses `scripts/compat-3.0.12/.../Conversion.java`, taken from
upstream commit `dfb9512` with only the WordUtils replacement. The current upstream
source uses a different method signature and cannot replace the released class.
It preserves the original compiled settings UI because the checked-in generated
UI source is stale. Tests run without Commons Lang, and all other JAR entries
are checked for identical content. A separate test is compiled against the original
JAR and executed against the finished package to verify binary compatibility.
The installed plugin is not modified.

Output: `build/distributions/CamelCasePlugin-3.0.12.2-local.jar` (IDE build 262+).
Do not install `3.0.12.1-local`: that earlier package has an incompatible method signature.
Install via **Settings → Plugins → ⚙ → Install Plugin from Disk…**, then restart.
The plugin ID is unchanged, so this replaces CamelCase rather than adding a second action.
