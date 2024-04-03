---
title: Chess Server - Server Package
---
classDiagram
class Server
Server *-- Handler
Server *-- Serializer

clearHandler --|> Handler

CreateGameHandler --|> Handler
CreateGameHandler *-- CreateGameRequest
CreateGameHandler *-- CreateGameResult

JoinGame Handler --|> Handler
JoinGame Handler *-- JoinGame Request

ListGamesHandler --|> Handler
ListGamesHandler *-- ListGamesRequest
ListGamesHandler *-- ListGamesResult

Login Handler --|> Handler
Login Handler *-- Login Request
Login Handler *-- Login Result

Logout Handler --|> Handler
Logout Handler *-- Logout Request

RegisterHandler --|> Handler
RegisterHandler *-- RegisterRequest
RegisterHandler *-- RegisterResult



---
title: Chess Server - DataAccess Package
---
classDiagram
class DAO
<<interface>>DAO
DAO <|.. AuthDAO
<<interface>>AuthDAO
DAO <|.. GameDAO
<<interface>> GameDAO
DAO <|.. UserDAO
<<interface>> UserDAO
AuthDAO <|-- MemoryAuthDAO
GameDAO <|-- MemoryGameDAO
UserDAO <|-- MemoryUserDAO
DAO --> DataAccessException


---
title: Chess Server - Service Package
---
class Service
Service <|-- AuthService
Service <|-- DevService
Service <|-- GameService
Service <|-- RegistrationService
