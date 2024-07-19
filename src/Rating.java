import java.util.Arrays;

import helpers.Ranks;
import helpers.Central;
import helpers.Files;
import helpers.Squares;


public class Rating {

    static final int[] pawnBoard={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        0,  0,  0,  0,  0,  0,  0,  0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5,  5, 10, 25, 25, 10,  5,  5,
        0,  0,  0, 20, 20,  0,  0,  0,
        5, -5,-10,  0,  0,-10, -5,  5,
        5, 10, 10,-20,-20, 10, 10,  5,
        0,  0,  0,  0,  0,  0,  0,  0};
    static final int[] bishopBoard={
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20};
    static final int[] knightBoard={
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50};
    static final int[] rookBoard={
        0,  0,  0,  0,  0,  0,  0,  0,
        5, 10, 10, 10, 10, 10, 10,  5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
       -5,  0,  0,  0,  0,  0,  0, -5,
        0,  0,  0,  5,  5,  0,  0,  0};
    static final int[] queenBoard={
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
        -5,  0,  5,  5,  5,  5,  0, -5,
         0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20};
    static final int[] kingMidBoard={
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
         20, 20,  0,  0,  0,  0, 20, 20,
         20, 30, 10,  0,  0, 10, 30, 20};
    static final int[] kingEndBoard={
        -50,-40,-30,-20,-20,-30,-40,-50,
        -30,-20,-10,  0,  0,-10,-20,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-30,  0,  0,  0,  0,-30,-30,
        -50,-30,-30,-30,-30,-30,-30,-50};

    static long unsafe;
    static boolean isAtEnd; 

    private Rating() {
        throw new IllegalStateException("Utility class");
    }

    public static int eval(Position p, boolean whiteToMove) {
        long WP = p.WP, WN = p.WN, WB = p.WB, WR = p.WR, WQ = p.WQ, WK = p.WK;
        long BP = p.BP, BN = p.BN, BB = p.BB, BR = p.BR, BQ = p.BQ, BK = p.BK;
        boolean CWK = p.CWK, CWQ = p.CWQ, CBK = p.CBK, CBQ = p.CBQ;
        
        int knightRookAffector = (16 - (Long.bitCount(WP) + Long.bitCount(BP))) * 5;
        int material = rateMaterial(WP, WN, WB, WR, WQ, knightRookAffector);
        int finalEval = material;
        isAtEnd = isEndgame(WQ, BQ, material);
    
        if (Search.getFirstLegalMove(Moves.possibleWhiteMoves(p), p, whiteToMove) == -1) {
            unsafe = Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
            if ((WK & unsafe) != 0) {
                return -5000;
            }
            return -1000;
        } else {
            finalEval += rateMobility(WP, WN, WB, WR, WQ, WK, BP, true);
            finalEval += ratePosition(WP, WN, WB, WR, WQ, WK, true);
            finalEval += evaluatePawnStructure(WP, BP);
            finalEval += evaluateKingSafety(p, true);
            finalEval += evaluatePieceActivity(p, true);
            finalEval += evaluateEndgame(p, true);
        }
    
        material = rateMaterial(BP, BN, BB, BR, BQ, knightRookAffector);
        finalEval -= material;
    
        if (Search.getFirstLegalMove(Moves.possibleBlackMoves(p), p, whiteToMove) == -1) {
            unsafe = Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
            if ((BK & unsafe) != 0) {
                return 5000;
            }
            return 1000;
        } else {
            finalEval -= rateMobility(BP, BN, BB, BR, BQ, BK, WP, false);
            finalEval -= ratePosition(BP, BN, BB, BR, BQ, BK, false);
            finalEval -= evaluateKingSafety(p, false);
            finalEval -= evaluatePieceActivity(p, false);
            finalEval -= evaluateEndgame(p, false);
        }
    
        return finalEval;
    }
    

    public static int rateMaterial(long P,long N,long B,long R,long Q, int affector){
        int materialRating = 0;
        int bishopRating = 0;
        int knightRating = 0;
        int rookRating = 0;
        for (int square = 0; square < 64; square++)
        {
            if (((P >> square) & 1) == 1)
            {
                materialRating+=100;
            }
            else if (((N >> square) & 1) == 1)
            {
                materialRating+=300;
                knightRating++;
            }
            else if (((B >> square) & 1) == 1)
            {
                bishopRating++;
            }
            else if (((R >> square) & 1) == 1)
            {
                materialRating+=500;
                rookRating++;
            }
            else if (((Q >> square) & 1) == 1)
            {
                materialRating+=900;
            }
        }
        if (bishopRating>=2) {
            materialRating+=300*bishopRating;
        } else if (bishopRating == 1) {
            materialRating+=250;
        }
        materialRating+=rookRating*affector-knightRating*affector;
        return materialRating;
    }
    
