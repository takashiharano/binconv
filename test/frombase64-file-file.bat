cd /d %~dp0
java -jar ..\target\binconv.jar -frombase64 -i c:\test\b64.txt -o c:\tmp\b64.jpg
pause
