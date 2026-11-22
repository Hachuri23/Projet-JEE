# Correspondance entre le cahier des charges, les TPs et le projet

Ce document sépare les notions déjà abordées dans les TPs des fonctionnalités qui restent à concevoir ou à développer. Une notion étudiée en TP n'est pas considérée comme une fonctionnalité du projet tant qu'elle n'est pas intégrée et testée dans ce dépôt.

## Section 1 — Philosophes gâtés

### Exigences du projet
- Déployer plusieurs microservices philosophes indépendants.
- Faire remonter leurs événements vers un moniteur.
- Utiliser une communication événementielle pour suivre l'activité des philosophes.

### Déjà étudié dans les TPs
- TP1 : bases de l'architecture microservices et de l'environnement Docker.
- TP3 : échange de messages avec Apache Kafka, notamment la publication et la consommation d'événements.

### À réaliser dans le projet
- Définir un événement commun : identifiant du philosophe, type d'événement, horodatage et informations utiles.
- Faire publier par chaque philosophe ses événements de cycle de vie.
- Créer un service moniteur consommant les événements Kafka.
- Afficher ou exposer l'état observé des philosophes.
- Tester plusieurs producteurs et vérifier la réception des événements.

### État dans ce premier push
Architecture et périmètre documentés. Le producteur Kafka, le consommateur et le moniteur restent à implémenter.

## Section 2 — Philosophes avec superviseur

### Exigences du projet
- Chaque philosophe interroge le superviseur lorsqu'il souhaite obtenir une fourchette.
- Le superviseur attribue une fourchette si elle est disponible et accepte sa libération.
- Commencer par une version simple et identifier ses limites à l'aide de tests.

### Déjà étudié dans les TPs
- TP2 : création de microservices Spring Boot indépendants et communication HTTP entre services.

### À réaliser dans le projet
- Exposer les opérations de réservation et de libération.
- Faire appeler le superviseur par les microservices philosophes.
- Comparer une attribution naïve, fourchette par fourchette, avec une réservation des deux fourchettes en une opération.
- Écrire des tests qui mettent en évidence les situations de blocage possibles dans la version naïve.

### État dans ce premier push
Le superviseur et le client philosophe communiquent en HTTP. Le superviseur réserve les deux fourchettes dans une opération atomique. La version naïve demandée pour l'étude comparative reste à ajouter.

## Section 3 — Section critique et exclusion mutuelle

### Exigences du projet
- Prendre en compte le traitement simultané de requêtes par plusieurs threads.
- Protéger l'état partagé des fourchettes.
- Vérifier que les deux fourchettes sont libres avant de les réserver ensemble.

### Déjà étudié dans les TPs
- TP2 : accès concurrents aux services et synchronisation ; les notions de section critique et de verrou sont à approfondir dans le projet.

### À réaliser dans le projet
- Délimiter précisément la section critique qui lit et modifie l'état des fourchettes.
- Garantir l'atomicité de la vérification et de la réservation des deux ressources.
- Tester des demandes concurrentes et vérifier qu'une fourchette n'est jamais attribuée à deux philosophes.
- Comparer les résultats avec et sans verrou.

### État dans ce premier push
La réservation centrale des deux fourchettes est protégée par une méthode synchronisée dans le superviseur. Des tests unitaires couvrent l'attribution, le refus d'une ressource occupée et la libération par son propriétaire. Les tests de concurrence multi-requêtes restent à renforcer.

## Section 3.3 — Protocole distribué par jetons

### Exigences du projet
- Chaque philosophe est un microservice Spring Boot et communique avec les autres par HTTP.
- Il n'existe aucun superviseur central pour attribuer les fourchettes.
- Chaque fourchette possède un jeton unique représentant le droit exclusif de l'utiliser.
- Diffuser et mémoriser les demandes horodatées, gérer les demandes simultanées, les retransmissions, l'équité, les interblocages et la famine.

### Déjà étudié dans les TPs
- TP2 : communication HTTP entre microservices.
- Les TPs fournissent les bases de communication ; ils ne réalisent pas le protocole distribué spécifique demandé ici.

### À réaliser dans le projet
- Définir les états locaux d'un philosophe et la structure d'une demande horodatée.
- Définir les règles de circulation et de transfert des jetons.
- Implémenter la diffusion, la réception et la mémorisation des demandes.
- Garantir l'unicité d'utilisation de chaque fourchette.
- Définir et tester une stratégie d'équité, de retransmission et de reprise après panne.
- Tracer les exécutions pour analyser interblocages et famine.

### État dans ce premier push
Non implémenté. Cette partie constituera une variante distincte de l'architecture avec superviseur.

## Section 4 — Tests

### Exigences du projet
- Tests unitaires avec JUnit 5 et Mockito.
- Tests d'intégration entre plusieurs philosophes.
- Tests de concurrence sur les demandes simultanées.
- Tests de résilience avec délais, indisponibilités et interruptions de communication.
- Analyse des propriétés vérifiées et des limites des tests.

### Déjà étudié dans les TPs
- TP5 : fallback, Retry, Circuit Breaker et TimeLimiter avec Resilience4j pour gérer les défaillances d'un service distant.
- JUnit et les tests Spring Boot constituent la base de validation des composants.

### À réaliser dans le projet
- Compléter les tests unitaires des règles métier et des états des philosophes.
- Démarrer plusieurs services dans des tests d'intégration.
- Simuler des accès concurrents et contrôler l'exclusion mutuelle.
- Simuler des pannes réseau, des réponses lentes et l'arrêt d'un philosophe.
- Documenter les résultats observés et les limites de la stratégie de test.

### État dans ce premier push
Des tests unitaires du superviseur sont présents. Les tests d'intégration, de concurrence multi-services et de résilience avec Resilience4j restent à ajouter.

## Section 6 — Approche DevOps

### Exigences du projet
- Conteneuriser les services avec Docker.
- Utiliser Kafka pour les événements.
- Préparer le déploiement Kubernetes.
- Mettre en place une chaîne CI/CD.

### Déjà étudié dans les TPs
- TP1 : premières manipulations de conteneurs Docker.
- TP3 : producteurs, consommateurs et échanges via Kafka.

### À réaliser dans le projet
- Écrire les Dockerfiles des microservices et un fichier Compose pour l'environnement local.
- Ajouter Kafka et la configuration des topics.
- Décrire les déploiements, services et configurations Kubernetes.
- Mettre en place une pipeline CI/CD qui compile, exécute les tests et construit les images.
- Ajouter la collecte des logs et des indicateurs utiles au suivi.

### État dans ce premier push
Les choix et étapes sont identifiés. Docker, Kafka dans l'application, Kubernetes et CI/CD ne sont pas encore intégrés.

## Ordre de réalisation proposé

1. Stabiliser et tester les services philosophe et superviseur.
2. Ajouter les événements Kafka et le moniteur.
3. Étendre les tests unitaires, d'intégration, de concurrence et de résilience.
4. Concevoir puis implémenter le protocole distribué par jetons.
5. Ajouter Docker Compose, Kubernetes et la CI/CD.

## Limites connues

Le premier incrément ne constitue pas une implémentation complète du cahier des charges. La réservation synchronisée protège l'état partagé à l'intérieur d'une instance du superviseur ; elle ne remplace pas un verrou distribué et ne constitue pas le protocole par jetons de la section 3.3.