cd /D "%~dp0"
SET CLASS_PATH=libs/*
"%WBHJAVA17HOME%" -cp %CLASS_PATH% -Dlog4j2.configurationFile=file:log4j2.xml org.aulich.wbh.vertiefung_3.programs.RunSequential