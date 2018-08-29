# Tutor Finder

## Getting Started

### Setting up the database

Create the database
```
createdb paw -O root
```
 in this case under the user root, you can use whichever user you want as long as it can create databases

Set up the database schemas
```
psql -f persistance/src/resources/tableschema.sql
```
And that's it

## Running the tests

To run the full test battery with maven installed simply run

```
mvn test
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring](https://spring.io/) - MVC Web Framework

## Authors

* **Francisco Delgado**  - [fdelgado96](https://github.com/fdelgado96)

