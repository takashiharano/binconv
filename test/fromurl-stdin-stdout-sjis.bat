cd /d %~dp0
cd ..\target 

@REM java -jar binconv.jar -fromurl ABC+%82%A0%82%A2%82%A4 -enc SJIS
java -jar binconv.jar -fromurl ABC+%%82%%A0%%82%%A2%%82%%A4 -enc SJIS

pause
