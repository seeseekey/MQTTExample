# Java starter project

Starter project for Java based on Maven. Generates a fat JAR file containing all dependencies. JAR are created with:

> mvn package

## Dependencies

Delivers some basic dependencies:

* Guava
* GSON
* SLF4J
* JUnit (Version 5)

Guava is a java helper library which deliver some interesting functions and helper methods. GSON helps to 
serialise JSON into classes and vice verse. SLF4J is a facade for logging.

JUnit is used for unit testing.

# Plugins

Define Java 17 as project version. Assembly plugin for creating fat JAR file. Surfire-Plugin for executing tests.

# Extensions

Support to deploy artifact to custom repository via WebDAV. 

# Usage

* Open project in IDE
* Rename package name and project name
* Rename Starter class
* Remove unneeded dependencies from pom.xml
* Remove or change custom repository upload on deploy from pom.xml
* Happy coding