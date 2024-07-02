import java.util.Arrays;

public class Moves {
    static final long AFILE = 0b0000000100000001000000010000000100000001000000010000000100000001L;
    static final long HFILE = 0b1000000010000000100000001000000010000000100000001000000010000000L;
    static final long ABFILES = 0b0000001100000011000000110000001100000011000000110000001100000011L;
    static final long GHFILES = 0b1100000011000000110000001100000011000000110000001100000011000000L;
    static final long RANK_1 = 0b1111111100000000000000000000000000000000000000000000000000000000L;
    static final long RANK_4 = 0b0000000000000000000000001111111100000000000000000000000000000000L;
    static final long RANK_5 = 0b0000000000000000000000000000000011111111000000000000000000000000L;
    static final long RANK_8 = 0b0000000000000000000000000000000000000000000000000000000011111111L;
    static final long CENTRE = 0b0000000000000000000000000001100000011000000000000000000000000000L;
    static final long EXTENDED_CENTRE = 0b0000000000000000001111000011110000111100001111000000000000000000L;
    static final long KING_SIDE = 0b1111000011110000111100001111000011110000111100001111000011110000L;
    static final long QUEEN_SIDE = 0b0000111100001111000011110000111100001111000011110000111100010000L;
    static final long KING_B7 = 0b1110000010100000111L;
    static final long KNIGHT_C6 = 0b101000010001000000000001000100001010L;
    static long NOT_WHITE_PIECES;
    static long BLACK_PIECES;
    static long OCCUPIED;
    static long EMPTY;
    static long RankMasks8[] =
    {
        0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
    };
    static long FileMasks8[] =
    {
        0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
        0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };
    static long DiagonalMasks8[] =
    {
	0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
	0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
	0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
    };
    static long AntiDiagonalMasks8[] =
    {
	0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
	0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
	0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
    };
    
    public static String possibleWhiteMoves(String history,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        NOT_WHITE_PIECES = ~(WP|WN|WB|WR|WQ|WK|BK);
        BLACK_PIECES = BP|BN|BB|BR|BQ;
        OCCUPIED = WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY = ~OCCUPIED;
        StringBuilder list= new StringBuilder(possibleP(history, WP, BP));
        list.append(possibleB(OCCUPIED, WB));
        
        return list.toString();
    }

