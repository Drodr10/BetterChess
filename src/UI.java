import javax.swing.*;
import java.awt.*;
import java.awt.event.*;;

public class UI extends JPanel {
    static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
    static long UniversalWP=0L,UniversalWN=0L,UniversalWB=0L,UniversalWR=0L,UniversalWQ=0L,UniversalWK=0L,UniversalBP=0L,UniversalBN=0L,UniversalBB=0L,UniversalBR=0L,UniversalBQ=0L, UniversalBK=0L;
    static int rating=0;
    static int border=10;
    static int humanColor = 1;
    static double squareSize = 60;
    static JFrame frame = new JFrame("The Chess Decimator");
    static UI chessUI = new UI();
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(chessUI);
        frame.setSize(730,540);
        frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-frame.getWidth())/2,
        (Toolkit.getDefaultToolkit().getScreenSize().height-frame.getHeight())/2);
        frame.setVisible(true);
        newGame();
        frame.repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize=(double)(Math.min(getHeight(), getWidth()-200-border)-2*border)/8;
            }
        });
        drawBorders(g);
        drawBoard(g);
        drawPieces(g);
    }
    public void drawPieces(Graphics g) {
        Image chessPieceImage;
        chessPieceImage=new ImageIcon("lib/ChessPiecesArray.png").getImage();
        for (int i=0;i<64;i++) {
            int j=-1,k=-1;
            if (((WP>>i)&1)==1) {j=5; k=humanColor;}
            else if (((BP>>i)&1)==1) {j=5; k=1-humanColor;}
            else if (((WB>>i)&1)==1) {j=4;k=humanColor;}
            else if (((BB>>i)&1)==1) {j=4;k=1-humanColor;}
            else if (((WN>>i)&1)==1) {j=3;k=humanColor;}
            else if (((BN>>i)&1)==1) {j=3;k=1-humanColor;}
            else if (((WQ>>i)&1)==1) {j=0;k=humanColor;}
            else if (((BQ>>i)&1)==1) {j=0;k=1-humanColor;}
            else if (((WR>>i)&1)==1) {j=2;k=humanColor;}
            else if (((BR>>i)&1)==1) {j=2;k=1-humanColor;}
            else if (((WK>>i)&1)==1) {j=1;k=humanColor;}
            else if (((BK>>i)&1)==1) {j=1;k=1-humanColor;}
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPieceImage, (int)((i%8)*squareSize)+border, (int)((i/8)*squareSize)+border, (int)((i%8+1)*squareSize)+border, (int)((i/8+1)*squareSize)+border, j*60, k*60, (j+1)*60, (k+1)*60, this);
            }
        }
    }
    public void drawBorders(Graphics g) {
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+border, border, border, (int)(8*squareSize), true);
        g.fill3DRect(border, 0, (int)(8*squareSize), border, true);
        g.fill3DRect(border, (int)(8*squareSize)+border, (int)(8*squareSize), border, true);
        
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, 0, border, border, true);
        g.fill3DRect(0, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, (int)(8*squareSize)+border, border, border, true);
        
        g.setColor(new Color(0,100,0));
        g.fill3DRect((int)(8*squareSize)+2*border, 0, 200, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+2*border, (int)(8*squareSize)+border, 200, border, true);
    }
    public void drawBoard(Graphics g) {
        for (int i=0;i<64;i+=2) {
            g.setColor(new Color(255, 200, 100));
            g.fillRect((int)((i%8+(i/8)%2)*squareSize)+border, (int)((i/8)*squareSize)+border, (int)squareSize, (int)squareSize);
            g.setColor(new Color(150, 50, 30));
            g.fillRect((int)(((i+1)%8-((i+1)/8)%2)*squareSize)+border, (int)(((i+1)/8)*squareSize)+border, (int)squareSize, (int)squareSize);
        }
    }
    
    public static void newGame() {
        final int Standard = 0;
        final int Chess960 = 1;
        Object[] option = {"Standard Chess", "Chess 960"};
        int choice = -1;
        choice = JOptionPane.showOptionDialog(null, "Do you want to play standard chess or chess 960?", "Game Type", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if (choice == Standard) {
            BoardGenerator.initiateStandardChess();
        }else if (choice == Chess960){
            BoardGenerator.initiateChess960();
        }else{
            JOptionPane.showMessageDialog(null, "Please select a choice!", "Warning", JOptionPane.ERROR_MESSAGE);
            newGame();
            return;
        }
        Object[] options = {"Computer", "Human"};
        humanColor = JOptionPane.showOptionDialog(null, "Who should play as white?", "Computer Settings", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        Moves.possibleWhiteMoves("", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

    }
}
