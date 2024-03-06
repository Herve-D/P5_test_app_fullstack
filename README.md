# Testez une application full-stack

# Yoga

## Technologies utilisées

* Java
* Spring Boot
* JUnit
* Mockito

* Angular
* Jest
* Cypress

## Démarrage du projet

Cloner le dépôt :

> git clone [https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing](https://github.com/Herve-D/P5_test_app_fullstack.git)

### Installation des dépendances :

Pour la partie back-end :

> cd back

> mvn clean install

Pour la partie front-end :

> cd front

> npm install

### Démarrage back-end :

> mvn spring-boot:run

### Démarrage front-end :

> npm run start

Le frontend sera accessible à l'adresse [localhost:4200](http://localhost:4200)

### MySQL

Le script SQL script est disponible ici : `ressources/sql/script.sql`

Le compte admin par défaut :
- login: yoga@studio.com
- password: test!1234

Ajustez si nécessaire à vos informations de base de données dans `application.properties` :
```
spring.datasource.username=xxxx
spring.datasource.password=xxxx
```

## Effectuer les tests

### Front : End-to-end

Lancer les tests e2e :

> npm run e2e

Générer le rapport de couverture (après avoir effectué les tests e2e):

> npm run e2e:coverage

Le rapport est disponible ici :

> front/coverage/lcov-report/index.html

### Front : Tests unitaires et d'intégration

Lancer les tests :

> npm run test

Générer le rapport de couverture :

> npm test --collect-coverage

Le rapport est disponible ici :

> front/coverage/jest/lcov-report/index.html

### Back : Tests unitaires et d'intégration

Lancer les tests :

> mvn clean test

Rapport de couverture disponible ici:

> back/target/site/jacoco/index.html
