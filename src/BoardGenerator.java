import java.util.Arrays;
import java.util.Random;

public class BoardGenerator {
    static long WP=0L, WN=0L, WB=0L, WR=0L, WQ=0L, WK=0L, BP=0L, BN=0L, BB=0L, BR=0L, BQ=0L, BK=0L;
    public static void initiateStandardChess() {
        String[][] board={
            {"r","n","b","q","k","b","n","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","N","B","Q","K","B","N","R"}
        };
        arrayToBitboards(board);
    }

    public static void initiateChess960() {
        String[][] board = {
            {" "," "," "," "," "," "," "," "},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {" "," "," "," "," "," "," "," "}
            };
        Random random = new Random();
        int rand1 = random.nextInt(8);
        board[0][rand1] = "b";
        board[7][rand1] = "B";
        int rand2 ,rand3, rand4a, rand4b;
        do{
            rand2 = random.nextInt(8);
        }
        while(rand1%2==rand2%2);
        board[0][rand2] = "b";
        board[7][rand2] = "B";
        do{
            rand3 = random.nextInt(8);
        }
        while(rand1==rand3||rand3==rand2);
        board[0][rand3] = "q";
        board[7][rand3] = "Q";
        
        rand4a = random.nextInt(5);
        int counter=0,loop=0;
        while (counter-1<rand4a) {
            if (" ".equals(board[0][loop])) {
                counter++;
            }
            loop++;
        }
        board[0][loop-1] = "n";
        board[7][loop-1] = "N";

        rand4b = random.nextInt(4);
        counter=0;
        loop=0;
        while (counter-1<rand4b) {
            if (" ".equals(board[0][loop])) {
                counter++;
            }
            loop++;
        }
        board[0][loop-1] = "n";
        board[7][loop-1] = "N";

        counter = 0;
        while (!" ".equals(board[0][counter])) {
            counter++;
        }
        board[0][counter] = "r";
        board[7][counter] = "R";

        while (!" ".equals(board[0][counter])) {
            counter++;
        }
        board[0][counter] = "k";
        board[7][counter] = "K";
        while (!" ".equals(board[0][counter])) {
            counter++;
        }
        board[0][counter] = "r";
        board[7][counter] = "R";
        arrayToBitboards(board);
    }

    public static void importFEN(String FEN){
        Operator.WP=Operator.WN=Operator.WB=Operator.WR=Operator.WQ=Operator.WK=Operator.BP=Operator.BN=Operator.BB=Operator.BR=Operator.BQ=Operator.BK=0;
        Operator.CWK = Operator.CWQ = Operator.CBK = Operator.CBQ = false ;
        int charI = 0;
        int boardI = 0;
        while (FEN.charAt(charI) != ' ')
        {
            switch (FEN.charAt(charI++))
            {
            case 'P': Operator.WP |= (1L << boardI++);
                break;
            case 'p': Operator.BP |= (1L << boardI++);
                break;
            case 'N': Operator.WN |= (1L << boardI++);
                break;
            case 'n': Operator.BN |= (1L << boardI++);
                break;
            case 'B': Operator.WB |= (1L << boardI++);
                break;
            case 'b': Operator.BB |= (1L << boardI++);
                break;
            case 'R': Operator.WR |= (1L << boardI++);
                break;
            case 'r': Operator.BR |= (1L << boardI++);
                break;
            case 'Q': Operator.WQ |= (1L << boardI++);
                break;
            case 'q': Operator.BQ |= (1L << boardI++);
                break;
            case 'K': Operator.WK |= (1L << boardI++);
                break;
            case 'k': Operator.BK |= (1L << boardI++);
                break;
            case '/':
                break;
            case '1': boardI++;
                break;
            case '2': boardI += 2;
                break;
            case '3': boardI += 3;
                break;
            case '4': boardI += 4;
                break;
            case '5': boardI += 5;
                break;
            case '6': boardI += 6;
                break;
            case '7': boardI += 7;
                break;
            case '8': boardI += 8;
                break;
            default:
                break;
            }
        }
        Operator.whiteToMove = (FEN.charAt(++charI) == 'w');
        charI += 2;
        while (FEN.charAt(charI) != ' ')
        {
            switch (FEN.charAt(charI++))
            {
            case '-':
                break;
            case 'K': Operator.CWK = true;
                break;
            case 'Q': Operator.CWQ = true;
                break;
            case 'k': Operator.CBK = true;
                break;
            case 'q': Operator.CBQ = true;
                break;
            default:
                break;
            }
        }
        if (FEN.charAt(++charI) != '-')
        {
            Operator.EP = Moves.FileMasks8[FEN.charAt(charI++) - 'a'];
        }
        Operator.position = new Position(Operator.WP,Operator.WN,Operator.WB,Operator.WR,Operator.WQ,Operator.WK,Operator.BP,Operator.BN,Operator.BB,Operator.BR,Operator.BQ,Operator.BK,Operator.EP,Operator.CWK,Operator.CWQ,Operator.CBK,Operator.CBQ);
    }

