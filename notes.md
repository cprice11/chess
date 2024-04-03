# My Notes
## Christian Price

## TODOS
- [ ] Create DAO templates C\*R\*U\*D
- [ ] Create Parent classes for Services, DataAccess Interface, Request, Result, Serialization, and Handler
- [ ] set up the browser interface (see docs) (What is Spark?)
- [ ] for a break, refactor the Board class to not use empty arrays.
- [ ] define Unit tests before coding
- [ ] start with clear service access method
- [ ] next setup a handler for an endpoint.
- [ ] phase-1 also needs cleaned up in general
- [ ] read Rubric for code quality 

The class structure for this project is a little odd.

So far some interfaces are set in stone, so what remains is to organize from those end points

In a related note, the naming of methods doesn't quite fit the guidelines from the lesson.

 [ ] for phase 3+ be very consistent with naming.

 * **Don't duplicate!**


A DOA method that verifies tokens without having to check is pretty neat.

## ChessGame
- TeamColor                     This seems out of place to me, but stays to keep passoff.
*   Moves   validMoves()
*   void    makeMove() throws
*   bool    isInCheck()
*   bool    isInCheckmate()
*   bool    isInStalemate()
---

## ChessBoard

* **ChessBoard**
*           addPiece(Pie)
*   Pie     getPiece(Pos)
*           resetBoard()
---


## ChessPiece

* **ChessPiece**
*   col     getTeamColor()
*   typ     getPieceType()
*   movs    pieceMoves()    bor, pos
---

- color
- type
- PieceType


## ChessMove

* **ChessMove** start pos, end pos, pro pie
*   pos     getStartPosition()
*   pos     getEndPosition()
*   pie     getPromotionPiece()
---

- startPosition
- endPosition
- promotionPiece

## ChessPosition 

* **ChessPosition** row int, col int
*   int     getRow()
*   int     getColumn()
---
- row
- col

===

## GameState
RuleSet
Board
stateBools
history

## Ruleset 
+               - MovementRules
+               - Move
+               - Other Rules (Checkmate Atomic)
> Ruleset
> ChessBoard
> is
> 

===

# ♕ Phase 3: Chess Web API

## Required HTTP Endpoints
### Clear application
### Register
### Login
### Logout
### List Games
### Create Game
### Join Game


## Required Classes

### Data Model Classes
**UserData**
**GameData**
**AuthData**

### Data Access Classes

- **Create** objects in the data store
- **Read** objects from the data store
- **Update** objects already in the data store
- **Delete** objects from the data store


### DataAccess Interface

### Service Classes

### Request and Result Classes

### Serialization

### Server Handler Classes

### Server Class

### Web Browser Interface

## Service Unit Tests


## Suggested Implementation Order

You can create and test your code in whatever order you would like. However, if you are trying to figure out how to get started, you might consider the following order.

- [ ] Use your sequence diagrams to guide the decision for what classes you need for your server, service, and data access objects.
- [ ] Implement your services
    - [ ] Create the classes you need to implement the `clear` service method.
    - [ ] Write a service test for `clear` to make sure the service and data access parts of your code are working properly.
    - [ ] Repeat writing and implementing service classes and tests until you have built all the required functionality.
- [ ] Create your server handler for a single endpoint that simply returns a string.
- [ ] Make sure you can hit your endpoint from a browser or Curl.
- [ ] Implement your server handlers
    - [ ] Convert your test server handler to implement the `clear` and `register` endpoints.
    - [ ] Run the pass off test for registration.
    - [ ] Repeat writing and implementing server handlers until you have completed all the pass off tests.

## Relevant Instruction Topics

## ☑ Deliverable

### Pass Off Tests

### Service Unit Tests

### Code Quality

### Pass Off, Submission, and Grading

### Grading Rubric

**⚠ NOTE**: You are required to commit to GitHub with every minor milestone. For example, after you successfully pass a test. This should result in a commit history that clearly details your work on this phase. If your Git history does not demonstrate your efforts then your submission may be rejected.

| Category       | Criteria                                                                                                                                                                                         |       Points |
| :------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -----------: |
| GitHub History | At least 10 GitHub commits evenly spread over the assignment period that demonstrate proof of work                                                                                               | Prerequisite |
| Web API Works  | All pass off test cases in `StandardAPITests.java` succeed                                                                                                                                       |          115 |
| Web Page Loads | Test web page properly loads in browser (including all files linked to by the test page: favicon.ico, index.css, index.js)                                                                       |           10 |
| Code Quality   | [Rubric](../code-quality-rubric.md)                                                                                                                                                              |           30 |
| Unit Tests     | All test cases pass<br/>Each public method on your **Service classes** has two test cases, one positive test and one negative test<br/>Every test case includes an Assert statement of some type |           25 |
|                | Total                                                                                                                                                                                            |          180 |


