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
    static final long KING_SPAN = 0b1110000010100000111L;
    static final long KNIGHT_SPAN = 0b101000010001000000000001000100001010L;
    static long CASTLE_ROOKS[]={63,56,7,0};
    static long NOT_MY_PIECES;
    static long MY_PIECES;
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
    
    public static String possibleWhiteMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ){
        NOT_MY_PIECES = ~(WP|WN|WB|WR|WQ|WK|BK);
        MY_PIECES = WP|WN|WB|WR|WQ;
        OCCUPIED = WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY = ~OCCUPIED;
        StringBuilder list= new StringBuilder(possibleWP(WP, BP, EP)); 
        list.append(possibleB(WB));
        list.append(possibleR(WR));
        list.append(possibleQ(WQ));
        list.append(possibleN(WN));
        list.append(possibleK(WK));
        list.append(possibleCW(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, CWK, CWQ));
        
        return list.toString();
    }

    public static String possibleBlackMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ){
        NOT_MY_PIECES = ~(BP|BN|BB|BR|BQ|WK|BK);
        MY_PIECES = BP|BN|BB|BR|BQ;
        OCCUPIED = WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY = ~OCCUPIED;
        StringBuilder list= new StringBuilder(possibleBP(BP, WP, EP));
        list.append(possibleB(BB));
        list.append(possibleR(BR));
        list.append(possibleQ(BQ));
        list.append(possibleN(BN));
        list.append(possibleK(BK));
        list.append(possibleCB(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, CBK, CBQ));        
        
        return list.toString();
    }

    

    public static long unsafeForWhite(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        long unsafe=((BP<<7)&~HFILE);
        unsafe|=((BP<<9)&~AFILE);
        long possibility;
        long i=BN&~(BN-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            if (loc>18)
            {
                possibility=KNIGHT_SPAN<<(loc-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-loc);
            }
            if (loc%8<4)
            {
                possibility &=~GHFILES;
            }
            else {
                possibility &=~ABFILES;
            }
            unsafe |= possibility;
            BN&=~i;
            i=BN&~(BN-1);
        }

        long QB=BQ|BB;
        i=QB&~(QB-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            possibility=diagonals(loc);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }

        long QR=BQ|BR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            possibility=horizontalAndVertical(loc);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }

        int loc=Long.numberOfTrailingZeros(BK);
        if (loc>9)
        {
            possibility=KING_SPAN<<(loc-9);
        }
        else {
            possibility=KING_SPAN>>(9-loc);
        }
        if (loc%8<4)
        {
            possibility &=~GHFILES;
        }
        else {
            possibility &=~ABFILES;
        }
        unsafe |= possibility;
        return unsafe;
    }

    public static long unsafeForBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        long unsafe=((WP>>>7)&~AFILE);
        unsafe|=((WP>>>9)&~HFILE);
        long possibility;
        long i=WN&~(WN-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            if (loc>18)
            {
                possibility=KNIGHT_SPAN<<(loc-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-loc);
            }
            if (loc%8<4)
            {
                possibility &=~GHFILES;
            }
            else {
                possibility &=~ABFILES;
            }
            unsafe |= possibility;
            WN&=~i;
            i=WN&~(WN-1);
        }

        long QB=WQ|WB;
        i=QB&~(QB-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            possibility=diagonals(loc);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }

        long QR=WQ|WR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int loc=Long.numberOfTrailingZeros(i);
            possibility=horizontalAndVertical(loc);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }

        int loc=Long.numberOfTrailingZeros(WK);
        if (loc>9)
        {
            possibility=KING_SPAN<<(loc-9);
        }
        else {
            possibility=KING_SPAN>>(9-loc);
        }
        if (loc%8<4)
        {
            possibility &=~GHFILES;
        }
        else {
            possibility &=~ABFILES;
        }
        unsafe |= possibility;
        return unsafe;
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

    

    public static String possibleWP(long WP,long BP,long EP) {
        StringBuilder list = new StringBuilder();

        long PAWN_MOVES=(WP>>7)&NOT_MY_PIECES&OCCUPIED&~RANK_8&~AFILE;//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1).append(i%8-1).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&NOT_MY_PIECES&OCCUPIED&~RANK_8&~HFILE;//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1).append(i%8+1).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&~RANK_8;//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8+1).append(i%8).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8+2).append(i%8).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(WP>>7)&NOT_MY_PIECES&OCCUPIED&RANK_8&~AFILE;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8-1).append(i%8).append("QP").append(i%8-1).append(i%8).append("RP").append(i%8-1).append(i%8).append("BP").append(i%8-1).append(i%8).append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&NOT_MY_PIECES&OCCUPIED&RANK_8&~HFILE;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8+1).append(i%8).append("QP").append(i%8+1).append(i%8).append("RP").append(i%8+1).append(i%8).append("BP").append(i%8+1).append(i%8).append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8).append(i%8).append("QP").append(i%8).append(i%8).append("RP").append(i%8).append(i%8).append("BP").append(i%8).append(i%8).append("NP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,"WE"
        //en passant right
        possibility = (WP << 1)&BP&RANK_5&~AFILE&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8-1).append(i%8).append("WE");
        }
        //en passant left
        possibility = (WP >> 1)&BP&RANK_5&~HFILE&EP;
        if (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8+1).append(i%8).append("WE");
        }
       return list.toString();
    }
    public static String possibleBP(long BP,long WP,long EP) {
        StringBuilder list= new StringBuilder();
        
        long PAWN_MOVES=(BP<<7)&NOT_MY_PIECES&OCCUPIED&~RANK_1&~HFILE;//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8-1).append(i%8+1).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<9)&NOT_MY_PIECES&OCCUPIED&~RANK_1&~AFILE;//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8-1).append(i%8-1).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<8)&EMPTY&~RANK_1;//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8-1).append(i%8).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<16)&EMPTY&(EMPTY<<8)&RANK_5;//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i/8-2).append(i%8).append(i/8).append(i%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }

        PAWN_MOVES=(BP<<7)&NOT_MY_PIECES&OCCUPIED&RANK_1&~HFILE;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8+1).append(i%8).append("qP").append(i%8+1).append(i%8).append("rP").append(i%8+1).append(i%8).append("bP").append(i%8+1).append(i%8).append("nP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<9)&NOT_MY_PIECES&OCCUPIED&RANK_1&~AFILE;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8-1).append(i%8).append("qP").append(i%8-1).append(i%8).append("rP").append(i%8-1).append(i%8).append("bP").append(i%8-1).append(i%8).append("nP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<8)&EMPTY&RANK_1;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8).append(i%8).append("qP").append(i%8).append(i%8).append("rP").append(i%8).append(i%8).append("bP").append(i%8).append(i%8).append("nP");
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        
        //en passant right
        possibility = (BP >> 1)&WP&RANK_4&~HFILE&EP;
        if (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8+1).append(i%8).append("BE");
        }

        //en passant left
        possibility = (BP << 1)&WP&RANK_4&~AFILE&EP;
        if (possibility != 0)
        {
            int i=Long.numberOfTrailingZeros(possibility);
            list.append(i%8-1).append(i%8).append("BE");
        }
       return list.toString();
    }

    static String possibleB(long B){
        StringBuilder list = new StringBuilder();
        
        long i = B&~(B-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = diagonals(loc)&NOT_MY_PIECES;
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
            B&=~i;
            i=B&~(B-1);
        }       

        return list.toString();
    }

    static String possibleR(long R){
        StringBuilder list = new StringBuilder();
        
        long i = R&~(R-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = horizontalAndVertical(loc)&NOT_MY_PIECES;
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
            R&=~i;
            i=R&~(R-1);
        }       

        return list.toString();
    }

    static String possibleQ(long Q){
        StringBuilder list = new StringBuilder();
        
        long i = Q&~(Q-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            possibility = (horizontalAndVertical(loc)|diagonals(loc))&NOT_MY_PIECES;
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
            Q&=~i;
            i=Q&~(Q-1);
        }       

        return list.toString();
    }

    static String possibleN(long N){
        StringBuilder list = new StringBuilder();
        
        long i = N&~(N-1);
        long possibility;
        while (i != 0) {
            int loc = Long.numberOfTrailingZeros(i);
            if (loc > 18) {
                possibility = KNIGHT_SPAN<<(loc-18);
            }else{
                possibility = KNIGHT_SPAN>>(18-loc);
            }
            if (loc % 8 < 4) {
                possibility &= ~GHFILES&NOT_MY_PIECES;
            }
            else{
                possibility &= ~ABFILES&NOT_MY_PIECES;
            }
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
            N&=~i;
            i=N&~(N-1);
        }       

        return list.toString();
    }

    static String possibleK(long K){
        StringBuilder list = new StringBuilder();
        long possibility;
        int loc = Long.numberOfTrailingZeros(K);
        if (loc > 9) {
            possibility = KING_SPAN << (loc-9);
        }else{
            possibility = KING_SPAN >> (9-loc);
        }
        if (loc % 8 < 4) {
            possibility &= ~GHFILES&NOT_MY_PIECES;
        }
        else{
            possibility &= ~ABFILES&NOT_MY_PIECES;
        }
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
        return list.toString();
    }

    static String possibleCW(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,boolean CWK,boolean CWQ){
        StringBuilder list = new StringBuilder();
        long UNSAFE=unsafeForWhite(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
        if ((UNSAFE&WK)==0) {
            if (CWK&&(((1L<<CASTLE_ROOKS[0])&WR)!=0) && ((OCCUPIED|UNSAFE)&((1L<<61)|(1L<<62)))==0) {
                    list.append("7476");
                }
            
            if (CWQ&&(((1L<<CASTLE_ROOKS[1])&WR)!=0) && ((OCCUPIED|(UNSAFE&~(1L<<57)))&((1L<<57)|(1L<<58)|(1L<<59)))==0) {
                    list.append("7472");
                }
            
        }
        return list.toString();
    }

    static String possibleCB(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,boolean CBK,boolean CBQ) {
        StringBuilder list= new StringBuilder();
        long UNSAFE=unsafeForBlack(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
        if ((UNSAFE&BK)==0) {
            if (CBK&&(((1L<<CASTLE_ROOKS[2])&BR)!=0))
            {
                if (((OCCUPIED|UNSAFE)&((1L<<5)|(1L<<6)))==0) {
                    list.append("0406");
                }
            }
            if (CBQ&&(((1L<<CASTLE_ROOKS[3])&BR)!=0))
            {
                if (((OCCUPIED|(UNSAFE&~(1L<<1)))&((1L<<1)|(1L<<2)|(1L<<3)))==0) {
                    list.append("0402");
                }
            }
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
    
    public static long makeMove(long board, String move, char type) {
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='P') {//pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[1]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[0]);
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[6]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[7]);
            }
            if (type==move.charAt(2)) {board|=(1L<<end);} else {board&=~(1L<<start); board&=~(1L<<end);}
        } else if (move.charAt(3)=='E') {//en passant
            int start, end;
            if (move.charAt(2)=='W') {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[3]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[2]);
                board&=~(FileMasks8[move.charAt(1)-'0']&RankMasks8[3]);
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[4]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[5]);
                board&=~(FileMasks8[move.charAt(1)-'0']&RankMasks8[4]);
            }
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);}
        } else {
            System.out.print("ERROR: Invalid move type");
        }
        return board;
    }
    public static long makeMoveEP(long board,String move) {
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            if ((Math.abs(move.charAt(0)-move.charAt(2))==2)&&(((board>>>start)&1)==1)) {//pawn double push
                return FileMasks8[move.charAt(1)-'0'];
            }
        }
        return 0;
    }

    public static long makeMoveCastle(long rookBoard, long kingBoard, String move, char type) {
        int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
        if ((((kingBoard>>>start)&1)==1)&&(("0402".equals(move))||("0406".equals(move))||("7472".equals(move))||("7476".equals(move)))) {//'regular' move
            if (type=='R') {
                switch (move) {
                    case "7472": rookBoard&=~(1L<<CASTLE_ROOKS[1]); rookBoard|=(1L<<(CASTLE_ROOKS[1]+3));
                        break;
                    case "7476": rookBoard&=~(1L<<CASTLE_ROOKS[0]); rookBoard|=(1L<<(CASTLE_ROOKS[0]-2));
                        break;
                }
            } else {
                switch (move) {
                    case "0402": rookBoard&=~(1L<<CASTLE_ROOKS[3]); rookBoard|=(1L<<(CASTLE_ROOKS[3]+3));
                        break;
                    case "0406": rookBoard&=~(1L<<CASTLE_ROOKS[2]); rookBoard|=(1L<<(CASTLE_ROOKS[2]-2));
                        break;
                }
            }
        }
        return rookBoard;
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