    public static void arrayToBitboards(String[][] board){
        long binary = 0b01L;
        for (int i = 0; i < 64; i++) {
            switch(board[i/8][i%8]){
                case "P":  
                    WP+=binary;
                    break;
                case "R":
                    WR+=binary;
                    break;
                case "N":
                    WN+=binary;
                    break;
                case "B":
                    WB+=binary;
                    break;
                case "Q":
                    WQ+=binary;
                    break;
                case "K":
                    WK+=binary;
                    break;
                case "p":  
                    BP+=binary;
                    break;
                case "r":
                    BR+=binary;
                    break;
                case "n":
                    BN+=binary;
                    break;
                case "b":
                    BB+=binary;
                    break;
                case "q":
                    BQ+=binary;
                    break;
                case "k":
                    BK+=binary;
                    break;
                default:
                    break;
            }
            binary <<= 1;
        }
        Operator.WP=WP; Operator.WN=WN; Operator.WB=WB;
        Operator.WR=WR; Operator.WQ=WQ; Operator.WK=WK;
        Operator.BP=BP; Operator.BN=BN; Operator.BB=BB;
        Operator.BR=BR; Operator.BQ=BQ; Operator.BK=BK;
        Operator.position = new Position(Operator.WP,Operator.WN,Operator.WB,Operator.WR,Operator.WQ,Operator.WK,Operator.BP,Operator.BN,Operator.BB,Operator.BR,Operator.BQ,Operator.BK,Operator.EP,Operator.CWK,Operator.CWQ,Operator.CBK,Operator.CBQ);
    }

    public static void drawArray(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String[][] board = {
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "}
        };

        for (int i = 0; i < 64; i++) {
            if (((WP>>i)&1)==1)
                board[i/8][i%8] = "P";
            else if (((WR>>i)&1)==1)
                board[i/8][i%8] = "R";
            else if (((WN>>i)&1)==1)
                board[i/8][i%8] = "N";
            else if (((WB>>i)&1)==1)
                board[i/8][i%8] = "B";
            else if (((WQ>>i)&1)==1)
                board[i/8][i%8] = "Q";
            else if (((WK>>i)&1)==1)
                board[i/8][i%8] = "K";
            else if (((BP>>i)&1)==1)
                board[i/8][i%8] = "p";
            else if (((BR>>i)&1)==1)
                board[i/8][i%8] = "r";
            else if (((BN>>i)&1)==1)
                board[i/8][i%8] = "n";
            else if (((BB>>i)&1)==1)
                board[i/8][i%8] = "b";
            else if (((BQ>>i)&1)==1)
                board[i/8][i%8] = "q";
            else if (((BK>>i)&1)==1)
                board[i/8][i%8] = "k";
        }
        printBoard(board);
    }
    public static void printBoard(String[][] board){
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}
