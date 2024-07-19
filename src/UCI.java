import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class UCI {
    public static final String ENGINENAME = "Jimmy Decimator";
    public static void communicate() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input=br.readLine();
            if ("uci".equals(input))
            {
                inputUCI();
            }
            else if (input.startsWith("setoption"))
            {
                inputSetOption(input);
            }
            else if ("isready".equals(input))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(input))
            {
                inputUCINewGame();
            }
            else if (input.startsWith("position"))
            {
                inputPosition(input);
            }
            else if (input.startsWith("go"))
            {
                inputGo(input);
            }
            else if (input.equals("qOperatort"))
            {
                inputQOperatort();
            }
            else if ("print".equals(input))
            {
                inputPrint();
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ENGINENAME);
        System.out.println("id author Diego");
        //options go here
        System.out.println("uciok");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    public static void inputUCINewGame() {
        //add code here
    }
    public static void inputPosition(String input) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            BoardGenerator.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            BoardGenerator.importFEN(input);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            while (input.length()>0)
            {
                String moves;
                if (Operator.whiteToMove) {
                    moves=Moves.possibleWhiteMoves(Operator.position);
                } else {
                    moves=Moves.possibleBlackMoves(Operator.position);
                }
                algebraToMove(input,moves);
                input=input.substring(input.indexOf(' ')+1);
            }
        }
        
    }
    public static void inputGo(String input) {    
        String move = Search.iterativeDeepeningWithMoveOrdering(Operator.position, 5000, Operator.whiteToMove);
        Operator.whiteToMove = !Operator.whiteToMove;    
        System.out.println("bestmove "+moveToAlgebra(move));
        Operator.position = Search.doLegalMove(move, Operator.position);
        Operator.updatePosition();
        Operator.transpositionTable.get(Zobrist.getZobristHash(Operator.position, Operator.whiteToMove)).incrementOccurence();
    }

    public static String moveToAlgebra(String move) {
        String append="";
        int start=0,end=0;
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
        } else if (move.charAt(3)=='P') {//pawn promotion
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[1]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[0]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[6]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[7]);
            }
            append=""+Character.toLowerCase(move.charAt(2));
        } else if (move.charAt(3)=='E') {//en passant
            if (move.charAt(2)=='W') {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[3]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[2]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[4]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[5]);
            }
        }
        String returnMove="";
        returnMove+=(char)('a'+(start%8));
        returnMove+=(char)('8'-(start/8));
        returnMove+=(char)('a'+(end%8));
        returnMove+=(char)('8'-(end/8));
        returnMove+=append;
        return returnMove;
    }

    public static String convertUCIToCustom(String uciMove) {
        // Example UCI move: "e2e4" or "e7e8q" (promotion to queen)
        String fromSquare = uciMove.substring(0, 2);
        String toSquare = uciMove.substring(2, 4);

        // Regular move
        if (uciMove.length() == 4) {
            return convertSquare(fromSquare) + convertSquare(toSquare);
        }

        // Promotion move
        if (uciMove.length() == 5) {
            char promotionPiece = uciMove.charAt(4);
            return convertSquare(fromSquare) + convertSquare(toSquare) +
                   promotionPiece + "P";
        }

        // En passant move
        if (uciMove.endsWith("ep")) {
            return convertSquare(fromSquare) + convertSquare(toSquare) +
                   (uciMove.startsWith("w") ? "WE" : "BE");
        }

        // Handle other cases (e.g., castling, invalid input)
        return "Invalid move format";
    }

    // Convert UCI square notation to custom format
    private static String convertSquare(String uciSquare) {
        int file = uciSquare.charAt(0) - 'a'; // Convert 'a' to 0, 'b' to 1, etc.
        int rank = 7 - (uciSquare.charAt(1) - '1'); // Convert '1' to 7, '2' to 6, etc.
        return "" + rank + file;
    }

    public static void algebraToMove(String input,String moves) {
        int start=0,end=0;
        int from=(input.charAt(0)-'a')+(8*('8'-input.charAt(1)));
        int to=(input.charAt(2)-'a')+(8*('8'-input.charAt(3)));
        for (int i=0;i<moves.length();i+=4) {
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                start=(Character.getNumericValue(moves.charAt(i+0))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                end=(Character.getNumericValue(moves.charAt(i+2))*8)+(Character.getNumericValue(moves.charAt(i+3)));
            } else if (moves.charAt(i+3)=='P') {//pawn promotion
                if (Character.isUpperCase(moves.charAt(i+2))) {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[1]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[0]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[6]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[7]);
                }
            } else if (moves.charAt(i+3)=='E') {//en passant
                if (moves.charAt(i+2)=='W') {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[3]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[2]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[4]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[5]);
                }
            }
            if ((start==from) && (end==to)) {
                if ((input.charAt(4)==' ') || (Character.toUpperCase(input.charAt(4))==Character.toUpperCase(moves.charAt(i+2)))) {
                    if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                        start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                        if (((1L<<start)&Operator.WK)!=0) {Operator.CWK=false; Operator.CWQ=false;}
                        else if (((1L<<start)&Operator.BK)!=0) {Operator.CBK=false; Operator.CBQ=false;}
                        else if (((1L<<start)&Operator.WR&(1L<<63))!=0) {Operator.CWK=false;}
                        else if (((1L<<start)&Operator.WR&(1L<<56))!=0) {Operator.CWQ=false;}
                        else if (((1L<<start)&Operator.BR&(1L<<7))!=0) {Operator.CBK=false;}
                        else if (((1L<<start)&Operator.BR&1L)!=0) {Operator.CBQ=false;}
                    }
                    Operator.EP=Moves.makeMoveEP(Operator.WP|Operator.BP,moves.substring(i,i+4));
                    Operator.WR=Moves.makeMoveCastle(Operator.WR, Operator.WK|Operator.BK, moves.substring(i,i+4), 'R');
                    Operator.BR=Moves.makeMoveCastle(Operator.BR, Operator.WK|Operator.BK, moves.substring(i,i+4), 'r');
                    Operator.WP=Moves.makeMove(Operator.WP, moves.substring(i,i+4), 'P');
                    Operator.WN=Moves.makeMove(Operator.WN, moves.substring(i,i+4), 'N');
                    Operator.WB=Moves.makeMove(Operator.WB, moves.substring(i,i+4), 'B');
                    Operator.WR=Moves.makeMove(Operator.WR, moves.substring(i,i+4), 'R');
                    Operator.WQ=Moves.makeMove(Operator.WQ, moves.substring(i,i+4), 'Q');
                    Operator.WK=Moves.makeMove(Operator.WK, moves.substring(i,i+4), 'K');
                    Operator.BP=Moves.makeMove(Operator.BP, moves.substring(i,i+4), 'p');
                    Operator.BN=Moves.makeMove(Operator.BN, moves.substring(i,i+4), 'n');
                    Operator.BB=Moves.makeMove(Operator.BB, moves.substring(i,i+4), 'b');
                    Operator.BR=Moves.makeMove(Operator.BR, moves.substring(i,i+4), 'r');
                    Operator.BQ=Moves.makeMove(Operator.BQ, moves.substring(i,i+4), 'q');
                    Operator.BK=Moves.makeMove(Operator.BK, moves.substring(i,i+4), 'k');
                    Operator.whiteToMove=!Operator.whiteToMove;
                    break;
                }
            }
        }
        Operator.position = new Position(Operator.WP,Operator.WN,Operator.WB,Operator.WR,Operator.WQ,Operator.WK,Operator.BP,Operator.BN,Operator.BB,Operator.BR,Operator.BQ,Operator.BK,Operator.EP,Operator.CWK,Operator.CWQ,Operator.CBK,Operator.CBQ);
    }
    public static void inputQOperatort() {
        System.exit(0);
    }

    public static void inputPrint() {
        BoardGenerator.drawArray(Operator.WP,Operator.WN,Operator.WB,Operator.WR,Operator.WQ,Operator.WK,Operator.BP,Operator.BN,Operator.BB,Operator.BR,Operator.BQ,Operator.BK);
    }

    
}

