import java.util.Arrays;
import java.util.Random;

public class BoardGenerator {
    static long WP=0L, WN=0L, WB=0L, WR=0L, WQ=0L, WK=0L, BP=0L, BN=0L, BB=0L, BR=0L, BQ=0L, BK=0L;
    public static void initiateStandardChess() {
        String[][] board={
            {"r","n","b","q","k","b","n","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," ","p"," "," "},
            {" ","p"," "," ","B"," "," "," "},
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
        UI.WP=WP; UI.WN=WN; UI.WB=WB;
        UI.WR=WR; UI.WQ=WQ; UI.WK=WK;
        UI.BP=BP; UI.BN=BN; UI.BB=BB;
        UI.BR=BR; UI.BQ=BQ; UI.BK=BK;
        drawArray();
    }

    private static void drawArray() {
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
            if (((WR>>i)&1)==1)
                board[i/8][i%8] = "R";
            if (((WN>>i)&1)==1)
                board[i/8][i%8] = "N";
            if (((WB>>i)&1)==1)
                board[i/8][i%8] = "B";
            if (((WQ>>i)&1)==1)
                board[i/8][i%8] = "Q";
            if (((WK>>i)&1)==1)
                board[i/8][i%8] = "K";
            if (((BP>>i)&1)==1)
                board[i/8][i%8] = "p";
            if (((BR>>i)&1)==1)
                board[i/8][i%8] = "r";
            if (((BN>>i)&1)==1)
                board[i/8][i%8] = "n";
            if (((BB>>i)&1)==1)
                board[i/8][i%8] = "b";
            if (((BQ>>i)&1)==1)
                board[i/8][i%8] = "q";
            if (((BK>>i)&1)==1)
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
