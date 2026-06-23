@echo off
REM Script de detecção de ambiente para minecraft-plugin-deploy
REM Usage: detect-env.cmd

echo === Detectando ambiente ===

REM Git user
echo [Git] User: 
git config user.name 2>nul || echo NAO_ENCONTRADO

REM Java
echo [Java] Versao:
java -version 2>&1 | findstr /i version

REM Maven
echo [Maven] Home:
where mvn 2>nul && mvn --version 2>&1 | findstr /i "Maven home"

REM Server detection
echo [Server] Procurando spigot.yml/bukkit.yml:
if exist "C:\Users\HENDI\Desktop\lobby\spigot.yml" echo   lobby: C:\Users\HENDI\Desktop\lobby
if exist "C:\Users\HENDI\Desktop\Skyblock_Setup_Free\spigot.yml" echo   Skyblock_Setup_Free: C:\Users\HENDI\Desktop\Skyblock_Setup_Free
if exist "C:\Users\HENDI\Desktop\teste\spigot.yml" echo   teste: C:\Users\HENDI\Desktop\teste

echo === Fim ===
