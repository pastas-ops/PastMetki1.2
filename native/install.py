# Устанавливает нативные виджеты в сгенерированный Android-проект.
import glob, os, shutil

A = 'android/app/src/main'
J = A + '/java/com/pastmetki/app'
os.makedirs(J, exist_ok=True)
for f in glob.glob('native/java/*.java'):
    shutil.copy(f, J)
shutil.copytree('native/res', A + '/res', dirs_exist_ok=True)
m = A + '/AndroidManifest.xml'
s = open(m, encoding='utf8').read()
if 'ChatsWidget' not in s:
    add = open('native/manifest_add.xml', encoding='utf8').read()
    s = s.replace('</application>', add + '\n    </application>')
    open(m, 'w', encoding='utf8').write(s)
print('widgets installed')

# Route quick-action widget deep links into the existing Capacitor app.
m = A + '/AndroidManifest.xml'
s = open(m, encoding='utf8').read()
if 'android:scheme="pastmetki"' not in s:
    marker = 'android:name=".MainActivity"'
    pos = s.find(marker)
    if pos >= 0:
        end = s.find('>', pos)
        intent = """
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="pastmetki" />
            </intent-filter>
        """
        s = s[:end+1] + intent + s[end+1:]
        open(m, 'w', encoding='utf8').write(s)
