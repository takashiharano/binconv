chcp 65001
cd /d %~dp0
cd ..\target
java -jar binconv.jar -tourl "ABC あいう"
pause
