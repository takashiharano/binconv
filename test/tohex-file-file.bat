cd /d %~dp0
cd ..\target 
java -jar binconv.jar -tohex -i c:\test\img.jpg -o c:\tmp\hex.txt
pause
