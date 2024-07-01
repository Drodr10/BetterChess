# Better Chess
### Disclaimer! When I mean better, I'm talking in relation to my previous chess engine that I have in another repo.
I also in no way shape or form claim the ideas, and in some cases code as my own as it was all written while watching advanced tutorials from [Logic Crazy Chess](https://www.youtube.com/@LogicCrazyChess).
## Info
This readme will contain and explain all techniques used in this chess engine in order of what was applied first to last. So take your time reading if you want to learn and understand how to make your own bot.
## Bitboard [(link to video)](https://youtu.be/a5IGltn95Bk?)

Divides the chess board into 12 different boards, with each board being the binary represetation of the piece type in that board.

Example piece types would be white rook, black pawn, etc. So 6 for white and 6 for black.
- The starting bitboard for black pawns would be 0000000011111111000000000000000000000000000000000000000000000000.
- It currently looks confusing, so for a easier way of seeing it, you can seperate it in your mind like this:
```
00000000
11111111   <- Black side
00000000
00000000
00000000
00000000
00000000   <- White side
00000000
```
This representation is implemented with a 64-bit integer, which in java is a long. Forget the fact that it is signed, because we just want the stored binary inside, not the base ten representation.

This takes much less space compared to my previous storage method of having a 2d String array. Getting possible moves is also quite easy since all movements are basically shifts in the binary.