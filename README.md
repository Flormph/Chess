# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

psuedo-sequence diagram url (phase 2) : https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGckChUEMDGwD2AnOcESAO2FQAcN9QsRrzoBlSfANzaproYydYcQWZN1rC+TACIBBAPJjejYNCkZgGAEYYkkRROXQAsrgAmkcGlT5NuAB7RcnQifOWAXAFdd+dLGJkwAC0AHyCzu4A2lIAogAyMQAqMQC60AD0ppqo4Wyh4cLI7tBYUDQASpAAjp7IwAAUAJQ5bOyFSKFqGtq6xaWQNDLg4E0tQiJIADxBQV1aOpB9ZfiVSJS4pLpjztNBBRPu-RXI65t6-iTku7n47gBMAAwPADqkAN6knsMAvug29o5nMYzBYkO4MJ5gPBErgANZkPwBcj5VpsKIABTkzESaXS3i4NxR4yK0HwkAA5iAkMA2JUanV6vj8KQMABbSAAGmg1BQAHcCKYuZBWRgQOBmvtkJ15O5yZBgABVHyMnws9nNWRyTrqea9ZjxGKwRLQJlqmAAM3wuFZJp823au017k+w1Qmu13QWhzJ6kgSrYKrYZq5PKQ-PwgugwtF4rd8g9usWAEkAHL68rG01szncnRhgVCkVi6CpxJyaD+3ySjohJ1YH00mSQ+Cjd21nU9ZNpmIZ21B7NciFQmHw0gllNl6BD+D2iaOmXTkcIm7XVrtdxkynU2knDa6UYXQLTG73J6vN6LuFkX7WWwOJxsYFuMHk7OIy7BMKo26RTHY3G6CgIAbNseRfsSYLgLglKkHStTUoGzIDqG4amBKa4TNKciyvKlaIcGKEChq8btp6eoGkafZIey0CWtaVGzsg87YS64CMTWdYNpATZQq2JFzJ27ipummaqgOU7NkuY6luW07scx4KSVepCgfgq4QYpw7Kagh5XDMJ6PC87yXqON66Z+J7RAayQAcgSDASphLgW0BxQeSuCQnBDImWQ6EQVh7huR5DRZuyg5KaOxFaqRibuLECTJBJWmjrRVo2nJ1YKaxqnqS5RRBZ5u5nAeSLALlaKGeerFmaVRIRJEADiSS4q+7KqXV67ENSXkIT5pB+XlHEynKwANdmSB8dFAlevqCSURgwypfRrV6JlMxOitaArvpGH5VSwCrKc+7NOZ5W3JV7ybTVH51Wiv5YjiGQre1znrvWAw0mN7I9Q0fVcitKbZgN7QBe9vpfZA9R-QDQNxlNHYzRRxp9Ut6XNvJ60ytlbbTb0wk9saMM0TJ0DPWtszDdmSZSDlO0aWDn3Zode6QydpVnaeRlvCt1PXYEt0-uiCqPekZPfh1BwAFa4CApAQz9UMRWQXI0mysC4FB+D-VTUjA5htYytLssQ4ryXK6rrLq5rHI87rcMJoJrBQDgSXQspqOu-bMWOxYkAu7bHubV7uOLAq6KyIlK2vPqxqUOAGAAJ5sK8ADqAASPYxKTOsYxTLFfGx217LtYJG3LTNFcdOns3TEQXR8Be-EAA
