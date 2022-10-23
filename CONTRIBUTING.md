# Contributing to *shabbat-api*

:clap: First off, thank you for taking the time to contribute. :clap:

- Fork the repository
- Create a new branch on your fork
- Commit your changes
- Create a pull request against the `main` branch

## Code walkthrough

The main entrypoint of the application is [ShabbatAPI](https://github.com/TomerFi/shabbat-api/blob/main/src/main/java/info/tomfi/shabbat/ShabbatAPI.java).

A request can be build using [APIRequest](https://github.com/TomerFi/shabbat-api/blob/main/src/main/java/info/tomfi/shabbat/APIRequest.java).

Using [Jackson](https://github.com/FasterXML/jackson), the responses are parsed into [APIResponse](https://github.com/TomerFi/shabbat-api/blob/main/src/main/java/info/tomfi/shabbat/APIResponse.java).

## Build commands

- `mvn test` run unit tests
- `mvn verify` verify the project
- `mvn install` install the *snapshot* version
