# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Sequence Diagram Link

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQUKDddw-Gsv0+J4bSWXY+gBc5NyVbUhxgBAEKQNBc0YsiYFY95sUHXVe2TMkKTNOFSMLJlkzdDlyF5flBQAKhgAAxcIagAWWrGAAHUWErLlqzuABeUieyLNTpwHZU+MUoxtAnKcCVkllU2NTJM2zET6Sc1SFVLDTvV9f0D0DTsG3PNsoxjEcIpdEseMnFUYBC1IYEgC8fITGcuN3ATEJXNdMDokEcvogYErGQDry+P8L2-G8YF6vr+t66j73QjBn1fd9ms-VqgI65L-26jjTBAsDvD8QIvBQNt4PsXxmGQ9JMkwB88jcnd5wqaQtPqLlmhaAjVCI7pOvHIbyqBXdnrQOqKpo7dcr4qrdthT6pPcmTnICmByTAArgbmi8VKy9TOS0gUkuzFKkkMkz0dSTK+1cv7wXyhsiv-UrDETfyDQ4FBuGChs4Yx-9EYJwoy0uvlrtxzGDKM0yMqTKKcuJtUNQprd6tVdVNXqrdqKuKjijvQnjrAManFMMxOBWiDAkhDg4OhGAAHF81ZfbUKOkbmEa86Ta5PCWnsfMnvhl7ld+qWefJuWRby5BYjN0ZVCZ0cEYlqmIYNaHYZB-GXPZjTuT5NHPr5nHBep7LTv+8oCrJkrI786OUyhilg7UMPMdZxP2RR1PdIz0yXZDhPhdz4mDDAAAeOBUhZSv8kjt653KQOwEr1kaoQdcfvlz2rimVu1HGSp+hXj1pDXgBGJ8AGYABYnhQzJFLuFYvh0BBQGbc-DwmL4V69fNH72GBbwawo1fKCoXycZoPRl7mzXhUDe+Yt67wPsfKYp9TQtUvk8a+t975MT6E-fML9Rhvw-stTwq1ILYB8FAbA3B4BBUMJXFIB00I5FtrnbCtQGi3RXm7ZmF53zP3zErM6bJvafVmOA0YWCxicXer9QcgV0yZCntXf8swuGjFBrxcGkUY4Ujju7NAtcopJwbtpH2F5m643bq6QmkiSbZkLuOYuMhs5SJNJXLesJFEFlMSWPR5Y4pQ0wfmTsnAfGjC3jAISkBAlqHcQvXi5QnHSBHrRH6DiZH5hnnPcRUSShLz6Jvbe5Q95HxgDw44qsbYa3fMAoJuSYD5MPoU7WoF8F6wCJYOmAlkgwAAFIQCEqbPxARkEgGbNbOhmFF6VCqJSJ2rDghaPfKQ4ALSoBwAgAJKACiIHSCKSrBJ4jhxaMEc8G+izlmrPWZUxaftO55QAFbdLQJXORF5rRHMoCc6AZyUBb2Uf9VRWV5Iw0ZvHIWZjPEpwMenbGAsGyRPMe5fOpNio2OklHNRZdoYPJyTokF9dNKN3CcYleGhgU5yJnlFejJTDIpLqi8otyhIPNIs8hZryVnvPCV8mFnjYqVnilNJlxzWWwFCRAdlcTiUZNFjLeJZxdkwDFrLeesKzqK0-sNOhGt6m6zWgELwCz4DcDwJ2bApDCDxESNQq2atRnKsqJzR2N1WjGFVaPYEqYDVwmxJc0lfEQDuthN83ydjS5uvTFARSocsUePrna7mNwiXZwlXlWxxKQ14FkZG5GMabKEphf7Piyb7GGndQVCNnLo1XRsiOeNpdE35uRS63c8rvrpKVZkuVUrXqnB-jAf+WtlpAA
