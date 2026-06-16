# ============================================================
# download-deps.ps1
# Downloads all required JAR files into the lib/ folder.
# Run once before compile.bat:  powershell -ExecutionPolicy Bypass -File download-deps.ps1
# ============================================================

$libDir = "$PSScriptRoot\lib"
New-Item -ItemType Directory -Force -Path $libDir | Out-Null

$jars = @(
    # Jackson core
    @{ name = "jackson-databind-2.17.1.jar";
       url  = "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar" },
    @{ name = "jackson-core-2.17.1.jar";
       url  = "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar" },
    @{ name = "jackson-annotations-2.17.1.jar";
       url  = "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar" },
    @{ name = "jackson-datatype-jsr310-2.17.1.jar";
       url  = "https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.17.1/jackson-datatype-jsr310-2.17.1.jar" }
)

Write-Host "Downloading dependencies to .\lib\ ..." -ForegroundColor Cyan

foreach ($jar in $jars) {
    $dest = Join-Path $libDir $jar.name
    if (Test-Path $dest) {
        Write-Host "  [SKIP] $($jar.name) already exists"
    } else {
        Write-Host "  [DL]   $($jar.name)" -NoNewline
        try {
            Invoke-WebRequest -Uri $jar.url -OutFile $dest -UseBasicParsing
            Write-Host "  OK" -ForegroundColor Green
        } catch {
            Write-Host "  FAILED: $_" -ForegroundColor Red
        }
    }
}

Write-Host ""
Write-Host "Done. Now run: compile.bat" -ForegroundColor Yellow
