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

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQRnc5gSuAYDy-T4nhtJZdj6AE6LvJVtSHGAEAQpA0FzGtAxgNj3mxQddV7ZMyQpM04VIwsmWTN0OXIXl+UFAAqGAADFwhqABZasYAAdRYSsuWrO4AF5SJ7It1OnAdlX4pSjG0CcpwJOSWQUsBM2zUT6WctSFVLTTuT5AVOwbc82yM0yEuzSzrPCWzSMcpixCTKLeMnFUYBC1IYEgC9fITWigV3QTEJXNdMA3Hiit3D8xLGQDry+P8L2-G8YGGkbRuG6j73QjBn1fd9GM-bqgL6xL-0GzjTBAsDvD8QIvBQNt4PsXxmGQ9JMkwB88ncnd5wqaRtPqLlmhaAjVCI7p+vHCa3LOOr536T7Ly4v7MI88oGuO2FAekjzZJcwK0tSaAkAALxQMqoZWi9VJdEtCjLWKdMRpKkhSsyRwymy7PzXKFuqwxEwCg0OBQbhMgx6GItxjTynuvlHuJ-9DOM8mG0prLqdGWmuq5vsfsHVV1U1GSfta+c1Q1FruJo66SkVzXvtOS6wBmpxTDMTgtogwJIQ4ODoRgABxfNWVO1CLqm5h2tux2uTwlp7HzD6sa+4o2sKNWGMB9a1a3BWBOhZ3RlUTHsxJmG+LhyKDRHZG0Y5kO0BxuX8Zi7T4sB4XUopqyqZypyVf8+GDXJMAk7UVPR2x2XXNLzly8FQPRTJmAh7UHvCt18EYAMMAAB44FSFl2-yemt0jhPYnb1kmoQddtbjsP-qmMfVHGSp+jHj1pHPgBGJ8AGYABYnhQzIlLuFYvh0BBQGbD-DwTC+GPL0+YgF7BgLeHW8BPblAqC+JwzQegnxdufCol98zXzvo-F+Uw36mjyl-J4P8-4AOYn0YB+ZQGjHAZAzanhtqQWwD4KA2BuDwGNJkJ2+YUhnTQjkL2utsK1AaM9Mewc07-nfCA-MVEj7r21lHQuswZFfmBnOQ+fFUycJQNvTuJMVFUPzBnYqDMm7ZxTIjPO6MGz6P-MXXu7J+5xUFJXEeNdMrZTytLO4a9GbN0sa3PRqiCwT1dNFZxRMx5VzMqfPx5jcbaPTJkdu19YQhIcVFPu5ZfSDyMaKYSkAYC+IKuEoq09UnSD8bVOcSSTTt13vvEG8sj5XBQaMLB5R77PxgHIm6k0BGm3fO0lAnSYDdKfr0i2oEGHWwCJYVmglkgwAAFIQGEtw0UAQSEgGbB7ARoMbpwKqJSf24jgiF3fGw4ACyoBwAgIJKAhiOnSD6ccVWii9x9EBtaX+tz7mPOeaMm+6jNwtK0TAAAVustA7c7EXl+TcygALoBAuviYvyMgmaWNzlAVGNjQqc1KXjJxWkXGCwvDExG4svELR8fmeJWKAlBThT80emDpCZPCdkwm8VokjziY3JlFjyjQuEqy5REk-nIoeai9lLyuUkrLN6XJFL0CIv+bK2AhSIDyuBWEks5SSoa2VrDD5f19aaljuCvWxSlZQIGY+GACDplWx2gELwNyYHplgMAbAbDCDxESLw92xtDm2rug9J6rRjAOvNbUw03A8CwmxNao1-EQBJrhBimqCTiypizSmg1W53R8z9rSqAqhpin2mIDDQxLNGmMtdUiOnyTVa2ad7ZthtCjG1NubTaQA
