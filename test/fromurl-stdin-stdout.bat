cd /d %~dp0
cd ..\target 

@REM java -jar binconv.jar -fromurl abc+%E3%81%82%E3%81%84%E3%81%86
java -jar binconv.jar -fromurl abc+%%E3%%81%%82%%E3%%81%%84%%E3%%81%%86

pause
