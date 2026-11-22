# Projet JEE — Philosophes distribués

Ce dépôt contient le premier incrément du projet. Il pose le périmètre, l'architecture de départ et les fonctionnalités à développer à partir des notions vues en TP.

## Objectif du premier incrément

Mettre en place une base Spring Boot composée de deux microservices indépendants :
- `supervisor-service` : gestion centralisée et atomique des réservations de fourchettes ;
- `philosopher-service` : API d'un philosophe qui demande ses deux fourchettes au superviseur.

Le détail de la correspondance entre le cahier des charges, les TPs et le travail restant se trouve dans [docs/avancement-cahier-des-charges.md](docs/avancement-cahier-des-charges.md).

## Prérequis

- Java 17
- Maven 3.9+
- Git

## Lancer les services

Depuis la racine du dépôt, dans deux terminaux PowerShell :

```powershell
mvn -pl supervisor-service spring-boot:run
```

```powershell
mvn -pl philosopher-service spring-boot:run
```

Le superviseur écoute sur le port 8081 et le philosophe sur le port 8080.

## Vérifier le fonctionnement

Demander les fourchettes 1 et 2 au nom du philosophe 1 :

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/philosophers/1/eat?firstFork=1&secondFork=2"
```

Un deuxième philosophe ne peut pas réserver une fourchette déjà attribuée :

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/philosophers/2/eat?firstFork=2&secondFork=3"
```

Consulter l'état des fourchettes :

```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/supervisor/forks"
```

Libérer les fourchettes du philosophe 1 :

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/philosophers/1/finish?firstFork=1&secondFork=2"
```

## Tests

À la racine :

```powershell
mvn clean test
```

## État du projet

Ce premier incrément couvre le socle de communication HTTP et la réservation centralisée des deux fourchettes. Le protocole distribué par jetons, Kafka, ZooKeeper, les tests de charge/concurrence avancés, Docker, Kubernetes et la CI/CD sont des étapes suivantes.