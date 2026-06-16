@echo off
:: ============================================================
:: run.bat  –  Launch Ludo Elite without Maven
:: ============================================================
setlocal EnableDelayedExpansion

:: ── Configure JavaFX path (same as compile.bat) ───────────
set JAVAFX_PATH=C:\javafx-sdk-21\lib

:: ── Derived paths ─────────────────────────────────────────
set OUT_DIR=%~dp0out\classes
set LIB_DIR=%~dp0lib
set MAIN_CLASS=com.ludoelite.LudoEliteApp

:: ── Build classpath ───────────────────────────────────────
set CP=%OUT_DIR%
for %%f in ("%LIB_DIR%\*.jar") do (
    set CP=!CP!;%%f
)

:: ── Launch ────────────────────────────────────────────────
echo [RUN] Starting Ludo Elite...

if exist "%JAVAFX_PATH%" (
    java --module-path "%JAVAFX_PATH%" ^
         --add-modules javafx.controls,javafx.fxml ^
         --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^
         --add-opens javafx.fxml/javafx.fxml=ALL-UNNAMED ^
         -cp "!CP!" ^
         %MAIN_CLASS%
) else (
    echo [WARN] JAVAFX_PATH not found. Trying without module-path...
    java -cp "!CP!" %MAIN_CLASS%
)

if errorlevel 1 (
    echo.
    echo [ERROR] Application crashed. See output above.
    pause
)
