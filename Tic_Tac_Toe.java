import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/* Indexing
There are 9 squares in tic tac toe
In our game they are represented in the front end with 9 jbuttons labled 1-9
In the back end they are represented by a 2D array with the 1st position being the x coordinate and the second being the y
The origin is placed at the top left corner of the grid of buttons
This means that the y values start at the top with 0 and end with 2 at the bottom
The cells start with 1 at the top left and read left to right then up to down
Example: cell 4 is the second from the top and furthest to the left, it is represented by the coordinates, (0, 1)
*/


public class Tic_Tac_Toe {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws ArrayIndexOutOfBoundsException
    {
        // boolean value=false;
        String[] Options = getOptions();
    }

    @SuppressWarnings("deprecation")
    public static void game(String[] Options) {
        JFrame f = new JFrame("Tic Tac Toe!"); //Initializing the JFrame and making it maximzed to the current screen
        f.setExtendedState(f.MAXIMIZED_BOTH);
        f.setVisible(true);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true);
        f.setCursor(0);
        f.setDefaultLookAndFeelDecorated(true);
        f.setSize((int) (f.getWidth() * 0.80), (int) (f.getHeight() * 0.80));
        f.setLocationRelativeTo(null);
        f.setExtendedState(f.MAXIMIZED_BOTH);
        f.setCursor(3);

        f.setBackground(Color.green); // changes the header of the JFrame only on macs for some reason

        //variables used later for determining which player is which symbol
        boolean isWinner = false;
        boolean isX;

        String ally;
        String opponent;
        String gamemode = Options[0];

        if (gamemode == "AI") // Options[0] is Gamemode
        { // Options[1] is player 1
            if (Options[1] == "X") {
                isX = true;
                ally = "X";
                opponent = "O";
            } else {
                isX = false;
                ally = "O";
                opponent = "X";
            }
        } else // assumes gamemode is 1v1
        {
            // System.out.println(Options[2]);
            if (Options[2] == "X First") // if X will go first.
            {
                ally = "X";
                opponent = "O";
                isX = true;
            } else // if O will go first.
            {
                ally = "O";
                opponent = "X";
                isX = false;
            }
        }

        // Debugging stuff

        String[][] board = new String[3][3]; // Declaring the board, the back-end representation of the game
        ImageIcon img = new ImageIcon("C:\\download.png");
        f.setIconImage(img.getImage());

        KnotsAndCrosses kandc = new KnotsAndCrosses(); // the knots and crosses object

        JButton[] cell = new JButton[9];
        for (int i = 0; i < cell.length; i++) cell[i] = new JButton("");

        JButton Reset = new JButton("");//As well as other text that goes on the JFrame
        JButton help = new JButton("Help");
        JLabel Turn = new JLabel("");
        JLabel Win = new JLabel("");

        Random rand = new Random(); // For starting positions

        clearboard(board, cell);
        JLabel Wins = new JLabel(""); // More text on the screen
        int moves = 0;

