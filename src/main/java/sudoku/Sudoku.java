package sudoku;

import java.util.*;

class Sudoku {

    SudokuBox[][] sudoku = new SudokuBox[9][9];

    private List<Integer> emptyBoxes = new ArrayList<>();
    private List<Integer> filledBoxes = new ArrayList<>();
    private List<Integer> emptyBoxValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    private long solvingTime;
    private int solvingStepsCount;

    private PrintingService printingService = new PrintingService(sudoku);
    private CheckingService checkingService = new CheckingService(sudoku);
    private GeneratingService generatingService = new GeneratingService(sudoku);
    private SolvingService solvingService = new SolvingService(sudoku);

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
            generatingService.fillDiagonalSquaresWithRandomValues();

            //fills remaining boxes
            reducePossibleValuesInBoxes();
            fillWithRandom();
        } while (!checkingService.checkIfSudokuIsFlawless());

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

    public SudokuBox[][] getSudoku() {
        return sudoku;
    }

    void reduceFilledBoxes(int numberOfEmptyBoxes) {
        boolean generate = true;
        while (generate) {
            for (int i = 0; i < numberOfEmptyBoxes; i++) {
                resetFilledBoxesList();
                generatingService.removeBox();
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

    void removeAllEmptyBoxes() {
        for (Integer emptyBox : emptyBoxes) {
            sudoku[filledBoxes.get(emptyBox)/10][filledBoxes.get(emptyBox)%10].resetBox();
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

    boolean solveSudoku(boolean printSolved, boolean printSteps) {
        solvingStepsCount = 0;
        long startTime = System.currentTimeMillis();

        while (checkingService.checkIfAnyBoxWasChangedRecently(false)) {
            checkingService.resetIfAnyChangesWereMadeRecently(true, true);
            solvingService.solveRowsColumnsAndSquares();
            solvingStepsCount++;
            if (checkingService.checkIfAnyBoxWasChangedRecently(true) && printSteps) {
                System.out.println("Step " + solvingStepsCount + ":");
                printingService.printSudoku(false,true);
                System.out.println(checkingService.countEmptyBoxes());
            }
        }
        if (!checkingService.checkIfSudokuIsSolved()) {
            if (printSolved || printSteps) {
                System.out.println("The sudoku was not solved properly");
            }
            return false;
        }
        if (printSolved && !printSteps) {
            printingService.printSudoku(true,false);
        }
        solvingTime = System.currentTimeMillis() - startTime;
        return checkingService.checkIfSodokuSolvedProperly(printSolved || printSteps, solvingTime);
    }

    private void fillWithRandom() {
        while (!solveSudoku(false, false)) {
            generatingService.fillRandomBoxWithRandomValue();
            reducePossibleValuesInBoxes();
            if (!checkingService.checkIfSudokuIsFlawless()) {
                break;
            }
        }

    }
}
