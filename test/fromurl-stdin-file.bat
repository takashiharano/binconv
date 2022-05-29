cd /d %~dp0
cd ..\target

@REM java -jar binconv.jar -fromurl abc%20%E3%81%82%E3%81%84%E3%81%86 -o c:\tmp\url-utf8-dec.txt
java -jar binconv.jar -fromurl abc%%20%%E3%%81%%82%%E3%%81%%84%%E3%%81%%86 -o c:\tmp\url-utf8-dec.txt

pause
