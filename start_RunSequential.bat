cd /D "%~dp0"
SET CLASS_PATH=libs/*
"C:\Program Files\JetBrains\IntelliJ IDEA 2023.2.3\jbr\bin\java" -cp %CLASS_PATH% -Dlog4j2.configurationFile=file:log4j2.xml org.aulich.wbh.vertiefung_3.programs.RunSequential