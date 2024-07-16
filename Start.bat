@echo off
set /p arg1=Masukkan Date "YYYYMMMDD"


java -Xms512m -Xmx6048m -jar PreRun.jar "%arg1%"  
pause

