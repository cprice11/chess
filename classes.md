---
title: Chess Server
---
classDiagram
class DAO
<<interface>>DAO
DAO <|.. AuthDAO
DAO <|.. GameDAO
DAO <|.. UserDAO
AuthDAO <|-- MemoryAuthDAO
GameDAO <|-- MemoryGameDAO
UserDAO <|-- MemoryUserDAO
DAO --> DataAccessException
class Server

class Service
Service <|-- AuthService
Service <|-- DevService
Service <|-- GameService
Service <|-- RegistrationService
