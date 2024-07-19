import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Search {
    
    static long startTime;

    public static int minimax (Position position, int depth, int alpha, int beta, boolean whiteToMove ) {
        if (System.currentTimeMillis()-startTime>=Operator.searchTime) {
            return 0;
        }
        if (depth == 0) {
            return Rating.eval(position, whiteToMove);
        }

        long key = Zobrist.getZobristHash(position, whiteToMove);
        if (Operator.transpositionTable.containsKey(key)) {
            TranspositionEntry entry = Operator.transpositionTable.get(key);
            if (entry.getDepth() >= depth) {
                return entry.getEvaluation();
            }
        }

        if (whiteToMove) {
            int max = -10000;
            String moves = getAllLegalMoves(Moves.possibleWhiteMoves(position), position, true);
            for (int i = 0; i < moves.length(); i += 4) {
                String move = moves.substring(i, i + 4);
                Position tempPosition = doLegalMove(move, position);
                int eval;
                long newKey = Zobrist.getZobristHash(tempPosition, whiteToMove);
                if (Operator.transpositionTable.containsKey(newKey)) {
                    TranspositionEntry entry = Operator.transpositionTable.get(newKey);
                    if(entry.getOccurences() == 3){
                        eval = 0;
                    }
                    else{
                        entry.incrementOccurence();
                        eval = minimax(tempPosition, depth - 1, alpha, beta, false);
                        entry.decrementOccurence();        
                    }
                }
                else
                    eval = minimax(tempPosition, depth - 1, alpha, beta, false);

                max = Math.max(max, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            Operator.transpositionTable.put(key, new TranspositionEntry(depth, max, 1));
            return max;
        } else {
            int min = 10000;
            String moves = getAllLegalMoves(Moves.possibleBlackMoves(position), position, false);
            for (int i = 0; i < moves.length(); i += 4) {
                String move = moves.substring(i, i + 4);
                Position tempPosition = doLegalMove(move, position);
                int eval;
                long newKey = Zobrist.getZobristHash(tempPosition, whiteToMove);
                if (Operator.transpositionTable.containsKey(newKey)) {
                    TranspositionEntry entry = Operator.transpositionTable.get(newKey);
                    if(entry.getOccurences() == 3){
                        eval = 0;
                    }
                    else{
                        entry.incrementOccurence();
                        eval = minimax(tempPosition, depth - 1, alpha, beta, true);
                        entry.decrementOccurence();        
                    }
                }
                else
                    eval = minimax(tempPosition, depth - 1, alpha, beta, true);

                min = Math.min(min, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            Operator.transpositionTable.put(key, new TranspositionEntry(depth, min, 1));
            return min;
        }
    }

    public static String iterativeDeepeningWithMoveOrdering(Position position, int timeLimit, boolean whiteToMove) {
        String prevBest = "";
        String bestMove = "";
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int depth = 0;
        startTime = System.currentTimeMillis();

        while (System.currentTimeMillis()-startTime<timeLimit) {
            depth++;
            prevBest = bestMove;
            List<String> orderedMoves = orderMoves(position, whiteToMove);
            for (String move : orderedMoves) {
                Position tempPosition = doLegalMove(move, position);
                int eval = minimax(tempPosition, depth, alpha, beta, !whiteToMove);
                
                // Update bestMove if this move is the best so far
                if (whiteToMove && eval > alpha) {
                    alpha = eval;
                    bestMove = move;
                } else if (!whiteToMove && eval < beta) {
                    beta = eval;
                    bestMove = move;
                }
            
                if (beta <= alpha || System.currentTimeMillis()-startTime>=timeLimit) {
                    break; // Alpha-beta cutoff
                }
            }
            
            // If no legal moves found, break early
            if (orderedMoves.isEmpty()) {
                break;
            }
        }
        
        return prevBest;
    }

    public static List<String> orderMoves(Position position, boolean whiteToMove) {
        String moves = (whiteToMove)?Moves.possibleWhiteMoves(position):Moves.possibleBlackMoves(position);
        String legalMoves = getAllLegalMoves(moves, position, whiteToMove);
        List<MoveScorePair> scoredMoves = new ArrayList<>();

        // Evaluate each move and assign a score
        String move;
        for (int i = 0; i < legalMoves.length(); i+=4) {
            move = legalMoves.substring(i, i+4);
            Position tempPosition = doLegalMove(move, position);
            int score = Rating.eval(tempPosition, !whiteToMove); // Implement this method to evaluate the move
            scoredMoves.add(new MoveScorePair(move, score));
        }

        // Sort moves based on the score (higher score first)
        Collections.sort(scoredMoves, Comparator.comparingInt(MoveScorePair::getScore).reversed());

        // Extract and return the ordered moves
        List<String> orderedMoves = new ArrayList<>();
        for (MoveScorePair pair : scoredMoves) {
            orderedMoves.add(pair.getMove());
        }

        return orderedMoves;
    }
    
    public static int getFirstLegalMove(String moves, Position position, boolean whiteToMove) {
        for (int i=0;i<moves.length();i+=4) {
            long WPt=Moves.makeMove(position.WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(position.WN, moves.substring(i,i+4), 'N'),
                    WBt=Moves.makeMove(position.WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(position.WR, moves.substring(i,i+4), 'R'),
                    WQt=Moves.makeMove(position.WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(position.WK, moves.substring(i,i+4), 'K'),
                    BPt=Moves.makeMove(position.BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(position.BN, moves.substring(i,i+4), 'n'),
                    BBt=Moves.makeMove(position.BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(position.BR, moves.substring(i,i+4), 'r'),
                    BQt=Moves.makeMove(position.BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(position.BK, moves.substring(i,i+4), 'k');
            WRt=Moves.makeMoveCastle(WRt, position.WK|position.BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, position.WK|position.BK, moves.substring(i,i+4), 'r');
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && whiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !whiteToMove)) {
                return i;
            }
        }
        return -1;
    }

    public static Position doLegalMove(String move, Position position){
        long WPt=Moves.makeMove(position.WP, move, 'P'), WNt=Moves.makeMove(position.WN, move, 'N'),
        WBt=Moves.makeMove(position.WB, move, 'B'), WRt=Moves.makeMove(position.WR, move, 'R'),
        WQt=Moves.makeMove(position.WQ, move, 'Q'), WKt=Moves.makeMove(position.WK, move, 'K'),
        BPt=Moves.makeMove(position.BP, move, 'p'), BNt=Moves.makeMove(position.BN, move, 'n'),
        BBt=Moves.makeMove(position.BB, move, 'b'), BRt=Moves.makeMove(position.BR, move, 'r'),
        BQt=Moves.makeMove(position.BQ, move, 'q'), BKt=Moves.makeMove(position.BK, move, 'k'),
        EPt=Moves.makeMoveEP(position.WP|position.BP,move);
        WRt=Moves.makeMoveCastle(WRt, position.WK|position.BK, move, 'R');
        BRt=Moves.makeMoveCastle(BRt, position.WK|position.BK, move, 'r');
        boolean CWKt=position.CWK,CWQt=position.CWQ,CBKt=position.CBK,CBQt=position.CBQ;
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            if (((1L<<start)&position.WK)!=0) {CWKt=false; CWQt=false;}
            else if (((1L<<start)&position.BK)!=0) {CBKt=false; CBQt=false;}
            else if (((1L<<start)&position.WR&(1L<<63))!=0) {CWKt=false;}
            else if (((1L<<start)&position.WR&(1L<<56))!=0) {CWQt=false;}
            else if (((1L<<start)&position.BR&(1L<<7))!=0) {CBKt=false;}
            else if (((1L<<start)&position.BR&1L)!=0) {CBQt=false;}
        }
        return new Position(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt, CWKt, CWQt, CBKt, CBQt);
    }

    public static String getAllLegalMoves(String allMoves, Position position, boolean whiteToMove){
        StringBuilder legalMoves = new StringBuilder();
        for (int i = 0; i < allMoves.length(); i+=4) {
            if (isLegalMove(allMoves.substring(i,i+4), position, whiteToMove)) {
                legalMoves.append(allMoves.substring(i,i+4));
            }
        }
        return legalMoves.toString();
    }

    public static boolean isLegalMove(String move, Position position, boolean whiteToMove){
        long WPt=Moves.makeMove(position.WP, move, 'P'), WNt=Moves.makeMove(position.WN, move, 'N'),
        WBt=Moves.makeMove(position.WB, move, 'B'), WRt=Moves.makeMove(position.WR, move, 'R'),
        WQt=Moves.makeMove(position.WQ, move, 'Q'), WKt=Moves.makeMove(position.WK, move, 'K'),
        BPt=Moves.makeMove(position.BP, move, 'p'), BNt=Moves.makeMove(position.BN, move, 'n'),
        BBt=Moves.makeMove(position.BB, move, 'b'), BRt=Moves.makeMove(position.BR, move, 'r'),
        BQt=Moves.makeMove(position.BQ, move, 'q'), BKt=Moves.makeMove(position.BK, move, 'k');
        WRt=Moves.makeMoveCastle(WRt, position.WK|position.BK, move, 'R');
        BRt=Moves.makeMoveCastle(BRt, position.WK|position.BK, move, 'r');
        return (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && whiteToMove) ||
                ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !whiteToMove));
    }
}
