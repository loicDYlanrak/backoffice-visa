@echo off

where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven n'est pas installe. Utilisation de Maven Wrapper...
    call mvnw.cmd clean install
    call mvnw.cmd spring-boot:run
) else (
    echo Lancement de l'application Spring Boot...
    call mvn clean install
    call mvn spring-boot:run
)