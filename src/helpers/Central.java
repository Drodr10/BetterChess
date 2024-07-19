package helpers;

public class Central {
    private static final boolean[] centralSquares = {
        false, false, false, false, false, false, false, false, // Rank 1
        false, false, false, false, false, false, false, false, // Rank 2
        false, false, false,  true,  true, false, false, false,     // Rank 3
        false, false,  true,  true,  true,  true, false, false,     // Rank 4
        false, false,  true,  true,  true,  true, false, false,     // Rank 5
        false, false, false,  true,  true, false, false, false,     // Rank 6
        false, false, false, false, false, false, false, false, // Rank 7
        false, false, false, false, false, false, false, false  // Rank 8
    };

    

    private Central() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isCentral(int square) {
        if (square < 0 || square >= 64) {
            throw new IllegalArgumentException("Square index out of bounds.");
        }
        return centralSquares[square];
    }

    public static boolean isEdge(int square) {
        if (square < 0 || square >= 64) {
            throw new IllegalArgumentException("Square index out of range");
        }
        int rank = Ranks.getRank(square);
        int file = Files.getFile(square);
        // Check if the square is on the outermost ranks or files
        return rank == Ranks.RANK_1 || rank == Ranks.RANK_8 || file == Files.FILE_A || file == Files.FILE_H;
    }
}
