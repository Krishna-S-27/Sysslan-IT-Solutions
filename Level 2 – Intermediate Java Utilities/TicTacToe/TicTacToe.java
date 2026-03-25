package TicTacToe;
import java.util.*;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private int moves;
    private String player1Name;
    private String player2Name;

    public TicTacToe() {
        this.board = new char[3][3];
        this.currentPlayer = 'X';
        this.gameOver = false;
        this.moves = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void displayBoard() {
        System.out.println("\n");
        System.out.println("    0   1   2");
        System.out.println("   -----------");
        for(int i = 0; i < 3; i++) {
            System.out.print(i + " | ");
            for(int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("   -----------");
        }
        System.out.println();
    }

    public void getPlayerNames() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Player 1 Name (X): ");
        player1Name = sc.nextLine();
        System.out.print("Enter Player 2 Name (O): ");
        player2Name = sc.nextLine();
        System.out.println();
    }

    public boolean makeMove(int row, int col) {
        if(row < 0 || row > 2 || col < 0 || col > 2) {
            System.out.println("Invalid position! Rows and columns must be 0-2.");
            return false;
        }

        if(board[row][col] != ' ') {
            System.out.println("Position already occupied! Try again.");
            return false;
        }

        board[row][col] = currentPlayer;
        moves++;
        return true;
    }

    private String getCurrentPlayerName() {
        return currentPlayer == 'X' ? player1Name : player2Name;
    }

    public boolean checkWin() {
        for(int i = 0; i < 3; i++) {
            if(board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        for(int j = 0; j < 3; j++) {
            if(board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        if(board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }

        if(board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        return false;
    }

    public boolean checkDraw() {
        return moves == 9;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        getPlayerNames();

        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Enter row (0-2) and column (0-2) to place your mark");

        while(!gameOver) {
            displayBoard();

            String currentName = getCurrentPlayerName();
            System.out.println(currentName + "'s turn (" + currentPlayer + ")");

            int row = -1, col = -1;
            boolean validMove = false;

            while(!validMove) {
                try {
                    System.out.print("Enter row (0-2): ");
                    row = sc.nextInt();
                    System.out.print("Enter column (0-2): ");
                    col = sc.nextInt();
                    sc.nextLine();

                    validMove = makeMove(row, col);
                } catch(InputMismatchException e) {
                    System.out.println("Invalid input! Please enter numbers 0-2.");
                    sc.nextLine();
                }
            }

            if(checkWin()) {
                displayBoard();
                System.out.println(currentName + " (" + currentPlayer + ") WINS!");
                gameOver = true;
            }
            else if(checkDraw()) {
                displayBoard();
                System.out.println("It's a DRAW!");
                gameOver = true;
            }
            else {
                switchPlayer();
            }
        }

        playAgain(sc);
    }

    private void playAgain(Scanner sc) {
        System.out.print("\nDo you want to play again? (y/n): ");
        String response = sc.nextLine().toLowerCase();

        if(response.equals("y")) {
            TicTacToe newGame = new TicTacToe();
            newGame.play();
        } else {
            System.out.println("Thanks for playing! Goodbye!");
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Tic-Tac-Toe!");

        TicTacToe game = new TicTacToe();
        game.play();
    }
}