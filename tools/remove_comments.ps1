param(
    [string]$Target = 'finsure'
)

$BackupRoot = Join-Path -Path (Get-Location) -ChildPath 'comment_removal_backups'
if (-not (Test-Path $BackupRoot)) { New-Item -ItemType Directory -Path $BackupRoot | Out-Null }

$exts = @('.java','.kt','.groovy','.js','.ts','.c','.cpp','.h','.cs','.scala','.xml','.html','.md','.properties','.yml','.yaml')

Write-Host "Processing target:" $Target

$targetFull = (Get-Item $Target).FullName
Get-ChildItem -Path $Target -Recurse -File | Where-Object { ($_.FullName -notlike "*comment_removal_backups*") -and ($exts -contains $_.Extension.ToLower()) } | ForEach-Object {
    $file = $_.FullName
    try {
        $content = Get-Content -Path $file -Raw -Encoding UTF8 -ErrorAction Stop
    } catch {
        $content = Get-Content -Path $file -Raw -Encoding Default
    }
    $orig = $content

    # Remove XML/HTML/MD comments
    $content = [regex]::Replace($content, '(?s)<!--.*?-->', '')
    # Remove block comments /* ... */
    $content = [regex]::Replace($content, '(?s)/\*.*?\*/', '')
    # Remove full-line // comments
    $content = [regex]::Replace($content, '(?m)^[ \t]*//.*$', '')
    # Remove inline // comments but avoid URLs like http:// by requiring the // not be preceded by :
    $content = [regex]::Replace($content, '(?m)(?<!:)//.*$', '')
    # Remove lines that start with # or ! (properties/yaml)
    $content = [regex]::Replace($content, '(?m)^[ \t]*[#\!].*$', '')

    if ($content -ne $orig) {
        # backup
        $relPath = $file.Substring($targetFull.Length)
        if ($relPath.StartsWith('\') -or $relPath.StartsWith('/')) { $relPath = $relPath.Substring(1) }
        $backupPath = Join-Path -Path $BackupRoot -ChildPath $relPath
        $backupDir = Split-Path $backupPath -Parent
        if (-not (Test-Path $backupDir)) { New-Item -ItemType Directory -Path $backupDir | Out-Null }
        if ($backupPath -ne $file) {
            Copy-Item -Path $file -Destination $backupPath -Force
        } else {
            Write-Host "Skipping backup (same path): $file"
        }
        # write back
        Set-Content -Path $file -Value $content -Encoding utf8
        Write-Host "Modified:" $file
    }
}

Write-Host "Comment removal (PowerShell) finished. Backups in comment_removal_backups."