        help.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = { "Change Gamemode", "Nevermind lol", "Close Game" };
                int n = JOptionPane.showOptionDialog(f, "Ya?", "Whatdya want", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == JOptionPane.CANCEL_OPTION) {
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                } else if (n == JOptionPane.YES_OPTION) {
                    clearboard(board, cell);
                    // message(f);
                } else if (n == JOptionPane.NO_OPTION) {
                }
            }
        });
        for (int i = 0; i < cell.length; i++) f.add(cell[i]); //Adding all needed buttons and text to JFrame

        f.add(Reset); // Adds all non board buttons and labels.
        f.add(Turn);
        f.add(Win);
        f.add(Wins);

        Color y = new Color(255, 233, 55); // Yellow
        Color g = new Color(76, 187, 23); // Green
        Color v = new Color(127,0,255);  //violet
        Color o = new Color(255,165,0); //orange
        Color fg = new Color(34,139,34);//forest green
        Color b = new Color(0,0,255); //blue
        Color sb = new Color(130,182,217); //skyblue

        Color color1=g;  //color 1
        Color color2=y;  //color 2 will be put in checkered pattern
        f.getContentPane().setBackground(sb); // sets the background to a pale dark blue

        cell[0].setBackground(color1);
        cell[1].setBackground(color2);
        cell[2].setBackground(color1);
        cell[3].setBackground(color2);
        cell[4].setBackground(color1);
        cell[5].setBackground(color2);
        cell[6].setBackground(color1);
        cell[7].setBackground(color2);
        cell[8].setBackground(color1);
        Reset.setBackground(color1);
        help.setBackground(color2);

        rescale(cell, f, Reset, Turn, Win, Wins); //Scales the cells to the current screen size
        f.addWindowStateListener(new WindowStateListener() { // when the smaller window button gets pressed we scale the cells to fit
            @Override
            public void windowStateChanged(WindowEvent e) {
                rescale(cell, f, Reset, Turn, Win, Wins);
            }
        });
        char whoGoFirst = Options[2].charAt(0);
        char humanPlayer = Options[1].charAt(0);
        if (gamemode == "AI" && humanPlayer != whoGoFirst) // If AI Mode and human going 2nd
        {
            int[] startingAIMove = startingPositions(rand);
            AIMove(kandc, board, cell, startingAIMove,
                    opponent, gamemode, whoGoFirst, humanPlayer,Turn);
        }

        Reset.setText("Reset Board");
        Reset.addActionListener(new ActionListener() { // listening for when the reset button is pressed, then it clears the board on the front and back-end
            @Override
            public void actionPerformed(ActionEvent e) {
                clearboard(board, cell);
                Win.setText("");
                checkplayer(Turn, true, isX, ally, opponent);
                if (gamemode == "AI" && humanPlayer != whoGoFirst) // If AI Mode and human going 2nd
                {
                    int[] startingAIMove = startingPositions(rand);
                    AIMove(kandc, board, cell, startingAIMove,
                            opponent, gamemode, whoGoFirst, humanPlayer,Turn);
                }
            }
        });


        ActionListener e = (ActionListener) new ActionListener()
        {
            boolean player = true; // true = first player
            int XWins = 0; // false = second player/AI
            int YWins = 0;
            boolean win = false;

            public void actionPerformed(ActionEvent e) //when a button gets pressed, the proper text is placed in the cell and on the board
            {
                Object obj = e.getSource();
                if (obj == cell[0]) // 0,0
                {
                    player = move(cell[0], player, board, 0, 0, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[1]) // 1,0
                {
                    player = move(cell[1], player, board, 1, 0, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[2]) // 2,0
                {
                    player = move(cell[2], player, board, 2, 0, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[3]) // 0,1
                {
                    player = move(cell[3], player, board, 0, 1, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[4]) // 1,1
                {
                    player = move(cell[4], player, board, 1, 1, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[5]) // 2,1
                {
                    player = move(cell[5], player, board, 2, 1, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[6]) // 0,2
                {
                    player = move(cell[6], player, board, 0, 2, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[7]) // 1,2
                {
                    player = move(cell[7], player, board, 1, 2, gamemode, isX, ally, opponent, Options);
                }
                else if (obj == cell[8]) // 2,2
                {
                    player = move(cell[8], player, board, 2, 2, gamemode, isX, ally, opponent, Options);
                }
                // Below are constants, that happen every time
                // somebody makes a move.
                // System.out.print(player); prints out the move.
                win = checkWinner();
                if (gamemode == "AI") // For AI Mode
                {
                    if (!player) // player 2 or AI
                    {
                        int x[] = bestMove(kandc, board, moves, ally, opponent, isX);
                        AIMove(kandc, board, cell, x, opponent,
                                gamemode, whoGoFirst, humanPlayer,Turn);
                        player = !player;
                    }
                }
                win = checkWinner();
                if (!positionsLeft(kandc, board) && win != true)
                {
                    Turn.setText("Tie what");
                    disableBoard(cell);
                    player = true;
                }
                if (cell[0].isEnabled())
                {
                    player = checkplayer(Turn, player, isX, ally, opponent);
                }
                Wins.setText("X:" + XWins + " Wins. O:" + YWins + " Wins.");
                // 1101111011
            }

            private boolean checkWinner()
            {
                if (kandc.checkWinner("O", board) == true)
                {
                    Win.setText("You did it, O!");
                    Turn.setText("");
                    YWins++;
                    winLine(cell, kandc, board, "O");
                    player = true;
                    return true;
                }
                else if (kandc.checkWinner("X", board) == true)
                {
                    Win.setText("Good job, X");
                    Turn.setText("");
                    XWins++;
                    winLine(cell, kandc, board, "X");
                    player = true;
                    return true;
                }
                return false;
            }
        };

        cell[0].addActionListener((ActionListener) e);// Adds action listener to all buttons
        cell[1].addActionListener((ActionListener) e);
        cell[2].addActionListener((ActionListener) e);
        cell[3].addActionListener((ActionListener) e);
        cell[4].addActionListener((ActionListener) e);
        cell[5].addActionListener((ActionListener) e);
        cell[6].addActionListener((ActionListener) e);
        cell[7].addActionListener((ActionListener) e);
        cell[8].addActionListener((ActionListener) e);
    }

    public static void error() //when a cell is pressed that already has text in it, the error box pops up
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, "This spot is already taken!", "Woah Buddy", JOptionPane.ERROR_MESSAGE);
    }
    public static String[][] clearboard(String[][] board, JButton[] cell) {
        for (int i = 0; i <= 2; i++) // Changing all values to "Why", because we needed to have some text in the cells
        {
            for (int j = 0; j <= 2; j++) {
                board[j][i] = "why";
            }
        }
        ArrayList<JButton> cells = new ArrayList<JButton>(); //adds all the buttons to a list
        for (int i = 0; i < cell.length; i++) {
            cells.add(cell[i]);
        }

        for(int i = 0; i < 9; i++){ // cycles through the list and sets the cells to be enabled, sets the text as clear, and the foreground to black
            cells.get(i).setText("");
            cells.get(i).setEnabled(true);
            cells.get(i).setForeground(Color.BLACK);
        }
        return board;
    }
    public static boolean checkplayer(JLabel Turn, boolean player, boolean isX, String ally, String opponent) {
        if (player) {
            Turn.setText("Take your time," + ally);
        } else {
            Turn.setText("Take your time," + opponent);
        }
        return player;
    }
    public static int[] startingPositions(Random rand) throws ArrayIndexOutOfBoundsException {
        // takes a random number and assigns it either one of the 4 corrners or the middle cell
        int[] startCoords = new int[2];
        startCoords[0] = -1;
        startCoords[1] = -1;
        int starter = rand.nextInt(5);
        // System.out.println(starter);
        if (starter == 0) {
            startCoords[0] = 0;
            startCoords[1] = 0;
        } else if (starter == 1) {
            startCoords[0] = 0;
            startCoords[1] = 2;
        } else if (starter == 2) {
            startCoords[0] = 2;
            startCoords[1] = 0;
        } else if (starter == 3) {
            startCoords[0] = 2;
            startCoords[1] = 2;
        } else if (starter == 4) {
            startCoords[0] = 1;
            startCoords[1] = 1;
        }
        return startCoords;
    }
    public static boolean positionsLeft(KnotsAndCrosses kandc, String[][] board) throws ArrayIndexOutOfBoundsException {
        //checks if there has been a move made in any of the cells
        for (int c = 0; c <= 2; c++) {
            for (int d = 0; d <= 2; d++) {
                if (board[c][d] == "why") {
                    return true;
                }
            }
        }
        return false;
    }

    // AI Stuff Start
    public static int checkWinnerConverted (KnotsAndCrosses kandc, String [][] board, String ally, String opponent, int depth, boolean isX){
        if (isX = true){
            ally = "X";
            opponent = "O";
        }else if(isX = false){
            ally = "O";
            opponent = "X";
        }
        int winScore = 0;
        if (kandc.checkWinner(ally , board)==true){
            winScore = 10 + depth;
        }else if (kandc.checkWinner(opponent, board)==true){
            winScore = -10 - depth;
        }
        return winScore;
    }
    // this is a method that uses the minimax algorithm to calculate the optimal value for the given board position
    public static int AI (KnotsAndCrosses kandc,String [][] board,boolean maximizer, int depth, String ally, String opponent, boolean isX){
        if (isX = true){
            ally = "X";
            opponent = "O";
        }else if(isX = false){
            ally = "O";
            opponent = "X";
        }
        //The function will terminate if either, the winnerValue is positive, indicating a win for ally
        //If the depth has reached its max, primarily used as a saftey net
        //Or if there are no positions left on the board
        int winnerValue = checkWinnerConverted(kandc, board, ally, opponent, depth, isX);
        if (Math.abs(winnerValue) > 0 || depth==0 || !checkBoard(board)){
            return winnerValue;
        }
        int maxScore;
        if (maximizer == true) { //checks if the player is a trying to find the maximum value
            maxScore = Integer.MIN_VALUE;
            for (int i = 0; i <=2; i++){ // search all the board positions to find moves left to be made
                for (int j = 0; j <=2; j++){
                    if (board [i][j] == "why" ){// if it finds a move that has not been made, it makes a move in that square for the maximizing player
                        board[i][j] = ally;
                        //calculates the maximum value of our default value which is set to the minimum value of an integer, and the return of AI
                        //it also lowers the depth
                        maxScore = Math.max(maxScore, AI(kandc, board, false, depth-1, ally, opponent, isX));
                        board[i][j] = "why"; // after we have found the value of the move, we undo it as to not have the cells be different from the board
                    }
                }
            }
            return maxScore;
        }else { // this does the same thing as the maximum player but instead tries to find the minimum value, still reduces the board by 1
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i <=2; i++) {
                for (int j = 0; j <=2; j++){
                    if (board [i][j]== "why"){
                        board [i][j] = opponent;
                        minScore = Math.min(minScore, AI(kandc, board, true, depth-1, ally, opponent, isX));
                        board [i][j] = "why";
                    }
                }
            }
            return minScore;
        }
    }
    // a method that uses AI to find the value of each move avalible
    // returns the coordinates of the said best move
    public static int [] bestMove (KnotsAndCrosses kandc,String [][] board, int depth, String ally, String opponent, boolean isX) throws ArrayIndexOutOfBoundsException {
        if (isX = true){
            ally = "X";
            opponent = "O";
        }else if(isX = false){
            ally = "O";
            opponent = "X";
        }
        int [] bestMoveCoords= new int [2];
        bestMoveCoords [0] = -1;
        bestMoveCoords [1] = -1;
        int highScore = -1000; // our initial highest value
        for (int i = 0; i <=2; i++) {// searching all the board positions to find an empty one
            for (int j = 0; j <=2; j++){
                if (board [i][j]== "why"){// once it has found an empty one, it makes a move for ally player
                    board [i][j] = ally;
                    int moveValueIndex = AI(kandc, board, false, depth, ally, opponent, isX); // uses AI to find the value of that specific move
                    board[i][j] = "why"; // undoes the move
                    if (moveValueIndex > highScore){ // if the value of the previous move is less than the value of the current move,
                        // we set the coordinates of that move to our array and the value of that move to our high score
                        bestMoveCoords [0] = i;
                        bestMoveCoords [1] = j;
                        highScore = moveValueIndex;
                    }
                }
            }
        }
        return bestMoveCoords;
    }
    // AI Stuff End

    public static void disableBoard(JButton[] cell) {
        for (int i = 0; i < cell.length; i++) cell[i].setEnabled(false);
    }

    public static void enableBoard(JButton[] cell) {
        for (int i = 0; i < cell.length; i++) cell[i].setEnabled(true);
    }

    public static void AIMove(KnotsAndCrosses kandc, String[][] board, JButton[] cell, int[] x,
                              String opponent, String gamemode, char whoGoFirst, char humanPlayer,JLabel Turn) {

        try
        {
            int X = x[0];
            int Y = x[1];
            // System.out.println(X + "," + Y);
            if (X == 0 && Y == 0) // Cell1
            {
                cell[0].setText(opponent);
            }
            else if (X == 1 && Y == 0) // Cell2
            {
                cell[1].setText(opponent);
            }
            else if (X == 2 && Y == 0) // Cell3
            {
                cell[2].setText(opponent);
            }
            else if (X == 0 && Y == 1) // Cell4
            {
                cell[3].setText(opponent);
            }
            else if (X == 1 && Y == 1) // Cell5
            {
                cell[4].setText(opponent);
            }
            else if (X == 2 && Y == 1) // Cell6
            {
                cell[5].setText(opponent);
            }
            else if (X == 0 && Y == 2) // Cell7
            {
                cell[6].setText(opponent);
            }
            else if (X == 1 && Y == 2) // Cell8
            {
                cell[7].setText(opponent);
            }
            else if (X == 2 && Y == 2) // Cell9
            {
                cell[8].setText(opponent);
            }
            board[X][Y] = opponent;
        }
        catch (Exception e)
        {
            Turn.setText("Tie what");
            disableBoard(cell);
        }
    }

    public static int[] getAIMove() //currently not being used
    {
        Random rand = new Random();
        int[] startingAIMove = startingPositions(rand);
        return startingAIMove;
    }

    public static boolean move(JButton cell, boolean turn, String[][] board, int x, int y, String gamemode, boolean isX,
                               String ally, String opponent, String[] Options) {
        if (board[x][y] == "why") // boolean turn=player 1 or 2/AI Options[2] decides who goes first
        { // boolean isX is player 1 X or not.
            if (Options[0] == "AI") {
                cell.setText(ally);
                board[x][y] = ally;
                return !turn;
            } else {
                if (turn) // If player 1
                {
                    cell.setText(ally);
                    board[x][y] = ally;
                    System.out.println(ally);
                    return !turn;
                } else // If player 2
                {
                    cell.setText(opponent);
                    board[x][y] = opponent;
                    return !turn;
                }
            }

        } else {
            error();
        }
        return turn;
    }

    public static boolean checkBoard(String[][] board) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (board[j][i] == "why") {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printBoard(String[][] board) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (board[j][i] != "why") {
                    System.out.print(board[j][i]);
                } else {
                    System.out.print("_");
                }
            }
            System.out.println();
        }
        System.out.println("-+-+-+-+-");
    }

    @SuppressWarnings("deprecation")
    public static String[] getOptions()
    {
        Font font = new Font("Comic Sans MS", Font.PLAIN, 40);
        Font Smallfont = new Font("Comic Sans MS", Font.PLAIN, 15);
        JFrame f = new JFrame("Pick stuff");
        f.getContentPane();
        f.setSize(500, 700);
        f.setVisible(true);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setCursor(2);
        String[] Options = new String[3];

        JLabel chooseGamemode = new JLabel("Choose your gamemode"); // creates essential JLabels
        JLabel choosePlayer = new JLabel("If AI mode, who will you play");
        JLabel chooseStartingPlayer = new JLabel("Choose who will go first");

        JLabel currentGamemode = new JLabel("Current Selection:");
        JLabel currentPlayer = new JLabel("Current Selection:");
        JLabel currentStartingPlayer = new JLabel("Current Selection:");

        chooseGamemode.setFont(Smallfont);
        choosePlayer.setFont(Smallfont);
        chooseStartingPlayer.setFont(Smallfont);
        currentGamemode.setFont(Smallfont);
        currentPlayer.setFont(Smallfont);
        currentStartingPlayer.setFont(Smallfont);

        f.add(chooseGamemode);
        f.add(choosePlayer);
        f.add(chooseStartingPlayer);
        f.add(currentGamemode);
        f.add(currentPlayer);
        f.add(currentStartingPlayer);

        chooseGamemode.setBounds(30, 10, 200, 50);
        choosePlayer.setBounds(30, 210, 400, 50);
        chooseStartingPlayer.setBounds(30, 410, 400, 50);

        currentGamemode.setBounds(300, 100, 200, 50);
        currentPlayer.setBounds(300, 300, 200, 50);
        currentStartingPlayer.setBounds(300, 500, 200, 50);

        JButton duo = new JButton("1v1");
        JButton AI = new JButton("AI");
        f.add(duo);
        f.add(AI);
        duo.setFont(font);
        AI.setFont(font);
        duo.setBounds(50, 60, 100, 100);
        AI.setBounds(150, 60, 100, 100);

        JButton X = new JButton("X");
        JButton O = new JButton("O");
        f.add(X);
        f.add(O);
        X.setFont(font);
        O.setFont(font);
        X.setBounds(50, 260, 100, 100);
        O.setBounds(150, 260, 100, 100);

        JButton XFirst = new JButton("X");
        JButton OFirst = new JButton("O");
        f.add(XFirst);
        f.add(OFirst);
        OFirst.setFont(font);
        XFirst.setFont(font);
        XFirst.setBounds(50, 460, 100, 100);
        OFirst.setBounds(150, 460, 100, 100);
        X.setEnabled(false);
        O.setEnabled(false);

        ActionListener e = (ActionListener) new ActionListener()// oh lord
        {
            public void actionPerformed(ActionEvent e)
            {
                Object obj = e.getSource();
                if (obj == duo)
                {
                    Options[0] = setOption(currentGamemode, "1v1");
                    X.setEnabled(false);
                    O.setEnabled(false);
                }
                else if (obj == AI)
                {
                    Options[0] = setOption(currentGamemode, "AI");
                    X.setEnabled(true);
                    O.setEnabled(true);
                }
                else if (obj == X)
                {
                    Options[1] = setOption(currentPlayer, "X");
                }
                else if (obj == O)
                {
                    Options[1] = setOption(currentPlayer, "O");
                }
                else if (obj == XFirst)
                {
                    Options[2] = setOption(currentStartingPlayer, "X First");
                }
                else if (obj == OFirst)
                {
                    Options[2] = setOption(currentStartingPlayer, "O First");
                }
            }
        };

        duo.addActionListener((ActionListener) e);
        AI.addActionListener((ActionListener) e);
        X.addActionListener((ActionListener) e);
        O.addActionListener((ActionListener) e);
        XFirst.addActionListener((ActionListener) e);
        OFirst.addActionListener((ActionListener) e);

        JButton complete = new JButton("Done");
        complete.addActionListener(new ActionListener()
        {
            boolean gameStart = false;

            public void actionPerformed(ActionEvent e)
            {
                if (Options[0] == "1v1" && Options[2]!=null) //If 1v1
                {
                    Options[1] = Options[2];
                    gameStart = confirmGame(f, Options);
                }
                else if (Options[0]=="AI" && Options[1]!=null && Options[2]!=null) //If AI
                {
                    gameStart = confirmGame(f, Options);
                }
                else
                {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Fill in all the boxes pls", "damn", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        f.add(complete);
        complete.setBounds(200, 600, 100, 50);
        return Options;
    }

    public static String setOption(JLabel text, String option) {
        text.setText("Current Selection:" + option);
        return option;
    }

    @SuppressWarnings("deprecation")
    public static boolean confirmGame(JFrame f, String[] Options) throws ArrayIndexOutOfBoundsException {
        try
        {
            Object[] options = { "Lets go", "Nah", };
            int n = JOptionPane.showOptionDialog(f,
                    "You sure you want to play a " + Options[0] + " Game with player one playing " + Options[1]
                            + " And " + Options[2],
                    "You Good with this?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                    options[1]);
            if (n == JOptionPane.YES_OPTION)
            {
                f.dispose();
                JOptionPane.showMessageDialog(null, "Enjoy Anish's tic tac toe", null,
                        JOptionPane.INFORMATION_MESSAGE);
                game(Options);
                return true;
            }
            else if (n == JOptionPane.NO_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Wow u bad", null, JOptionPane.INFORMATION_MESSAGE);
                // Calls method again
            }
        }
        catch (Exception e)
        {
            System.out.println("oops");
        }
        return false;
    }

    public static void rescale(JButton[] cell, JFrame f, JButton Reset, JLabel Turn, JLabel Win,
                               JLabel Wins) {
        int boxDimensions = (int) (f.getHeight() * 0.30);
        int xVal = (int) ((f.getWidth() / 2) - (boxDimensions * 1.5));
        int yVal = (int) (boxDimensions * 0.025);
        // System.out.println(yVal);
        int resetXSize = boxDimensions;
        int resetYSize = boxDimensions / 4;
        cell[0].setBounds(xVal, yVal, boxDimensions, boxDimensions);
        cell[1].setBounds(xVal + boxDimensions, yVal, boxDimensions, boxDimensions);
        cell[2].setBounds(xVal + (boxDimensions * 2), yVal, boxDimensions, boxDimensions);
        cell[3].setBounds(xVal, yVal + boxDimensions, boxDimensions, boxDimensions);
        cell[4].setBounds(xVal + boxDimensions, yVal + boxDimensions, boxDimensions, boxDimensions);
        cell[5].setBounds(xVal + (boxDimensions * 2), yVal + boxDimensions, boxDimensions, boxDimensions);
        cell[6].setBounds(xVal, yVal + (boxDimensions * 2), boxDimensions, boxDimensions);
        cell[7].setBounds(xVal + boxDimensions, yVal + (boxDimensions * 2), boxDimensions, boxDimensions);
        cell[8].setBounds(xVal + (boxDimensions * 2), yVal + (boxDimensions * 2), boxDimensions, boxDimensions);
        Reset.setBounds((xVal / 2) - (resetXSize / 2), (f.getHeight() / 2) - resetYSize, resetXSize, resetYSize);
        Turn.setBounds((xVal / 2) - (resetXSize / 2), (f.getHeight() / 2) + resetYSize, resetXSize * 2, resetYSize);
        Win.setBounds((xVal / 2) - (resetXSize / 2), (f.getHeight() / 3), resetXSize, resetYSize);
        Wins.setBounds((xVal / 2) - (resetXSize / 2), ((f.getHeight() / 2) - resetYSize) + (int) (resetYSize * 1.25),
                resetXSize, resetYSize);

        Font font = new Font("Comic Sans MS", Font.BOLD, xVal / 3);
        Font smallFont = new Font("Comic Sans MS", Font.PLAIN, resetYSize / 3);

        for (int i = 0; i < cell.length; i++) {
            cell[i].setFont(font);// Adds cell font to the buttons
        }

        Turn.setFont(smallFont); // adds font to all the text
        Win.setFont(smallFont);
        Wins.setFont(smallFont);
        Reset.setFont(smallFont);

        for (int i = 0; i < cell.length; i++) cell[i].setFont(font); // Adds cell font to the buttons

        Turn.setFont(smallFont);
        Win.setFont(smallFont);
        Wins.setFont(smallFont);
        Reset.setFont(smallFont);
    }

    public static void winLine(JButton[] cell, KnotsAndCrosses kandc, String [][] board, String player) {
        ArrayList<JButton> grid = new ArrayList<JButton>();
        ArrayList<Integer> coords = kandc.lineCord(player, board);
        int x = (int) coords.get(0);
        int y = (int) coords.get(1);
        int startCell = 0;
        if (x == 2 && y == 0) {
            startCell = 3;
        } else if (x == 2 && y == 1) {
            startCell = 6;
        } else if (x == 2 && y == 2) {
            startCell = 9;
        } else if (x == 1 && y == 2) {
            startCell = 8;
        } else if (x == 0 && y == 2) {
            startCell = 7;
        }
        int direction = (int) coords.get(2);
        for (int i = 0; i < cell.length; i++) grid.add(cell[i]);

        if (direction == 1) { // horizontal
            for (int i = 1; i <= 3; i++) {
                grid.get(startCell - i).setForeground(Color.WHITE);
            }
            if (y == 0) {
                for (int j = 0; j < 6; j++) {
                    grid.get(3 + j).setEnabled(false);
                }
            } else if (y == 1) {
                for (int j = 0; j < 3; j++) {
                    grid.get(j).setEnabled(false);
                }
                for (int k = 0; k < 3; k++) {
                    grid.get(6 + k).setEnabled(false);
                }
            } else if (y == 2) {
                for (int l = 0; l < 6; l++) {
                    grid.get(l).setEnabled(false);
                }
            }
        } else if (direction == 2) { //vertical
            for (int i = 0; i < 3; i++) {
                grid.get((startCell - 1) - (3 * i)).setForeground(Color.WHITE);
            }
            if (x == 0) {
                for (int j = 0; j < 3; j++) {
                    grid.get((startCell - (3 * j))).setEnabled(false);
                }
                for (int k = 0; k < 3; k++) {
                    grid.get((startCell + 1) - (3 * k)).setEnabled(false);
                }
            } else if (x == 1) {
                for (int l = 0; l < 3; l++) {
                    grid.get((startCell - 2) - (3 * l)).setEnabled(false);
                }
                for (int m = 0; m < 3; m++) {
                    grid.get((startCell) - (3 * m)).setEnabled(false);
                }
            } else if (x == 2) {
                for (int n = 0; n < 3; n++) {
                    grid.get((startCell - 2) - (3 * n)).setEnabled(false);
                }
                for (int p = 0; p < 3; p++) {
                    grid.get((startCell - 3) - (3 * p)).setEnabled(false);
                }
            }
        } else if (direction == 3) { //diagonal with a negative slope
            for (int i = 0; i < 3; i++) {
                grid.get((startCell - 1) - (i * 4)).setForeground(Color.WHITE);
            }
            for (int j = 0; j < 3; j++) {
                grid.get(j + 1).setEnabled(false);
            }
            for (int k = 0; k < 3; k++) {
                grid.get(k + 5).setEnabled(false);
            }
        } else if (direction == 4) { // diagonal with a positive slope
            for (int i = 0; i < 3; i++) {
                grid.get((startCell - 1) - (2 * i)).setForeground(Color.WHITE);
            }
            grid.get(0).setEnabled(false); //I had to disable these cells manually scince there was no corelation between their numbers
            grid.get(1).setEnabled(false);
            grid.get(3).setEnabled(false);
            grid.get(5).setEnabled(false);
            grid.get(7).setEnabled(false);
            grid.get(8).setEnabled(false);
        }

    }
}