    public static int rateMobility(long P,long N,long B,long R,long Q,long K, long otherP, boolean isWhite){
        int mobility = Long.bitCount(Moves.bishopMoves(B));
        mobility += Long.bitCount(Moves.rookMoves(R));
        if (isWhite) {
            mobility += Long.bitCount(Moves.knightMoves(N)&~Moves.blackPawnMoves(otherP));
            mobility += Long.bitCount(Moves.whitePawnMoves(P));
        }else{
            mobility += Long.bitCount(Moves.knightMoves(N)&~Moves.whitePawnMoves(otherP));
            mobility += Long.bitCount(Moves.blackPawnMoves(P));
        }
        mobility += Long.bitCount(Moves.queenMoves(Q));
        mobility += Long.bitCount(Moves.kingMoves(K));

        return mobility;
    }

    public static int evaluatePieceActivity(Position p, boolean isWhite) {
        long ownPawns = isWhite ? p.WP : p.BP;
        long ownKnights = isWhite ? p.WN : p.BN;
        long ownBishops = isWhite ? p.WB : p.BB;
        long ownRooks = isWhite ? p.WR : p.BR;
        long ownQueens = isWhite ? p.WQ : p.BQ;
    
        int pieceActivityScore = 0;
    
        // Evaluate knight activity
        pieceActivityScore += evaluateKnightActivity(ownKnights, ownPawns);
    
        // Evaluate bishop activity
        pieceActivityScore += evaluateBishopActivity(ownBishops, ownPawns);
    
        // Evaluate rook activity
        pieceActivityScore += evaluateRookActivity(ownRooks, ownPawns);
    
        // Evaluate queen activity
        pieceActivityScore += evaluateQueenActivity(ownQueens, ownPawns);
    
        return pieceActivityScore;
    }
    
    private static int evaluateKnightActivity(long knights, long pawns) {
        int knightActivityScore = 0;
    
        while (knights != 0) {
            int square = Long.numberOfTrailingZeros(knights);
            // Bonus for knights in the center of the board
            if (Central.isCentral(square)) {
                knightActivityScore += 10;
            }
            // Penalty for knights on the edge of the board
            if (Central.isEdge(square)) {
                knightActivityScore -= 5;
            }
            // Bonus for knights with many possible moves
            int mobility = Long.bitCount(Moves.knightMoves(square) & ~pawns);
            knightActivityScore += mobility;
    
            knights &= knights - 1; // Clear lowest set bit
        }
    
        return knightActivityScore;
    }
    
    private static int evaluateBishopActivity(long bishops, long pawns) {
        int bishopActivityScore = 0;
    
        while (bishops != 0) {
            int square = Long.numberOfTrailingZeros(bishops);
            // Bonus for bishops controlling long diagonals
            int mobility = Long.bitCount(Moves.bishopMoves(square) & ~pawns);
            bishopActivityScore += mobility;
    
            bishops &= bishops - 1; // Clear lowest set bit
        }
    
        return bishopActivityScore;
    }
    
    private static int evaluateRookActivity(long rooks, long pawns) {
        int rookActivityScore = 0;
    
        while (rooks != 0) {
            int square = Long.numberOfTrailingZeros(rooks);
            // Bonus for rooks on open files
            if ((Files.getFileMask(square) & ~pawns) == 0) {
                rookActivityScore += 10;
            }
            // Bonus for rooks on semi-open files
            else if ((Files.getFileMask(square) & pawns) == 0) {
                rookActivityScore += 5;
            }
            // Bonus for rooks with many possible moves
            int mobility = Long.bitCount(Moves.rookMoves(square) & ~pawns);
            rookActivityScore += mobility;
    
            rooks &= rooks - 1; // Clear lowest set bit
        }
    
        return rookActivityScore;
    }
    
    private static int evaluateQueenActivity(long queens, long pawns) {
        int queenActivityScore = 0;
    
        while (queens != 0) {
            int square = Long.numberOfTrailingZeros(queens);
            // Bonus for queens with many possible moves
            int mobility = Long.bitCount(Moves.queenMoves(square) & ~pawns);
            queenActivityScore += mobility;
    
            queens &= queens - 1; // Clear lowest set bit
        }
    
        return queenActivityScore;
    }
    

