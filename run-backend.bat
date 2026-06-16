@echo off
echo ========================================
echo Starting Ludo Elite Backend Server
echo ========================================
echo.

cd /d "%~dp0"

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo Compiling and running backend...
echo.

REM Compile with javac if you don't have maven
REM For now, try to run the compiled classes if they exist
if exist "out\classes" (
    java -cp "out\classes;lib\*" com.ludoelite.backend.LudoBackendApplication
) else (
    echo ERROR: Classes not compiled yet
    echo Please compile first using compile.bat
    pause
    exit /b 1
)

pause
