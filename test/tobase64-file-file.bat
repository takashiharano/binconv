cd /d %~dp0
cd ..\target
java -jar binconv.jar -tobase64 -i c:\test\img.jpg -o c:\tmp\b64.txt
pause