    public static int ratePosition(long P,long N,long B,long R,long Q,long K, boolean isWhite){
        int position = 0;
        if (isWhite) {
            for (int i = 0; i < 64; i++) {
                if (((P>>i)&1)==1)
                    position += pawnBoard[i];
                else if (((R>>i)&1)==1)
                    position += rookBoard[i];
                else if (((N>>i)&1)==1)
                    position += knightBoard[i];
                else if (((B>>i)&1)==1)
                    position += bishopBoard[i];
                else if (((Q>>i)&1)==1)
                    position += queenBoard[i];
                else if (((K>>i)&1)==1){
                    position += (isAtEnd)?
                    kingEndBoard[i]:kingMidBoard[i];
                }
            }
        }
        else{
            for (int i = 0; i < 64; i++) {
                if (((P>>i)&1)==1)
                    position += pawnBoard[63-i];
                else if (((R>>i)&1)==1)
                    position += rookBoard[63-i];
                else if (((N>>i)&1)==1)
                    position += knightBoard[63-i];
                else if (((B>>i)&1)==1)
                    position += bishopBoard[63-i];
                else if (((Q>>i)&1)==1)
                    position += queenBoard[63-i];
                else if (((K>>i)&1)==1){
                    position += (isAtEnd)?
                    kingEndBoard[63-i]:kingMidBoard[63-i];
                }
            }
        }
        return position;
    }

    public static int evaluatePawnStructure(long WP, long BP) {
        int pawnStructureScore = 0;
    
        // Evaluate isolated pawns
        pawnStructureScore -= countIsolatedPawns(WP);
        pawnStructureScore += countIsolatedPawns(BP);
    
        // Evaluate doubled pawns
        pawnStructureScore -= countDoubledPawns(WP);
        pawnStructureScore += countDoubledPawns(BP);
    
        // Evaluate connected pawns
        pawnStructureScore += countConnectedPawns(WP);
        pawnStructureScore -= countConnectedPawns(BP);
    
        // Evaluate passed pawns
        pawnStructureScore += countPassedPawns(WP, BP);
    
        // Evaluate pawn islands
        pawnStructureScore -= countPawnIslands(WP);
        pawnStructureScore += countPawnIslands(BP);
    
        return pawnStructureScore;
    }
    
    private static int countIsolatedPawns(long pawns) {
        int isolatedPawns = 0;
        long pawnMask = pawns;
    
        while (pawnMask != 0) {
            int square = Long.numberOfTrailingZeros(pawnMask);
            // Check adjacent files for pawns
            if ((Files.getFileMask(square) & pawns) == 0) {
                isolatedPawns++;
            }
            pawnMask &= pawnMask - 1; // Clear lowest set bit
        }
    
        return isolatedPawns * -10; // Penalty for each isolated pawn
    }
    
    private static int countDoubledPawns(long pawns) {
        int doubledPawns = 0;
        long pawnMask = pawns;
        long fileOccupancy = 0;
    
        while (pawnMask != 0) {
            int square = Long.numberOfTrailingZeros(pawnMask);
            int file = Files.getFile(square);
            // Check if there are other pawns on the same file
            if ((fileOccupancy & (1L << file)) != 0) {
                doubledPawns++;
            }
            fileOccupancy |= (1L << file);
            pawnMask &= pawnMask - 1; // Clear lowest set bit
        }
    
        return doubledPawns * -5; // Penalty for each doubled pawn
    }
    
    private static int countConnectedPawns(long pawns) {
        int connectedPawns = 0;
        long pawnMask = pawns;
    
        while (pawnMask != 0) {
            int square = Long.numberOfTrailingZeros(pawnMask);
            int file = Files.getFile(square);
            // Check adjacent files for pawns
            if ((Files.getFileMask(square) & pawns) != 0) {
                connectedPawns++;
            }
            pawnMask &= pawnMask - 1; // Clear lowest set bit
        }
    
        return connectedPawns * 5; // Bonus for each connected pawn
    }
    
    private static int countPassedPawns(long WP, long BP) {
        int passedPawns = 0;
        long whitePassedPawns = WP;
        long blackPassedPawns = BP;
    
        while (whitePassedPawns != 0) {
            int square = Long.numberOfTrailingZeros(whitePassedPawns);
            if (!isPassed(square, BP)) {
                passedPawns++;
            }
            whitePassedPawns &= whitePassedPawns - 1; // Clear lowest set bit
        }
    
        while (blackPassedPawns != 0) {
            int square = Long.numberOfTrailingZeros(blackPassedPawns);
            if (!isPassed(square, WP)) {
                passedPawns++;
            }
            blackPassedPawns &= blackPassedPawns - 1; // Clear lowest set bit
        }
    
        return passedPawns * 10; // Bonus for each passed pawn
    }
    
    private static boolean isPassed(int square, long opponentPawns) {
        // Check if there are no opponent pawns in front or on adjacent files
        long adjacentFiles = Files.getFileMask(square) | (Files.getFileMask(square) >>> 1) | (Files.getFileMask(square) << 1);
        return (Files.getFileMask(square) & opponentPawns) == 0 && (adjacentFiles & opponentPawns) == 0;
    }
    
