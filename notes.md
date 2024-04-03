# My Notes
## Christian Price

## TODOS

clean up tests one by one.

The class structure for this project is a little odd.

So far some interfaces are set in stone, so what remains is to organize from those end points

In a related note, the naming of methods doesn't quite fit the guidelines from the lesson.

for phase 3+ be very consistent with naming.

Don't duplicate!

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


