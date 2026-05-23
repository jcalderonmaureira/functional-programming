# Inicializar repositorio git para functional-programming
# Ejecutar desde la carpeta functional-programming
$ErrorActionPreference = "Stop"
$repoPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $repoPath

Write-Host "Inicializando repositorio git en: $repoPath" -ForegroundColor Cyan

git init
git config user.name "Juan Felipe"
git config user.email "jfcalder@gmail.com"

# .gitignore general
@"
# IntelliJ IDEA
*.iml
.idea/workspace.xml
.idea/shelf/
out/

# Python
__pycache__/
*.pyc
*.pyo
.pytest_cache/

# Java
*.class
target/
"@ | Out-File -FilePath ".gitignore" -Encoding utf8

git add .
git commit -m "feat: ejemplos de programacion funcional Python y Java 26

- python/: 7 modulos en Python con funciones puras, inmutabilidad,
  map/filter/reduce, currying, composicion, recursion y monadas
- java/: proyecto IntelliJ IDEA con los mismos 7 modulos en Java 26
  usando records, sealed classes, Stream API y pattern matching"

Write-Host ""
Write-Host "Repositorio inicializado." -ForegroundColor Green
git log --oneline
