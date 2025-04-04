import java.util.Scanner;

public class MineSweeper{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean isLost = false;
        int rowInput;
        int columnInput;
        int bombsInput;

        Object pointInput[];
        boolean toFlag;

        rowInput = positiveUserInput(input, "the number of rows.");
        columnInput = positiveUserInput(input, "the number of columns.");
        bombsInput = positiveUserInput(input, "the number of bombs on the board.");

        MineField board = new MineField(rowInput, columnInput, bombsInput);
        board.printConsole();

        Scanner scan = new Scanner(System.in);

        while (!board.isWon() && !isLost){
            pointInput = positionOperationInput(scan,board.rowLength(), board.columnLength());
            rowInput = (int)pointInput[0];
            columnInput = (int)pointInput[1];
            toFlag = (boolean)pointInput[2];

            if (toFlag){
                if (!board.isSeen(rowInput, columnInput)){
                    board.addFlag(rowInput, columnInput);
                }
            }
            else{
                if (!board.isFlaged(rowInput, columnInput)){
                    isLost = board.check(rowInput, columnInput);
                }
            }

            board.printConsole();
        }

        if (board.isWon()){
            System.out.println("-O-O\n\\___/\n");
        }
        else{
            System.out.println(" x x\n ___\n/   \\");
        }
    }

    public static int positiveUserInput(Scanner input, String request){
        int inputVal = 0;
        
        System.out.println("Please enter " + request);
        while (true){
            if (input.hasNextInt()){
                inputVal = input.nextInt();

                //If else whether input is positive
                if (inputVal > 0){
                    return inputVal;
                }
                else{
                    System.out.println("Input must be greater than 0.");
                }
            }
            else{
                input.next();
                System.out.println("Input must be an integer");
            }
        }

    }

    public static Object[] positionOperationInput(Scanner input, int xLength, int yLength){
        int row = 0;
        int column = 0;
        boolean isFlag = false;

        String message = "";
        boolean isValid = true;
        int comma;

        //While rows, columns, or format are not valid
        while (!isValid || !(0 < row && row <= xLength)){
            isValid = true;

            message = input.nextLine();
            if (message.isBlank()) isValid = false;

            comma = message.indexOf(',');
            if (comma == -1) isValid = false;


            row = findNumber(message, 0, comma);
            if (!(0 < row && row <= xLength)) isValid = false;

            column = findNumber(message, comma + 1, message.length());
            if (!(0 < column && column <= yLength)) isValid = false;


            if (isValid && message.charAt(message.length() - 1) == 'f' || message.charAt(message.length() - 1) == 'F'){
                isFlag = true;
            }
        }

        return new Object[]{row,column,isFlag};
    }

    //Finds the first number between start (inclusive) and end (exclusive)
    public static int findNumber(String toFind, int start, int end){
        String number = "";
        boolean previouslyNumber = false;
        for (int index = start; index < end; index++){
            //If the index is a digit
            if ('0' <= toFind.charAt(index) && toFind.charAt(index) <= '9'){
                previouslyNumber = true;
                number += toFind.charAt(index);
            }
            else if (previouslyNumber){
                index = end;
            }
        }

        if (number.isEmpty()){
            return -1;
        }
        else{
            return Integer.parseInt(number);
        }

        
    }
}