package helpers;

public class Files {
    public static final int FILE_A = 0;
    public static final int FILE_B = 1;
    public static final int FILE_C = 2;
    public static final int FILE_D = 3;
    public static final int FILE_E = 4;
    public static final int FILE_F = 5;
    public static final int FILE_G = 6;
    public static final int FILE_H = 7;

    private Files(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the file index (0-7) of a given square index (0-63).
     *
     * @param squareIndex The square index (0-63).
     * @return The file index (0-7).
     */
    public static int getFile(int squareIndex) {
        return squareIndex % 8;
    }

    /**
     * Get a bitboard mask representing the file of a given square.
     *
     * @param squareIndex The square index (0-63).
     * @return A long bitboard mask representing the file.
     */
    public static long getFileMask(int squareIndex) {
        return 0x0101010101010101L << (squareIndex % 8);
    }
}

