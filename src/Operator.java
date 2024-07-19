import java.io.IOException;
import java.util.HashMap;

public class Operator {
    static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L,EP=0L;
    static boolean CWK=true,CWQ=true,CBK=true,CBQ=true,whiteToMove=true;//true=castle is possible
    static int searchTime=5000,moveCounter;
    static int MATE_SCORE=5000,NULL_INT=Integer.MIN_VALUE;
    static Position position;
    static HashMap<Long, TranspositionEntry> transpositionTable = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Zobrist.zobristFillArray();
        UCI.communicate();
        

        // BoardGenerator.initiateStandardChess();
        // System.out.println(Moves.possibleWhiteMoves(position));
        // System.out.println(Rating.eval(position, whiteToMove));
        // long timeTest = System.currentTimeMillis();
        // System.out.println("Time: "+(System.currentTimeMillis()-timeTest));
        // System.out.println(Search.bestMove);
        // timeTest = System.currentTimeMillis();
        // System.out.println(Search.iterativeDeepeningWithMoveOrdering(position, 5000, whiteToMove));
        // System.out.println("Time: "+(System.currentTimeMillis()-timeTest));
        

    }

    public static void updatePosition(){
        WP=position.WP;WN=position.WN;WB=position.WB;WR=position.WR;WQ=position.WQ;WK=position.WK;BP=position.BP;BN=position.BN;BB=position.BB;BR=position.BR;BQ=position.BQ;BK=position.BK;EP=position.EP;
        CWK = position.CWK; CWQ = position.CWQ; CBK = position.CBK; CBQ = position.CBQ;
    }
}
