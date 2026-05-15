# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[API Endpoint Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUM8zRtF0vQGOo+RoDABGKp8iyrD8+h-DsewNqBgpAc84HlqpYzqb8-yAqZ0KPMB4lUEiCBCeKGKCcJBJEmApLboYu40vutSMiybIqVyN5+XeS7CmKEpujKcplu8SqmCqwYam62q6vqIVGBAahoAA5MwVpopgMBlTIt4OkmvrOl2sVbuVHb2e+NUSXUboAHIQGAIq+JwUYxnGhSNXp1XwMgqbpvhozLDmqh5vM0FFiW9R6OuKKEmoYBLHRTaYCl6owAA6t4DgwOZOyleVw5VXZSL1fIV1lW+Pl2c8zLTJe0BIAAXigHADSgsYKU9TUYWNom1OmACMWazfyC0FmMy3QPUPifXq31-bsMC7QxVAmkg66OcJoMvYKN30oecgoM+8Tnpe16NZTAqRSua4BozW5k7V7YGXUrnihkqgAaD-MgdUhmEcZi2mSsYwUVR9ZqVcI2S-CyYTdhuHTUZiUmepiuIcrVmNgx9GMd4fj+F4KDoDEcSJLb9uub4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMRtIeh8KVPz9QUVjeQFDA8TBhwtDi3dLowCTbsM-BxtoF55O+byI4BUydP55RhdzuFVWVMu0VPlz8iyvKkfoMlqoagAkmgBPIOunfDddlWLlnXY9n2POdhLbW1B9CdQL9-2A8D8Zq21mFa2AUNOLDM1jHNiNLcWqMKhj8RY-9O1m6YLPz81XZ06+vOlwuo7p9wx6XtXStFzCmXBuQpajSBQD-Qwr9uYlw-DZZ4It9yYEzm9OoIwrijU1iUPeOssx40tsxfwKJ1z+GwOKDU-E0QwAAOJKg0B7VB3tqEB2DvYJUEdLwAOjvpeBpZl6-STopVOIYM68M9rVeoyAci0JzP-WupgS5UnHqOQKVcR512AYuRuUVxQtxfNoduxpOGF27qlGA-dB5EyMQXKOzNlFPyRNPfsW9n5P3elfG+ANoxAyGrPRMWDJoHzhsfBG+Yz4rUvvw7Gd8LZAM-g4l00DHreQpso2oUiwAyLUBiDR8S2bUKZJWBANClQejifuBJXY4AQD7CgcACkAA8WSeTlAUe-OBsJniUOkXQ4WosUGtRqLUcYbCcwLAaC4SZnQMHqx3tg2ouDRhjFGWocZkyXDTPvp4K2AQOAAHY3BOBQE4GIEZghwC4gANngBOQwWSYBFF3uIhejRWgdFYewq+ACCIrI6pBFW3COlfnjl9FeP1BEwAANSyggOaZBYjJ5jluVkjEcBkVKncltYu78lH13pKov+6jykRW0aKXRnN9Ft3iiPUxh0LGE2HsY2xY88Ws0RU4vxg5GFL1BavLxg0QYuPBgE7CMNgknzCYWc+pZ0ZRNvrjLZj8xovRXK3YAbS54f38tTdEKKVnXiVaSx8JS5hlKVYilZvdpAatcWNOOaKjwoCyX0hAgEEWMJGUqK1CxobhGCIEGZ28Ia7wWXhH5XrpA+r9QGrZTFraWAgY5TYDskAJDAAmvsEBk0ACkIDihNYYfwyRQBqkedg55QzXnMhkj0FZHCbHoAItgBAwAE1QGqY5KAOl5aWsjbUX1-rA2JljrwkFmMwUQuhToWFlh4WdJjk1JEAArPNaAUW5vFM6lAm0SQ2sHLizRKjK6EqZegXJFS2bNwpVeAx1LT2FH2j3cxA8GXWJrsysq5rBmON7M48qsDBnuLlfynxgr-2zODfMsVR8JWLSlRE2VvLokKtiV+xdiS1V7p3Aez+FdMlKgxL289JLQFXuabe+UvbaUanuRwXKAo0DdRgMVHIoM0Mqo5S4rlgG6gACEQyYpyOvXxQqNaVEhlNcVoS4PI2latEMMANoeRiXtdjEjyPJIOhqQT6J1wgBqdASsZoazhlHs9dpPGVxVlDKZ4TYHzNBpFXvdMmYYPSaRijPh1mTPURQ3tRRFVWW1D8FoXVBH9XaGIyA5czJsChbuaUmBFmHjztqIgkcc6vxuLQUO4V4mQ2LPQbGnZNsoCtpTWmrw5XEDBlgMAbAzbCAQrLeYCt-ofZ+wDkHXoxhAUjtSzAEA3A8A5NMJnb92chu1dGykrV5cpt4DdKoUbhrQHgMgUUjc7okuapw9q4bUA6bLai1otbECmSGBNMUpJ6rZt7fmwdrJx3iXReFOti7m2NO3YAyl4F6X+SZdsh63LYnxrzMK3jIAA)

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
