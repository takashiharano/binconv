cd /d %~dp0
cd ..\target 
java -jar binconv.jar -fromhex "41 42 43"
pause
