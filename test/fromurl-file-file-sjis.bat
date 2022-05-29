cd /d %~dp0
cd ..\target 
java -jar binconv.jar -fromurl -i C:\test\url-sjis.txt -enc SJIS -o c:\tmp\url-sjis-dec.txt
pause
