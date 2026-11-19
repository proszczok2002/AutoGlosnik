[CmdletBinding(SupportsShouldProcess)]
param()

$ErrorActionPreference = 'Stop'
$project = (Get-Item -LiteralPath (Join-Path $PSScriptRoot '..')).FullName
$versionPattern = '[0-9]+(?:\.[0-9]+){1,3}'
$manifest = Get-Content -LiteralPath (Join-Path $project 'update.json') -Raw -Encoding UTF8 | ConvertFrom-Json
if ($manifest.versionName -notmatch "^${versionPattern}$" -or $manifest.sha256 -notmatch '^[a-fA-F0-9]{64}$') {
    throw 'Invalid version or APK checksum in update.json.'
}
$currentVersion = [version]$manifest.versionName
$currentApk = Get-Item -LiteralPath (Join-Path $project "AutoGlosnik-v$($manifest.versionName).apk")
if ($currentApk.PSIsContainer -or $currentApk.Attributes.HasFlag([IO.FileAttributes]::ReparsePoint)) {
    throw 'Expected a regular local APK.'
}
$stream = [IO.File]::OpenRead($currentApk.FullName)
try { $apkHash = (Get-FileHash -InputStream $stream -Algorithm SHA256).Hash }
finally { $stream.Dispose() }
if ($apkHash -ne $manifest.sha256) {
    throw 'The current local APK does not match update.json.'
}

$archive = Join-Path $project 'niepotrzebne'
$archiveParent = Get-Item -LiteralPath $archive -ErrorAction SilentlyContinue
if ($archiveParent -and (-not $archiveParent.PSIsContainer -or
        $archiveParent.Attributes.HasFlag([IO.FileAttributes]::ReparsePoint))) {
    throw "Expected a regular local archive folder: $archive"
}
$batch = Join-Path $archive ("{0}-wydanie-{1}" -f [DateTime]::UtcNow.ToString('yyyyMMddTHHmmssfffZ'), $manifest.versionName)

$locations = @(
    @{ Folder = '.'; Pattern = "^AutoGlosnik-v(?<version>$versionPattern)(?:\.apk|-source\.zip)$"; IncludeCurrent = $false; Directory = $false }
    @{ Folder = '.tools'; Pattern = "^AutoGlosnik-v(?<version>$versionPattern)(?:\.apk|-source\.zip)$"; IncludeCurrent = $false; Directory = $false }
    @{ Folder = '.tools'; Pattern = "^verify-AutoGlosnik-v(?<version>$versionPattern)(?:\.apk|-source\.zip)$"; IncludeCurrent = $true; Directory = $false }
    @{ Folder = 'build/release-work'; Pattern = "^v(?<version>$versionPattern)$"; IncludeCurrent = $true; Directory = $true }
)

$candidates = foreach ($location in $locations) {
    $folder = $project
    $available = $true
    foreach ($part in ($location.Folder -split '/')) {
        if ($part -eq '.') { continue }
        $folder = Join-Path $folder $part
        $item = Get-Item -LiteralPath $folder -ErrorAction SilentlyContinue
        if (-not $item) { $available = $false; break }
        if (-not $item.PSIsContainer -or $item.Attributes.HasFlag([IO.FileAttributes]::ReparsePoint)) {
            throw "Expected a regular local folder: $folder"
        }
    }
    if (-not $available) { continue }

    foreach ($item in (Get-ChildItem -LiteralPath $folder -Force)) {
        if ($item.PSIsContainer -ne $location.Directory -or $item.Name -notmatch $location.Pattern) { continue }
        $version = [version]$Matches.version
        if ($version -gt $currentVersion -or ($version -eq $currentVersion -and -not $location.IncludeCurrent)) { continue }
        if ($item.Attributes.HasFlag([IO.FileAttributes]::ReparsePoint) -or
            ($item.PSIsContainer -and (Get-ChildItem -LiteralPath $item.FullName -Recurse -Force -Attributes ReparsePoint))) {
            throw "Refusing to move a symlink or junction: $($item.FullName)"
        }
        $item
    }
}

if (-not $candidates) {
    Write-Output 'No release artifacts to archive.'
    return
}
if (Test-Path -LiteralPath $batch) { throw "Archive already exists: $batch" }

foreach ($item in $candidates) {
    $relative = $item.FullName.Substring($project.Length + 1)
    $destination = Join-Path $batch $relative
    if ($PSCmdlet.ShouldProcess($item.FullName, "Move to $destination")) {
        $null = [IO.Directory]::CreateDirectory((Split-Path -Parent $destination))
        if (Test-Path -LiteralPath $destination) { throw "Destination already exists: $destination" }
        Move-Item -LiteralPath $item.FullName -Destination $destination
        Write-Output "Archived: $relative"
    }
}
