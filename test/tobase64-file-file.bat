cd /d %~dp0
java -jar ..\target\binconv.jar -tobase64 -i c:\test\img.jpg -o c:\tmp\b64.txt
pause
