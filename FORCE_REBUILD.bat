@echo off
echo ========================================
echo  FORCE REBUILD - FIX CORNER SQUARES
echo ========================================
echo.
echo This script will:
echo 1. Delete old bytecode (target folder)
echo 2. Force Maven to rebuild everything
echo 3. Verify the build success
echo.
pause

echo.
echo [1/3] Deleting old bytecode...
if exist target (
    rmdir /s /q target
    echo     Done! Target folder deleted.
) else (
    echo     Target folder not found (already clean).
)
echo.

echo [2/3] Running Maven clean install...
echo.
call mvn clean install
echo.

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [3/3] Build SUCCESSFUL!
    echo.
    echo ========================================
    echo  REBUILD COMPLETE!
    echo ========================================
    echo.
    echo Next steps:
    echo 1. Run the application from IntelliJ
    echo 2. Check console for version "3.7.0-POSITION-13-FIX"
    echo 3. Test corner squares - they WILL work now!
    echo.
) else (
    echo.
    echo [3/3] Build FAILED!
    echo.
    echo ========================================
    echo  ERROR: Build failed
    echo ========================================
    echo.
    echo Please check the error messages above.
    echo You may need to:
    echo - Ensure Maven is installed
    echo - Check pom.xml for errors
    echo - Use IntelliJ Build menu instead
    echo.
)

pause
