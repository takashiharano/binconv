cd /d %~dp0
cd ..\target 
java -jar binconv.jar -fromurl -i C:\test\url-utf8.txt -o c:\tmp\url-utf8-dec.txt
pause
