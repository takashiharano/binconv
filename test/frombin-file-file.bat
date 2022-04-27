cd /d %~dp0
cd ..\target 
java -jar binconv.jar -frombin -i c:\test\bin.txt -o c:\tmp\bin.jpg
pause
