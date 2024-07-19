import java.security.*;
public class Zobrist {
    static long zArray[][][] = new long[2][6][64];
    static long zEnPassant[] = new long[8];
    static long zCastle[] = new long[4];
    static long zBlackMove;
    public static long random64Bit() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
    public static void zobristFillArray() {
        for (int color = 0; color < 2; color++)
        {
            for (int pieceType = 0; pieceType < 6; pieceType++)
            {
                for (int square = 0; square < 64; square++)
                {
                    zArray[color][pieceType][square] = random64Bit();
                }
            }
        }
        for (int column = 0; column < 8; column++)
        {
            zEnPassant[column] = random64Bit();
        }
        for (int i = 0; i < 4; i++)
        {
            zCastle[i] = random64Bit();
        }
        zBlackMove = random64Bit();
    }
    
    public static long getZobristHash(Position p, boolean WhiteToMove) {
        long returnZKey = 0;
        for (int square = 0; square < 64; square++)
        {
            if (((p.WP >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][0][square];
            }
            else if (((p.BP >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][0][square];
            }
            else if (((p.WN >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][1][square];
            }
            else if (((p.BN >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][1][square];
            }
            else if (((p.WB >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][2][square];
            }
            
            else if (((p.BB >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][2][square];
            }
            else if (((p.WR >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][3][square];
            }
            else if (((p.BR >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][3][square];
            }
            else if (((p.WQ >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][4][square];
            }
            else if (((p.BQ >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][4][square];
            }
            else if (((p.WK >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][5][square];
            }
            else if (((p.BK >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][5][square];
            }
        }
        for (int column = 0; column < 8; column++)
        {
            if (p.EP == Moves.FileMasks8[column])
            {
                returnZKey ^= zEnPassant[column];
            }
        }
        if (p.CWK)
            returnZKey ^= zCastle[0];
        if (p.CWQ)
            returnZKey ^= zCastle[1];
        if (p.CBK)
            returnZKey ^= zCastle[2];
        if (p.CBQ)
            returnZKey ^= zCastle[3];
        if (!WhiteToMove)
            returnZKey ^= zBlackMove;
        return returnZKey;
    }
    public static void testDistribution() {
        int sampleSize = 2000;
        int sampleSeconds = 10;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (sampleSeconds * 1000);
        int[] distArray;
        distArray = new int[sampleSize];
        while (System.currentTimeMillis() < endTime)
        {
            for (int i = 0; i < 10000; i++)
            {
                distArray[(int)(random64Bit()% (sampleSize / 2)) + (sampleSize / 2)]++;
            }
        }
        for (int i = 0; i < sampleSize; i++)
        {
            System.out.println(distArray[i]);
        }
    }
}