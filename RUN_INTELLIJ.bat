@echo off
:: ============================================================
:: RUN_INTELLIJ.bat - Run Ludo Elite via IntelliJ Maven
:: ============================================================

echo ====================================
echo  LUDO ELITE - Starting Application
echo ====================================
echo.

:: Check if Maven wrapper exists
if exist "mvnw.cmd" (
    echo Using Maven Wrapper...
    call mvnw.cmd clean javafx:run
    goto END
)

:: Check if Maven in IntelliJ bundled location exists
set INTELLIJ_MAVEN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.1\plugins\maven\lib\maven3\bin\mvn.cmd
if exist "%INTELLIJ_MAVEN%" (
    echo Using IntelliJ Bundled Maven...
    call "%INTELLIJ_MAVEN%" clean javafx:run
    goto END
)

:: Try common IntelliJ Maven locations
set INTELLIJ_MAVEN=C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven3\bin\mvn.cmd
if exist "%INTELLIJ_MAVEN%" (
    echo Using IntelliJ Maven...
    call "%INTELLIJ_MAVEN%" clean javafx:run
    goto END
)

set INTELLIJ_MAVEN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2023\plugins\maven\lib\maven3\bin\mvn.cmd
if exist "%INTELLIJ_MAVEN%" (
    echo Using IntelliJ Maven...
    call "%INTELLIJ_MAVEN%" clean javafx:run
    goto END
)

:: If no Maven found, try direct Java execution
echo.
echo Maven not found! Trying direct Java execution...
echo.

:: Set classpath
set CP=target\classes
for %%f in (lib\*.jar) do call set CP=%%CP%%;%%f

:: Run with Java
java -cp "%CP%" --module-path "lib" --add-modules javafx.controls,javafx.fxml com.ludoelite.LudoEliteApp

:END
echo.
echo ====================================
echo  Press any key to exit...
echo ====================================
pause >nul
