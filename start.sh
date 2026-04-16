#!/bin/bash

# Vérifier si Maven est installé
if ! command -v mvn &> /dev/null
then
    echo "Maven n'est pas installé. Utilisation de Maven Wrapper..."
    ./mvnw clean install
    ./mvnw spring-boot:run
else
    echo "Lancement de l'application Spring Boot..."
    mvn clean install
    mvn spring-boot:run
fi