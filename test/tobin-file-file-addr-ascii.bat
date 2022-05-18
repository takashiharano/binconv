cd /d %~dp0
cd ..\target 
java -jar binconv.jar -tobin -i c:\test\img.jpg -o c:\tmp\bin.txt -addr -ascii
pause
