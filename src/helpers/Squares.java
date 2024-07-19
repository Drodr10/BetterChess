package helpers;

public class Squares {
    // Constants representing square indices
    public static final int A1 = 0, B1 = 1, C1 = 2, D1 = 3, E1 = 4, F1 = 5, G1 = 6, H1 = 7;
    public static final int A2 = 8, B2 = 9, C2 = 10, D2 = 11, E2 = 12, F2 = 13, G2 = 14, H2 = 15;
    public static final int A3 = 16, B3 = 17, C3 = 18, D3 = 19, E3 = 20, F3 = 21, G3 = 22, H3 = 23;
    public static final int A4 = 24, B4 = 25, C4 = 26, D4 = 27, E4 = 28, F4 = 29, G4 = 30, H4 = 31;
    public static final int A5 = 32, B5 = 33, C5 = 34, D5 = 35, E5 = 36, F5 = 37, G5 = 38, H5 = 39;
    public static final int A6 = 40, B6 = 41, C6 = 42, D6 = 43, E6 = 44, F6 = 45, G6 = 46, H6 = 47;
    public static final int A7 = 48, B7 = 49, C7 = 50, D7 = 51, E7 = 52, F7 = 53, G7 = 54, H7 = 55;
    public static final int A8 = 56, B8 = 57, C8 = 58, D8 = 59, E8 = 60, F8 = 61, G8 = 62, H8 = 63;

    // Algebraic notation mapping
    private static final String[] algebraicFiles = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] algebraicRanks = {"1", "2", "3", "4", "5", "6", "7", "8"};

    private Squares() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Convert algebraic notation (e.g., "a1", "h8") to square index (0-63).
     *
     * @param algebraicSquare Algebraic notation of the square (e.g., "e4").
     * @return The square index (0-63).
     */
    public static int algebraicToIndex(String algebraicSquare) {
        if (algebraicSquare.length() != 2) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + algebraicSquare);
        }
        char file = algebraicSquare.charAt(0);
        char rank = algebraicSquare.charAt(1);
        int fileIndex = file - 'a';
        int rankIndex = rank - '1';
        return rankIndex * 8 + fileIndex;
    }

    /**
     * Convert square index (0-63) to algebraic notation (e.g., "e4").
     *
     * @param squareIndex The square index (0-63).
     * @return Algebraic notation of the square (e.g., "e4").
     */
    public static String indexToAlgebraic(int squareIndex) {
        if (squareIndex < 0 || squareIndex > 63) {
            throw new IllegalArgumentException("Invalid square index: " + squareIndex);
        }
        int fileIndex = squareIndex % 8;
        int rankIndex = squareIndex / 8;
        return algebraicFiles[fileIndex] + algebraicRanks[rankIndex];
    }

    /**
     * Get a bitboard mask representing a single square.
     *
     * @param squareIndex The square index (0-63).
     * @return A long bitboard mask representing the square.
     */
    public static long getSquareMask(int squareIndex) {
        return 1L << squareIndex;
    }
}
