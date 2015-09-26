GreetingsApp
============

[![Build Status](https://travis-ci.org/rogargon/greetingsApp.svg?branch=spring-data-rest)](https://travis-ci.org/rogargon/greetingsApp)

This is a simple Spring demo of a Greetings application providing a RESTFul JSON API and a AngularJS client. It features:

* Spring Data REST
* Spring Data JPA/Hibernate persistence with HSQL and Heroku PostgreSQL
* Unit Testing
* Spring Cucumber acceptance tests
* Travis Continuous Integration and automated deployment to Heroku
* ...

To run locally, first build WAR package:
```
mvn package
```

Then run embedded Tomcat server:
```
mvn exec:exec
```

The application will be available at http://localhost:8080/api/ (Server API) and http://localhost:8080/app/ (AngularJS Client)