    private static int countPawnIslands(long pawns) {
        int pawnIslands = 0;
        long pawnMask = pawns;
    
        while (pawnMask != 0) {
            int square = Long.numberOfTrailingZeros(pawnMask);
            // Check if isolated from other pawns in any adjacent file
            if ((Files.getFileMask(square) & pawns) == 0) {
                pawnIslands++;
            }
            pawnMask &= pawnMask - 1; // Clear lowest set bit
        }
    
        return pawnIslands * -5; // Penalty for each pawn island
    }

    public static int evaluateKingSafety(Position p, boolean isWhite) {
        long ownKing = isWhite ? p.WK : p.BK;
        long ownPawns = isWhite ? p.WP : p.BP;
    
        int kingSafetyScore = 0;
    
        // Evaluate pawn shield
        kingSafetyScore += evaluatePawnShield(ownKing, ownPawns);
    
        // Add more king safety evaluation criteria here as needed...
    
        return kingSafetyScore;
    }
    
    private static int evaluatePawnShield(long king, long pawns) {
        int pawnShieldScore = 0;
        int[] shieldFiles = new int[] { Files.FILE_G, Files.FILE_H }; // Shield files (for White)
    
        while (king != 0) {
            int square = Long.numberOfTrailingZeros(king);
            int rank = Ranks.getRank(square);
            int file = Files.getFile(square);
    
            // Check if the king is behind the shield pawns
            if (rank == Ranks.RANK_1 && Arrays.binarySearch(shieldFiles, file) >= 0) {
                // Bonus for having shield pawns
                pawnShieldScore += 20;
            }
    
            king &= king - 1; // Clear lowest set bit
        }
    
        return pawnShieldScore;
    }    

    public static boolean isEndgame(long WQ, long BQ, int material){
        if (material < 1500) return true;
        for (int i = 0; i < 64; i++) {
            if (((WQ>>i)&1)==1||((BQ>>i)&1)==1)
                return false;
        }
        return true;
    }

    public static int evaluateEndgame(Position p, boolean isWhite) {
        long ownPawns = isWhite ? p.WP : p.BP;
        long ownKing = isWhite ? p.WK : p.BK;
        long opponentPawns = isWhite ? p.BP : p.WP;
        long opponentKing = isWhite ? p.BK : p.WK;

        int endgameScore = 0;

        // Evaluate king activity
        endgameScore += evaluateKingActivity(ownKing, ownPawns, opponentKing);

        // Evaluate pawn promotion potential
        endgameScore += evaluatePawnPromotion(ownPawns, opponentPawns, isWhite);

        // Add more endgame evaluation criteria here as needed...

        return endgameScore;
    }

    private static int evaluateKingActivity(long king, long pawns, long opponentKing) {
        int kingActivityScore = 0;
        int[] centralSquares = new int[] { Squares.E4, Squares.D4, Squares.E5, Squares.D5 }; // Central squares

        while (king != 0) {
            int square = Long.numberOfTrailingZeros(king);
            // Bonus for kings in the center of the board
            if (Arrays.binarySearch(centralSquares, square) >= 0) {
                kingActivityScore += 20;
            }
            // Bonus for kings with many possible moves
            int mobility = Long.bitCount(Moves.kingMoves(square) & ~pawns);
            kingActivityScore += mobility;

            // Bonus for active king near opponent's king
            int opponentKingSquare = Long.numberOfTrailingZeros(opponentKing);
            int distance = Math.max(Math.abs(Ranks.getRank(square) - Ranks.getRank(opponentKingSquare)),
                                    Math.abs(Files.getFile(square) - Files.getFile(opponentKingSquare)));
            if (distance <= 2) {
                kingActivityScore += 10;
            }

            king &= king - 1; // Clear lowest set bit
        }

        return kingActivityScore;
    }

    private static int evaluatePawnPromotion(long ownPawns, long opponentPawns, boolean isWhite) {
        int pawnPromotionScore = 0;

        // Bonus for passed pawns close to promotion
        while (ownPawns != 0) {
            int square = Long.numberOfTrailingZeros(ownPawns);
            if (isPassed(square, opponentPawns)) {
                // Evaluate proximity to promotion square
                int rank = Ranks.getRank(square);
                int promotionRank = isWhite ? Ranks.RANK_8 : Ranks.RANK_1;
                int promotionDistance = promotionRank - rank;
                pawnPromotionScore += 10 * promotionDistance;
            }
            ownPawns &= ownPawns - 1; // Clear lowest set bit
        }

        return pawnPromotionScore;
    }

}