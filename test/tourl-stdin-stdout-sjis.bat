cd /d %~dp0
cd ..\target 
java -jar binconv.jar -tourl "ABC ‚ ‚¢‚¤" -enc SJIS
pause
