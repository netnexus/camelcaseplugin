#!/usr/bin/env bash

set -euo pipefail

gradle_command="$1"
shift

wrapper_properties="gradle/wrapper/gradle-wrapper.properties"

if [[ -z "${GRADLE_DISTRIBUTION_URL:-}" ]]; then
  exec "$gradle_command" "$@"
fi

backup_file="$(mktemp)"
temporary_file="$(mktemp)"
cp "$wrapper_properties" "$backup_file"

cleanup() {
  cp "$backup_file" "$wrapper_properties"
  rm -f "$backup_file" "$temporary_file"
}
trap cleanup EXIT

awk -v distribution_url="$GRADLE_DISTRIBUTION_URL" '
  /^distributionUrl=/ { print "distributionUrl=" distribution_url; next }
  { print }
' "$wrapper_properties" > "$temporary_file"
mv "$temporary_file" "$wrapper_properties"

"$gradle_command" "$@"
