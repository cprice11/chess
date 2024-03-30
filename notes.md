# My Notes
## Christian Price

The class structure for this project is a little odd.

So far some interfaces are set in stone, so what remains is to organize from those end points

In a related note, the naming of methods doesn't quite fit the guidelines from the lesson.

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


