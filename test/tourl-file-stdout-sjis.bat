cd /d %~dp0
cd ..\target 
java -jar binconv.jar -tourl -i C:\test\url-sjis-dec.txt -enc SJIS
pause
