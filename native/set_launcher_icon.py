#!/usr/bin/env python3
"""Install pre-rendered PastMetki launcher icons into the generated Capacitor Android app."""
from pathlib import Path
import shutil
import re

root = Path(__file__).resolve().parent.parent
src = root / 'native' / 'launcher'
res = root / 'android' / 'app' / 'src' / 'main' / 'res'
if not res.is_dir():
    raise SystemExit('Android res directory is missing; run native/prep.sh first')

for folder in src.iterdir():
    if folder.is_dir():
        target = res / folder.name
        target.mkdir(parents=True, exist_ok=True)
        for item in folder.iterdir():
            if item.is_file():
                shutil.copy2(item, target / item.name)

manifest = root / 'android' / 'app' / 'src' / 'main' / 'AndroidManifest.xml'
text = manifest.read_text(encoding='utf-8')
app_match = re.search(r'<application\b[^>]*>', text, flags=re.S)
if not app_match:
    raise SystemExit('Could not find <application> in AndroidManifest.xml')
app = app_match.group(0)
for attr, value in [('android:icon', '@mipmap/ic_launcher'), ('android:roundIcon', '@mipmap/ic_launcher_round')]:
    pattern = rf'\s+{re.escape(attr)}="[^"]*"'
    if re.search(pattern, app):
        app = re.sub(pattern, f' {attr}="{value}"', app)
    else:
        if app.endswith('/>'):
            app = app[:-2] + f' {attr}="{value}"/>'
        else:
            app = app[:-1] + f' {attr}="{value}">'
text = text[:app_match.start()] + app + text[app_match.end():]
manifest.write_text(text, encoding='utf-8')
print('PastMetki launcher icons installed (legacy + adaptive; icon and roundIcon set).')
