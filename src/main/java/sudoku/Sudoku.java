package sudoku;

import java.util.*;

class Sudoku {

    private SudokuBox[][] sudoku = new SudokuBox[9][9];
    private List<Integer> emptyBoxes = new ArrayList<>();
    private List<Integer> filledBoxes = new ArrayList<>();
    private List<Integer> emptyBoxValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    private long solvingTime;
    private int solvingStepsCount;

    private Random randomGenerator = new Random();

    private final String resetFontColor = "\u001B[0m"; // resets font color
    private final String blueFontColor = "\u001B[34m"; // sets font color to blue

    Sudoku(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuBox sudokuBox = new SudokuBox(i, j, sudoku[i][j]);
                this.sudoku[i][j] = sudokuBox;
            }
        }
    }

    Sudoku() {
        do {
            // creates sudoku with empty boxes
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    SudokuBox sudokuBox = new SudokuBox(i, j, 0);
                    sudoku[i][j] = sudokuBox;
                }
            }

            // fills diagonal squares with random values
            for (int i = 0; i < 9; i += 3) {
                List<Integer> possibilities = new ArrayList<>(emptyBoxValues);
                List<Integer> addresses = new ArrayList<>(Arrays.asList(0, 1, 2, 10, 11, 12, 20, 21, 22));
                for (Integer address : addresses) {
                    Integer value = possibilities.get(randomGenerator.nextInt(possibilities.size()));
                    sudoku[i + address / 10][i + address % 10].setBoxValueInteger(value);
                    possibilities.remove(value);
                }
            }
            reducePossibleValuesInBoxes();
            fillWithRandom();
        } while (!checkIfSudokuIsFlawless());

    }

    private void resetBoxesAddresses() {
        this.emptyBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku[i][j].isSolved()) {
                    this.emptyBoxes.add(i*10+j);
                }
            }
        }
    }

    private void resetFilledBoxesList() {
        this.filledBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValueInteger() != 0) {
                    this.filledBoxes.add(i*10+j);
                }
            }
        }
    }

    private void reducePossibleValuesInBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValue().size() > 1) {
                    sudoku[i][j].setBoxValue(possibleValuesInTheBox(sudoku[i][j]));
                }
            }
        }
    }

    void printSudoku(boolean markAllSolved, boolean markRecentlySolved) {
        if (markAllSolved && markRecentlySolved) {
            System.out.println("Error: both markAllSolved and markRecentlySolved are set true");
            return;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boolean markBox;
                if (markAllSolved) {
                    markBox = !sudoku[i][j].isGiven();
                } else if (markRecentlySolved) {
                    markBox = sudoku[i][j].hasBeenSolvedRecently();
                } else {
                    markBox = false;
                }
                if (markBox) {
                    System.out.print("[");
                    System.out.print(blueFontColor + sudoku[i][j].getBoxValueString() + resetFontColor);
                    System.out.print("]");
                } else System.out.print("[" + sudoku[i][j].getBoxValueString() + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printSudokuArrayWithArrayLists() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j].getBoxValue().toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    void printBoxesChangedRecently() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].isSolved()) {
                    System.out.print("[");
                    System.out.print(blueFontColor + "X" + resetFontColor);
                    System.out.print("]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    boolean checkIfAnyBoxWasChangedRecently(boolean onlySolved) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (onlySolved && sudoku[i][j].hasBeenSolvedRecently()) {
                    return true;
                } else if (!onlySolved && sudoku[i][j].hasBeenChangedRecently()) {
                    return true;
                }
            }
        }
        return false;
    }

    void reduceFilledBoxes(int numberOfEmptyBoxes) {
        boolean generate = true;
        while (generate) {
            for (int i = 0; i < numberOfEmptyBoxes; i++) {
                resetFilledBoxesList();
                removeBox();
                removeAllEmptyBoxes();
                if (!solveSudoku(false, false)) {
                    System.out.println("Step #" + (i+1) + ": Unsolvable");
                    break;
                }

            }
            removeAllEmptyBoxes();
            generate = false;
        }
    }

    boolean checkIfSudokuHasEmptyBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValue().size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkIfValueIsDuplicated(SudokuBox sudokuBox) {
        if (sudokuBox == null) {
            return false;
        }
        int rowNumber = sudokuBox.getRowNumber();
        int columnNumber = sudokuBox.getColumnNumber();
        int boxValue = sudokuBox.getBoxValueInteger();
        if (boxValue == 0) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (i != rowNumber && sudoku[i][columnNumber].getBoxValueInteger() == boxValue) {
                return true;
            }
            if (i != columnNumber && sudoku[rowNumber][i].getBoxValueInteger() == boxValue) {
                return true;
            }
        }
        int squareRowNumber = rowNumber/3;
        squareRowNumber *= 3;
        int squareColumnNumber = columnNumber/3;
        squareColumnNumber *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (rowNumber == i + squareRowNumber && columnNumber == j + squareColumnNumber) {
                    continue;
                } else if (sudoku[i + squareRowNumber][j + squareColumnNumber].getBoxValueInteger() == boxValue) {
                    return true;
                }
            }
        }
        return false;

    }

    boolean checkIfSudokuIsFlawless() {
        boolean sudokuIsFlawless = true;
        if (checkIfSudokuHasEmptyBoxes()) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (checkIfValueIsDuplicated(sudoku[i][j])) {
                    System.out.println("Row: " + (i+1) + ", column: " + (j+1) + " is duplicated");
                    sudokuIsFlawless = false;
                }
            }
        }
        return sudokuIsFlawless;
    }

    private void resetIfAnyChangesWereMadeRecently(boolean resetRecentlySolved, boolean resetRecentlyChanged) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (resetRecentlySolved) {
                    sudoku[i][j].setHasBeenSolvedRecently(false);
                }
                if (resetRecentlyChanged) {
                    sudoku[i][j].setHasBeenChangedRecently(false);
                }
            }
        }
    }

    private void solveRowsColumnsAndSquares() {

        // solving rows
        for (int i = 0; i < sudoku.length; i++) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < sudoku[i].length; j++) {
                sudokuSubgrid.addSudokuBox(sudoku[i][j]);
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving columns
        for (int i = 0; i < sudoku.length; i++) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < sudoku[i].length; j++) {
                sudokuSubgrid.addSudokuBox(sudoku[j][i]);
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving squares
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        sudokuSubgrid.addSudokuBox(sudoku[i*3+x][j*3+y]);
                    }
                }
                updateSudoku(sudokuSubgrid);
            }
        }
    }

    private int countEmptyBoxes () {
        int numberOfEmptyBoxes = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValueInteger() == 0) {
                    numberOfEmptyBoxes++;
                }
            }
        }
        return numberOfEmptyBoxes;
    }

    void swapBox() {
        resetFilledBoxesList();

        int randomValue = randomGenerator.nextInt(9) + 1;
        int randomAddress = randomGenerator.nextInt(filledBoxes.size());
        int rowNumberA = filledBoxes.get(randomAddress)/10;
        int columnNumberA = filledBoxes.get(randomAddress)%10;

        emptyBoxes(randomValue, rowNumberA, columnNumberA);
        sudoku[rowNumberA][columnNumberA].setBoxValueInteger(randomValue);

    }

    void removeBox() {
        resetFilledBoxesList();
        int randomAddress = randomGenerator.nextInt(filledBoxes.size());
        emptyBoxes.add(randomAddress);

    }

    void removeAllEmptyBoxes() {
        for (Integer emptyBox : emptyBoxes) {
            sudoku[filledBoxes.get(emptyBox)/10][filledBoxes.get(emptyBox)%10].resetBox();
        }
    }

    private void emptyBoxes(int valueToRemove, int rowNumber, int columnNumber) {
        for (int i = 0; i < 9; i++) {
            if (i != columnNumber && sudoku[rowNumber][i].getBoxValueInteger() == valueToRemove) {
                sudoku[rowNumber][i].resetBox();
            }
            if (i != rowNumber && sudoku[i][columnNumber].getBoxValueInteger() == valueToRemove) {
                sudoku[i][columnNumber].resetBox();
            }
        }

        rowNumber /= 3;
        rowNumber *= 3;
        columnNumber /= 3;
        columnNumber *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku[rowNumber+i][columnNumber+j].getBoxValueInteger() == valueToRemove) {
                    sudoku[rowNumber+i][columnNumber+j].resetBox();
                }
            }
        }
    }

    private ArrayList<Integer> possibleValuesInTheBox (SudokuBox sudokuBox) {
        HashSet<Integer> possibleValuesHashSet = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            possibleValuesHashSet.add(i);
        }

        int rowNumber = sudokuBox.getRowNumber();
        int columnNumber = sudokuBox.getColumnNumber();
        HashSet<Integer> reservedValues = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            if (sudoku[i][columnNumber].getBoxValue().size() == 1) {
                reservedValues.add(sudoku[i][columnNumber].getBoxValueInteger() );
            }
            if (sudoku[rowNumber][i].getBoxValue().size() == 1) {
                reservedValues.add(sudoku[rowNumber][i].getBoxValueInteger());
            }
        }

        rowNumber /= 3;
        rowNumber *= 3;
        columnNumber /= 3;
        columnNumber *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku[rowNumber+i][columnNumber+j].getBoxValue().size() == 1) {
                    reservedValues.add(sudoku[rowNumber+i][columnNumber+j].getBoxValueInteger());
                }
            }
        }

        possibleValuesHashSet.removeAll(reservedValues);
        return new ArrayList<>(possibleValuesHashSet);
    }

    private void updateSudoku(SudokuSubgrid sudokuSubgrid) {
        sudokuSubgrid.reduceUnknownValues();
        SudokuBox[] sudokuBoxes = sudokuSubgrid.getSudokuSubgrid();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku[sudokuBox.getRowNumber()][sudokuBox.getColumnNumber()] = sudokuBox;
        }
        sudokuSubgrid.checkIfDigitAppearsOnce();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku[sudokuBox.getRowNumber()][sudokuBox.getColumnNumber()] = sudokuBox;
        }
    }

    boolean checkIfSudokuIsSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku[i][j].isSolved()) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean solveSudoku(boolean printSolved, boolean printSteps) {
        solvingStepsCount = 0;
        long startTime = System.currentTimeMillis();

        while (checkIfAnyBoxWasChangedRecently(false)) {
            resetIfAnyChangesWereMadeRecently(true, true);
            solveRowsColumnsAndSquares();
            solvingStepsCount++;
            if (checkIfAnyBoxWasChangedRecently(true) && printSteps) {
                System.out.println("Step " + solvingStepsCount + ":");
                printSudoku(false,true);
                System.out.println(countEmptyBoxes());
            }
        }
        if (!checkIfSudokuIsSolved()) {
            if (printSolved || printSteps) {
                System.out.println("The sudoku was not solved properly");
            }
            return false;
        }
        if (printSolved && !printSteps) {
            printSudoku(true,false);
        }
        solvingTime = System.currentTimeMillis() - startTime;
        return checkIfSodokuSolvedProperly(printSolved || printSteps);
    }

    boolean checkIfSodokuSolvedProperly(boolean printMessageIfSolvedProperly) {
        boolean isSudokuSolvedProperly = true;

        for (int i = 0; i < sudoku.length; i++) { // checks rows and columns
            SudokuSubgrid sudokuSubgridColumns = new SudokuSubgrid();
            SudokuSubgrid sudokuSubgridRows = new SudokuSubgrid();
            for (int j = 0; j < sudoku[i].length; j++) {
                sudokuSubgridColumns.addSudokuBox(sudoku[i][j]);
                sudokuSubgridRows.addSudokuBox(sudoku[j][i]);
            }
            if (sudokuSubgridColumns.checkIfSudokuGridIsSolvedProperly()) {
                System.out.println("Column number " + (i + 1) + ": " + sudokuSubgridColumns.toString() + " contains duplicates");
                isSudokuSolvedProperly = false;
            }
            if (sudokuSubgridRows.checkIfSudokuGridIsSolvedProperly()) {
                System.out.println("Row number " + (i + 1) + ": " + sudokuSubgridRows.toString() + " contains duplicates");
                isSudokuSolvedProperly = false;
            }
        }
        for (int i = 0; i < 3; i++) { // checks squares
            for (int j = 0; j < 3; j++) {
                SudokuSubgrid sudokuSubgridSquare = new SudokuSubgrid();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        sudokuSubgridSquare.addSudokuBox(sudoku[i*3+x][j*3+y]);
                    }
                }
                if (sudokuSubgridSquare.checkIfSudokuGridIsSolvedProperly()) {
                    System.out.println("Square: " + sudokuSubgridSquare.toString() + "contains duplicates");
                    isSudokuSolvedProperly = false;
                }
            }
        }

        if (isSudokuSolvedProperly && printMessageIfSolvedProperly) {
            String message = "Sudoku has been solved properly in " + solvingTime + " ms";
            StringBuilder dashes = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                dashes.append("-");
            }
            System.out.println(dashes);
            System.out.println(message);
            System.out.println(dashes);
        }
        return isSudokuSolvedProperly;
    }

    boolean fillRandomBoxWithRandomValue() {
        resetBoxesAddresses();
        int randomAddress;
        if (emptyBoxes.size() > 0) {
            randomAddress = randomGenerator.nextInt(emptyBoxes.size());
            int rowNumber = emptyBoxes.get(randomAddress)/10;
            int columnNumber = emptyBoxes.get(randomAddress)%10;
            this.emptyBoxes.remove(randomAddress);
            List<Integer> possibleValues = sudoku[rowNumber][columnNumber].getBoxValue();
            if (sudoku[rowNumber][columnNumber].getBoxValue().size() == 1) {
                sudoku[rowNumber][columnNumber].setBoxValueInteger(sudoku[rowNumber][columnNumber].getBoxValueInteger());
            } else {
                if (sudoku[rowNumber][columnNumber].getBoxValue().size() != 0) {
                    int randomValue = possibleValues.get(randomGenerator.nextInt(possibleValues.size()));
                    sudoku[rowNumber][columnNumber].setBoxValueInteger(randomValue);
                } else {
                    return false;
                }
            }
            reducePossibleValuesInBoxes();
        }
        return true;
    }

    void fillWithRandom() {
        while (!solveSudoku(false, false)) {
            fillRandomBoxWithRandomValue();
            if (!checkIfSudokuIsFlawless()) {
                break;
            }
        }

    }
}
