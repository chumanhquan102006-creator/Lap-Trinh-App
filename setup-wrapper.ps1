# Run ONCE on Windows before opening the project in Android Studio.
# This fetches the official Gradle Wrapper binary directly from Gradle's source repository.
$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$target = Join-Path $root 'gradle\wrapper\gradle-wrapper.jar'
if (Test-Path $target) {
    Write-Host 'Gradle wrapper is already installed.'
    exit 0
}
$url = 'https://raw.githubusercontent.com/gradle/gradle/v8.11.1/gradle/wrapper/gradle-wrapper.jar'
Write-Host "Downloading official Gradle 8.11.1 wrapper..."
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
Invoke-WebRequest -Uri $url -OutFile $target -UseBasicParsing
if ((Get-Item $target).Length -lt 20000) {
    Remove-Item $target -Force
    throw 'Gradle wrapper download failed or returned unexpected content.'
}
Write-Host 'Done. Open AdMobAssignmentDemo in Android Studio.'