    public static void drawBitboard(long bitBoard) {
        String[][] chessBoard=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]="";
        }
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if ("".equals(chessBoard[i/8][i%8])) {chessBoard[i/8][i%8]=" ";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

    

    private static String possibleP(String history, long WP, long BP) {
        StringBuilder list = new StringBuilder();
        long PAWN_MOVES = (WP>>7)&BLACK_PIECES&~RANK_8&~AFILE;
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1);
            list.append(i%8-1);
            list.append(i/8);
            list.append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES = (WP>>9)&BLACK_PIECES&~RANK_8&~HFILE;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1);
            list.append(i%8+1);
            list.append(i/8);
            list.append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES = (WP>>8)&EMPTY&~RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1);
            list.append(i%8);
            list.append(i/8);
            list.append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES = (WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i/8+2);
            list.append(i%8);
            list.append(i/8);
            list.append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }

        PAWN_MOVES = (WP>>7)&BLACK_PIECES&RANK_8&~AFILE;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i%8-1);
            list.append(i%8);
            list.append("QP");
            list.append(i%8-1);
            list.append(i%8);
            list.append("RP");
            list.append(i%8-1);
            list.append(i%8);
            list.append("BP");
            list.append(i%8-1);
            list.append(i%8);
            list.append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1); 
        }
        PAWN_MOVES = (WP>>9)&BLACK_PIECES&RANK_8&~HFILE;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i%8+1);
            list.append(i%8);
            list.append("QP");
            list.append(i%8+1);
            list.append(i%8);
            list.append("RP");
            list.append(i%8+1);
            list.append(i%8);
            list.append("BP");
            list.append(i%8+1);
            list.append(i%8);
            list.append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1); 
        }
        PAWN_MOVES = (WP>>8)&EMPTY&RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0) {    
            int i = Long.numberOfTrailingZeros(possibility);
            list.append(i%8);
            list.append(i%8);
            list.append("QP");
            list.append(i%8);
            list.append(i%8);
            list.append("RP");
            list.append(i%8);
            list.append(i%8);
            list.append("BP");
            list.append(i%8);
            list.append(i%8);
            list.append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1); 
        }
        int len = history.length();
        if (len>=4) {
            if ((history.charAt(len-1)==history.charAt(len-3)) && Math.abs(history.charAt(len-2)-history.charAt(len-4))==2) {
                int eFile=history.charAt(len-1)-'0';
                possibility = (WP<<1)&BP&RANK_5&~AFILE&FileMasks8[eFile];
                while (possibility !=0) {
                    int i = Long.numberOfTrailingZeros(possibility);
                    list.append(i%8-1);
                    list.append(i%8);
                    list.append(" E");
                }
                possibility = (WP>>1)&BP&RANK_5&~HFILE&FileMasks8[eFile];
                while (possibility !=0) {
                    int i = Long.numberOfTrailingZeros(possibility);
                    list.append(i%8+1);
                    list.append(i%8);
                    list.append(" E");
                }
            }
        }

        return list.toString();
    }

    static String possibleB(long OCCUPIED, long WB){
        StringBuilder list = new StringBuilder();
        
        long i = WB&~(WB-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = diagonals(loc)&NOT_WHITE_PIECES;
            long j = possibility&~(possibility-1);
            while (j != 0) {
                int index = Long.numberOfTrailingZeros(j);
                list.append(loc/8);
                list.append(loc%8);
                list.append(index/8);
                list.append(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WB&=~i;
            i=WB&~(WB-1);
        }       

        return list.toString();
    }

    static String possibleR(long OCCUPIED, long WR){
        StringBuilder list = new StringBuilder();
        
        long i = WR&~(WR-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = horizontalAndVertical(loc)&NOT_WHITE_PIECES;
            long j = possibility&~(possibility-1);
            while (j != 0) {
                int index = Long.numberOfTrailingZeros(j);
                list.append(loc/8);
                list.append(loc%8);
                list.append(index/8);
                list.append(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WR&=~i;
            i=WR&~(WR-1);
        }       

        return list.toString();
    }

    static String possibleQ(long OCCUPIED, long WQ){
        StringBuilder list = new StringBuilder();
        
        long i = WQ&~(WQ-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = (horizontalAndVertical(loc)|diagonals(loc))&NOT_WHITE_PIECES;
            long j = possibility&~(possibility-1);
            while (j != 0) {
                int index = Long.numberOfTrailingZeros(j);
                list.append(loc/8);
                list.append(loc%8);
                list.append(index/8);
                list.append(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WQ&=~i;
            i=WQ&~(WQ-1);
        }       

        return list.toString();
    }

    static long horizontalAndVertical(int a){
        long binary = 1L<<a;
        long horizontalPossibilities = (OCCUPIED - 2 * binary) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binary));
        long verticalPossibilities = ((OCCUPIED&FileMasks8[a % 8]) - 2 * binary) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks8[a % 8]) - 2 * Long.reverse(binary));
        return horizontalPossibilities&RankMasks8[a / 8] | verticalPossibilities&FileMasks8[a % 8];
    }

    static long diagonals(int a){
        long binary = 1L<<a;
        long diagonalPossibilities = ((OCCUPIED&DiagonalMasks8[a / 8  + a % 8]) - 2 * binary) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[a / 8 + a % 8]) - 2 * Long.reverse(binary));;
        long oppDiagonalPossibilities = ((OCCUPIED&AntiDiagonalMasks8[a / 8 + 7 - a % 8]) - 2 * binary) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[a / 8 + 7 - a % 8]) - 2 * Long.reverse(binary));
        return diagonalPossibilities&DiagonalMasks8[a / 8 + a % 8] | oppDiagonalPossibilities&AntiDiagonalMasks8[a / 8 + 7 - a % 8];
    }

    private static void timeExperiment(long WP) {
        int loopLen = 10000;
        long startTime = System.currentTimeMillis();
        tEMethodA(loopLen, WP);
        long endTime = System.currentTimeMillis();
        long total1 = endTime-startTime;
        System.out.println("First method: "+total1);
        startTime = System.currentTimeMillis();
        tEMethodB(loopLen, WP);
        endTime = System.currentTimeMillis();
        long total2 = endTime-startTime;
        System.out.println("Second method: "+total2);

    }

    private static void tEMethodA(int loopLen, long WP) {
        for (int loop = 0; loop < loopLen; loop++) {
        
        }
    }

    private static void tEMethodB(int loopLen, long WP) {
        for (int loop = 0; loop < loopLen; loop++) {
            
        }
    }

}
