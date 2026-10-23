$ErrorActionPreference = "Stop"

$projectPath = $PSScriptRoot
$javaHome = "C:\Dev\jdk8u504-b01"
$tomcatHome = "C:\Dev\apache-tomcat-9.0.122"
$demoPath = Join-Path $projectPath "demo-app"
$warPath = Join-Path $demoPath "target\eddy-demo.war"
$deployPath = Join-Path $tomcatHome "webapps\eddy-demo.war"

if (-not (Test-Path -LiteralPath (Join-Path $javaHome "bin\java.exe"))) {
    throw "JDK introuvable dans $javaHome"
}

if (-not (Test-Path -LiteralPath (Join-Path $tomcatHome "bin\catalina.bat"))) {
    throw "Tomcat introuvable dans $tomcatHome"
}

$env:JAVA_HOME = $javaHome
$env:Path = (Join-Path $javaHome "bin") + ";" + $env:Path

Push-Location $projectPath
try {
    mvn.cmd clean install
    if ($LASTEXITCODE -ne 0) {
        throw "La compilation du framework a échoué."
    }

    Push-Location $demoPath
    try {
        mvn.cmd clean package
        if ($LASTEXITCODE -ne 0) {
            throw "La compilation de l'application de démonstration a échoué."
        }
    }
    finally {
        Pop-Location
    }

    Copy-Item -LiteralPath $warPath -Destination $deployPath -Force
    Write-Host "Déploiement terminé : $deployPath"
    Write-Host "Redémarre Tomcat puis ouvre http://localhost:8080/eddy-demo/"
}
finally {
    Pop-Location
}
