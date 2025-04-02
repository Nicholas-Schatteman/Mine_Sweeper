import java.util.Random;
import java.lang.Error;

public class MineField {
    private boolean board[][][];//[isMine, isFlag, isSeen]
    private int number[][];
    private int spacesUnchecked;
    private int bombCount;
    final private int INFORMATION_COUNT = 3;
    final int IS_MINE = 0;
    final int IS_FLAG = 1;
    final int IS_SEEN = 2;
    final int SEPERATION_SPACES = 3;
    

    public MineField(int rows, int columns, int bombs){
        //Create array of player view which starts as all unknown spaces
        board = new boolean[rows][columns][INFORMATION_COUNT];
        number = new int[rows][columns];
        spacesUnchecked = board.length * board[0].length;
        
        //There are 0 bombs at start
        bombCount = 0;

        bombAdder(bombs);
    }

    public int rowLength(){return board.length;}
    public int columnLength(){return board[0].length;}
    public boolean isWon(){return spacesUnchecked == bombCount;}
    public boolean isSeen(int row, int column){return board[row][column][IS_SEEN];}
    public boolean isFlaged(int row, int column){return board[row][column][IS_FLAG];}

    public boolean check(int row, int column){
        if (board[row - 1][column - 1][IS_FLAG]){
            throw new Error("Flag must be removed before checking for mine.");
        }


        if (!board[row - 1][column - 1][IS_SEEN]){
            spacesUnchecked--;
            board[row - 1][column - 1][IS_SEEN] = true;

            if (number[row - 1][column - 1] == 0 && !board[row - 1][column - 1][IS_FLAG]){
                for (int xCheck = -1; xCheck <= 1; xCheck++){
                    for (int yCheck = -1; yCheck <= 1; yCheck++){
                        if ((0 < row + xCheck && row + xCheck <= board.length) 
                        && (0 < column + yCheck && column + yCheck <= board[0].length) 
                        && (xCheck != 0 || yCheck != 0)){
                            check(row + xCheck, column + yCheck);
                        }
                    }
                }
            }
        }

        return board[row - 1][column - 1][IS_MINE];
    }

    public void addFlag(int row, int column){
        if (board[row - 1][column - 1][IS_FLAG]){
            board[row - 1][column - 1][IS_FLAG] = false;
        }
        else{
            board[row - 1][column - 1][IS_FLAG] = true;
        }
    }

    protected void bombAdder(int bombs){
        bombCount += bombs;

        /* Error Code */
        if (bombCount >= board.length * board[0].length){
            throw new IndexOutOfBoundsException("There are more bombs then spaces.");
        }
        /* Error Code */
        
        int row;
        int column;
        int dummyCounter = 0;
        Random mineGenerator = new Random();

        while (dummyCounter < bombs){
            row = mineGenerator.nextInt(board.length);
            column = mineGenerator.nextInt(board[0].length);
            
            if (!board[row][column][IS_MINE]){
                board[row][column][IS_MINE] = true;
                dummyCounter++;
            }
        }
        updateNumbers();
    }

    protected void updateNumbers(){
        int bombsSurrounding = 0;

        for (int x = 0; x < board.length; x++){
            for (int y = 0; y < board[0].length; y++){
                if (!board[x][y][IS_MINE]){
                    //For all positions that aren't bombs
                    for (int xCheck = -1; xCheck <= 1; xCheck++){
                        for (int yCheck = -1; yCheck <= 1; yCheck++){
                            //If it is an existing tile and has a bomb, increment
                            if ((0 <= x + xCheck && x + xCheck < board.length) && (0 <= y + yCheck && y + yCheck < board[0].length) 
                            && board[x + xCheck][y + yCheck][IS_MINE]){
                                bombsSurrounding++;
                            }
                        }
                    }
                    number[x][y] = bombsSurrounding;
                    bombsSurrounding = 0;
                }
            }
        }
    }

    public void printConsole(){
        int numberSizeX;
        int numberSizeY;

        System.out.println("\n");

        //Print row numbers
        for (int count = 0; count <= SEPERATION_SPACES; count++){
            System.out.print(" ");
        }
        for (int x = 0; x < board.length; x++){
            numberSizeX = Integer.toString(x + 1).length();
            System.out.print((x + 1));
            //Spaceing correction
            System.out.print(" ".repeat(SEPERATION_SPACES - numberSizeX + 1));
        }
        System.out.println("\n");

        for (int y = 0; y < board[0].length; y++){
            numberSizeY = Integer.toString(y+1).length();
            //Print column numbers
            System.out.print((y + 1));
            //Spaceing correction
            System.out.print(" ".repeat(SEPERATION_SPACES - numberSizeY + 1));

            //Print spaces
            for (int x = 0; x < board.length; x++){
                if (board[x][y][IS_FLAG]){
                    System.out.print("F");
                }

                else if (board[x][y][IS_SEEN] && board[x][y][IS_MINE]){
                    System.out.print("!");
                }

                else if (board[x][y][IS_SEEN]){
                    System.out.print(number[x][y]);
                }

                else{
                    System.out.print("#");
                }

                //Spacing correction
                System.out.print(" ".repeat(SEPERATION_SPACES));
                
            }
            System.out.println("\n");
        }
    }
}
