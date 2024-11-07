# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Phases

- [x] phase - 0
- [x] phase - 1
- [x] phase - 2
- [ ] phase - 3
- [ ] phase - 4
- [ ] phase - 5
- [ ] phase - 6

## Diagrams

<details>
  <summary><strong>Server Class Structure Diagrams</strong></summary>
 
![Class structure from module 2 specs](https://github.com/softwareconstruction240/softwareconstruction/raw/main/chess/2-server-design/server-class-structure.png)

</details>

### Server Sequence Diagrams

<details>
  <summary><strong>Clear Application</strong></summary>

![clear application diagram](/phase-2-diagrams/clear-application-diagram.png)

</details>
<details>
  <summary><strong>Register</strong></summary>

![register diagram](/phase-2-diagrams/register-diagram.png)

</details>
<details>
  <summary><strong>Login</strong></summary>

![login diagram](/phase-2-diagrams/login-diagram.png)

</details>
  <details>
  <summary><strong>Logout</strong></summary>

![logout diagram](/phase-2-diagrams/logout-diagram.png)

</details>  <details>
  <summary><strong>List Games</strong></summary>

![list games diagram](/phase-2-diagrams/list-games-diagram.png)

</details>
  <details>
  <summary><strong>Create Game</strong></summary>

![create game diagram](/phase-2-diagrams/create-game-diagram.png)

</details>
<details>
<summary><strong>Join Game</strong></summary>

![join game diagram](/phase-2-diagrams/join-game-diagram.png)

</details>

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

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
