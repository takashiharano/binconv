cd /d %~dp0
cd ..\target
java -jar binconv.jar -frombase64 -i c:\test\b64.txt
pause
