@echo off
:: ============================================================
:: compile.bat  –  Compile Ludo Elite without Maven
:: ============================================================
setlocal EnableDelayedExpansion

:: ── 1. Configure paths ────────────────────────────────────
:: Set JAVAFX_PATH to your JavaFX SDK lib folder.
:: Example: C:\javafx-sdk-21\lib
:: If you have it on PATH or installed via JDK bundled version, leave blank.

set JAVAFX_PATH=C:\javafx-sdk-21\lib

:: ── 2. Derived paths ──────────────────────────────────────
set SRC_DIR=%~dp0src\main\java
set RES_DIR=%~dp0src\main\resources
set OUT_DIR=%~dp0out\classes
set LIB_DIR=%~dp0lib

:: ── 3. Create output directories ─────────────────────────
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

:: ── 4. Build classpath from lib\ folder ──────────────────
set CP=
for %%f in ("%LIB_DIR%\*.jar") do (
    set CP=!CP!;%%f
)

:: ── 5. Collect all .java source files ────────────────────
set SOURCES_FILE=%~dp0out\sources.txt
if exist "%SOURCES_FILE%" del "%SOURCES_FILE%"

for /r "%SRC_DIR%" %%f in (*.java) do (
    echo %%f >> "%SOURCES_FILE%"
)

:: ── 6. Compile ────────────────────────────────────────────
echo [COMPILE] Compiling sources...

if exist "%JAVAFX_PATH%" (
    echo [INFO] Using JavaFX module path: %JAVAFX_PATH%
    javac --module-path "%JAVAFX_PATH%" ^
          --add-modules javafx.controls,javafx.fxml ^
          -cp "!CP!" ^
          -d "%OUT_DIR%" ^
          @"%SOURCES_FILE%"
) else (
    echo [WARN] JAVAFX_PATH not found (%JAVAFX_PATH%). Trying without module-path...
    javac -cp "!CP!" ^
          -d "%OUT_DIR%" ^
          @"%SOURCES_FILE%"
)

if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed. Check errors above.
    pause
    exit /b 1
)

:: ── 7. Copy resources ─────────────────────────────────────
echo [COPY]    Copying resources...
xcopy /E /I /Y "%RES_DIR%\*" "%OUT_DIR%" >nul

echo.
echo [OK] Compilation successful! Run run.bat to launch.
pause
