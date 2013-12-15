# SEDRI
## Installation
* Maven

  If you want to build the sources on your own you can use Maven to install the dependencies and build the project by calling `mvn compile`.
* standalone jar

  There is also a standalone jar `sedri-0.1-jar-with-dependencies.jar` including all dependencies. This requires no installation.

## Usage
After you have created the configuration file start the web server by calling

    mvn exec:java
or by simply executing the jar file with

    java -jar sedri-0.1-jar-with-dependencies.jar

Afterwards you can execute requests against your configured endpoints. 

See the [usermanual](usermanual.pdf) for an explanation of all configuration elements or the [example configuration](config.xml).
