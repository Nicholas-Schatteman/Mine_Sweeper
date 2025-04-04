import java.util.Random;
import java.lang.Error;

public class MineField {
    private boolean[][] isMine, isFlag, isSeen;
    private int number[][];
    private int spacesUnchecked;
    private int bombCount;
    final int SEPERATION_SPACES = 3;
    

    public MineField(int rows, int columns, int bombs){
        //Create array of player view which starts as all unknown spaces
        number = new int[rows][columns];
        spacesUnchecked = number.length * number[0].length;
        
        //There are 0 bombs at start
        bombCount = 0;

        bombAdder(bombs);
    }

    public int rowLength(){return number.length;}
    public int columnLength(){return number[0].length;}
    public boolean isWon(){return spacesUnchecked == bombCount;}
    public boolean isSeen(int row, int column){return isSeen[row - 1][column - 1];}
    public boolean isFlaged(int row, int column){return isFlag[row - 1][column - 1];}

    public boolean check(int row, int column){
        if (isFlag[row - 1][column - 1]){
            throw new Error("Flag must be removed before checking for mine.");
        }


        if (!isSeen[row - 1][column - 1]){
            spacesUnchecked--;
            isSeen[row - 1][column - 1] = true;

            if (number[row - 1][column - 1] == 0 && !isFlag[row - 1][column - 1]){
                for (int xCheck = -1; xCheck <= 1; xCheck++){
                    for (int yCheck = -1; yCheck <= 1; yCheck++){
                        if ((0 < row + xCheck && row + xCheck <= number.length) 
                        && (0 < column + yCheck && column + yCheck <= number[0].length) 
                        && (xCheck != 0 || yCheck != 0)){
                            check(row + xCheck, column + yCheck);
                        }
                    }
                }
            }
        }

        return isMine[row - 1][column - 1];
    }

    public void addFlag(int row, int column){
        if (isFlag[row - 1][column - 1]){
            isFlag[row - 1][column - 1] = false;
        }
        else{
            isFlag[row - 1][column - 1] = true;
        }
    }

    protected void bombAdder(int bombs){
        bombCount += bombs;

        /* Error Code */
        if (bombCount >= number.length * number[0].length){
            throw new IndexOutOfBoundsException("There are more bombs then spaces.");
        }
        /* Error Code */
        
        int row;
        int column;
        int dummyCounter = 0;
        Random mineGenerator = new Random();

        while (dummyCounter < bombs){
            row = mineGenerator.nextInt(number.length);
            column = mineGenerator.nextInt(number[0].length);
            
            if (!isMine[row][column]){
                isMine[row][column] = true;
                dummyCounter++;
            }
        }
        updateNumbers();
    }

    protected void updateNumbers(){
        int bombsSurrounding = 0;

        for (int x = 0; x < number.length; x++){
            for (int y = 0; y < number[0].length; y++){
                if (!isSeen[x][y]){
                    //For all positions that aren't bombs
                    for (int xCheck = -1; xCheck <= 1; xCheck++){
                        for (int yCheck = -1; yCheck <= 1; yCheck++){
                            //If it is an existing tile and has a bomb, increment
                            if ((0 <= x + xCheck && x + xCheck < number.length) && (0 <= y + yCheck && y + yCheck < number[0].length) 
                            && isMine[x + xCheck][y + yCheck]){
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
        for (int x = 0; x < number.length; x++){
            numberSizeX = Integer.toString(x + 1).length();
            System.out.print((x + 1));
            //Spaceing correction
            System.out.print(" ".repeat(SEPERATION_SPACES - numberSizeX + 1));
        }
        System.out.println("\n");

        for (int y = 0; y < number[0].length; y++){
            numberSizeY = Integer.toString(y+1).length();
            //Print column numbers
            System.out.print((y + 1));
            //Spaceing correction
            System.out.print(" ".repeat(SEPERATION_SPACES - numberSizeY + 1));

            //Print spaces
            for (int x = 0; x < number.length; x++){
                if (isFlag[x][y]){
                    System.out.print("F");
                }

                else if (isSeen[x][y] && isSeen[x][y]){
                    System.out.print("!");
                }

                else if (isSeen[x][y]){
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
