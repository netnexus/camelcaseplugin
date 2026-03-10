<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Camel Case Changelog

## [3.0.13] - 2026-03-06

- Fixed bug with multiple careats

## [3.0.12]
- Bugfix: use standard config if com.intellij.openapi.project.Project is null

## [3.0.11]
- Bugfix: if only camelCase and snake_case is selected in the options

## [3.0.10]
- Single word bugfix (Foo => FOO => foo)
- Pascal Case with space bugfix (foo foo => Foo Foo)

## [3.0.9]
- New Conversion added (Camel Case)

## [3.0.7]
- Support any text field and editor

## [3.0.6]
- Fixed snake case bug
- Need IntelliJ Platform version higher than 191.4212.41

## [3.0.5]
- Fix for string in lower case

## [3.0.4]
- Select whole string with dashes

## [3.0.3]
- Fixed conversion with special char

## [3.0.2]
- Fixed deprecated function

## [3.0.1]
- Restore multiple caret mode (see #2)

## [3.0.0]
- Allow to use plugin in dialogs (like refactor/rename)

## [2.1.0]
- Introduce "space case" and allow to change conversion order in configuration.

## [2.0.0]
- Introduce config panel to switch off certain transformations (Preferences / Editor / Camel Case)

## [1.6.0]
- Bugfix: double underscore (thanks to John)

## [1.5.0]
- added hyphen-separated-notation

## [1.4.0]
- optimized multiple caret support
- idea 14.1 is now required, please use release 1.2 for older IDEs

## [1.3.0]
- multiple selections support

## [1.2.0]
- Compiled for J2SE 6.0

## [1.1.0]
- Added undo functionality
