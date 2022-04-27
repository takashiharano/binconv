cd /d %~dp0
cd ..\target 
java -jar binconv.jar -fromhex -i c:\test\hex.txt
pause
