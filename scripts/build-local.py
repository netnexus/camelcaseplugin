#!/usr/bin/env python3
"""Build a local hotfix using the binary-compatible 3.0.12 conversion source."""
import argparse
from copy import copy
from pathlib import Path
import re
import subprocess
import tempfile
import xml.etree.ElementTree as ET
from zipfile import ZipFile

root = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--ide', type=Path, default=Path('/Applications/IntelliJ IDEA.app/Contents'))
parser.add_argument('--base-jar', type=Path, default=root / 'build/base/CamelCasePlugin-3.0.12.jar')
args = parser.parse_args()
jdk = args.ide / 'jbr/Contents/Home/bin'
annotations = args.ide / 'lib/annotations.jar'
class_path = 'de/netnexus/CamelCasePlugin/Conversion.class'
descriptor_path = 'META-INF/plugin.xml'
output = root / 'build/distributions/CamelCasePlugin-3.0.12.2-local.jar'
if args.base_jar.resolve() == output.resolve():
    parser.error('The output must not overwrite the base JAR.')

with ZipFile(args.base_jar) as original, tempfile.TemporaryDirectory() as temp:
    descriptor = original.read(descriptor_path).decode('utf-8')
    metadata = ET.fromstring(descriptor)
    if metadata.findtext('id') != 'de.netnexus.camelcaseplugin' or metadata.findtext('version') != '3.0.12':
        parser.error('Expected the original CamelCase 3.0.12 JAR.')
    if any(n.upper().endswith(('.SF', '.RSA', '.DSA', '.EC')) for n in original.namelist()):
        parser.error('Signed base JARs are not supported.')
    subprocess.run([str(jdk / 'javac'), '--release', '17', '-encoding', 'UTF-8',
                    '-cp', str(annotations), '-d', temp,
                    str(root / 'src/de/netnexus/CamelCasePlugin/Conversion.java'),
                    str(root / 'tests/de/netnexus/CamelCasePlugin/ConversionTest.java')], check=True)
    # Run without the IDE or Commons Lang on the runtime classpath.
    subprocess.run([str(jdk / 'java'), '-cp', temp,
                    'de.netnexus.CamelCasePlugin.ConversionTest'], check=True)
    # Compile the caller against the ORIGINAL binary, not our replacement source.
    # Running this caller against the output catches NoSuchMethodError regressions.
    probe = Path(temp) / 'probe'
    subprocess.run([str(jdk / 'javac'), '--release', '11', '-encoding', 'UTF-8',
                    '-cp', str(args.base_jar), '-d', str(probe),
                    str(root / 'tests/de/netnexus/CamelCasePlugin/LegacyBinaryTest.java')], check=True)
    legacy = Path(temp) / 'legacy'
    subprocess.run([str(jdk / 'javac'), '--release', '11', '-encoding', 'UTF-8',
                    '-cp', str(annotations), '-d', str(legacy),
                    str(root / 'scripts/compat-3.0.12/de/netnexus/CamelCasePlugin/Conversion.java')], check=True)
    replacement = (legacy / class_path).read_bytes()
    assert b'org/apache/commons/lang/WordUtils' not in replacement
    descriptor = descriptor.replace('<version>3.0.12</version>', '<version>3.0.12.2-local</version>')
    # This local artifact targets the affected 2026.2 platform, not older IDEs.
    descriptor = re.sub(r'<idea-version\b[^>]*/>', '<idea-version since-build="262"/>', descriptor)
    output.parent.mkdir(parents=True, exist_ok=True)
    with ZipFile(output, 'w') as patched:
        for entry in original.infolist():
            data = original.read(entry.filename)
            if entry.filename == class_path:
                data = replacement
            elif entry.filename == descriptor_path:
                data = descriptor.encode('utf-8')
            patched.writestr(copy(entry), data)
    with ZipFile(output) as patched:
        assert patched.testzip() is None
        assert patched.namelist() == original.namelist()
        for name in original.namelist():
            if name not in (class_path, descriptor_path):
                assert patched.read(name) == original.read(name), name
    subprocess.run([str(jdk / 'java'), '-cp', str(probe) + ':' + str(output),
                    'de.netnexus.CamelCasePlugin.LegacyBinaryTest'], check=True)
print(output)
