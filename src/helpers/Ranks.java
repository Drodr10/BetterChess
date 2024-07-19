package helpers;

public class Ranks {
    public static final int RANK_1 = 0;
    public static final int RANK_2 = 1;
    public static final int RANK_3 = 2;
    public static final int RANK_4 = 3;
    public static final int RANK_5 = 4;
    public static final int RANK_6 = 5;
    public static final int RANK_7 = 6;
    public static final int RANK_8 = 7;

    private Ranks() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the rank index (0-7) of a given square index (0-63).
     *
     * @param squareIndex The square index (0-63).
     * @return The rank index (0-7).
     */
    public static int getRank(int squareIndex) {
        return squareIndex / 8;
    }
}

