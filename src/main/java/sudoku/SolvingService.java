package sudoku;

import java.util.ArrayList;
import java.util.HashSet;

class SolvingService {

    private Sudoku sudoku;
    private final CheckingService checkingService;
    private final PrintingService printingService;

    SolvingService(Sudoku sudoku, CheckingService checkingService, PrintingService printingService) {
        this.sudoku = sudoku;
        this.checkingService = checkingService;
        this.printingService = printingService;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    boolean solveSudoku(boolean printSolved, boolean printSteps) {
        int solvingStepsCount = 0;
        long startTime = System.currentTimeMillis();

        while (checkingService.checkIfAnyBoxWasChangedRecently(false)) {
            checkingService.resetRecentChanges(true, true);
            solveRowsColumnsAndSquares();
            solvingStepsCount++;
            if (checkingService.checkIfAnyBoxWasChangedRecently(true) && printSteps) {
                System.out.println("Step " + solvingStepsCount + ":");
                printingService.printSudoku(false,true);
            }
            sudoku.setSolvingStepsCount(solvingStepsCount);
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
        sudoku.setSolvingTime(System.currentTimeMillis() - startTime);
        return checkingService.checkIfSudokuIsFlawless(printSolved || printSteps);
    }

    void solveRowsColumnsAndSquares() {
        // solving rows
        for (int i = 0; i < 9; i++) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < 9; j++) {
                sudokuSubgrid.addSudokuBox(sudoku.getSudokuBox(j,i));
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving columns
        for (int i = 0; i < 9; i++) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < 9; j++) {
                sudokuSubgrid.addSudokuBox(sudoku.getSudokuBox(i,j));
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving squares
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        sudokuSubgrid.addSudokuBox(sudoku.getSudokuBox(i*3+x, j*3+y));
                    }
                }
                updateSudoku(sudokuSubgrid);
            }
        }
    }

    private void updateSudoku(SudokuSubgrid sudokuSubgrid) {
        sudokuSubgrid.reduceUnknownValues();
        SudokuBox[] sudokuBoxes = sudokuSubgrid.getSudokuSubgrid();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku.setSudokuBox(sudokuBox);
        }
        sudokuSubgrid.checkIfDigitAppearsOnce();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku.setSudokuBox(sudokuBox);
        }
    }

    void reducePossibleValuesInBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBoxValue(i ,j).size() > 1) {
                    sudoku.setSudokuBoxValue(i, j, possibleValuesInTheBox(i, j));
                }
            }
        }
    }

    ArrayList<Integer> possibleValuesInTheBox (int row, int column) {
        HashSet<Integer> possibleValuesHashSet = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            possibleValuesHashSet.add(i);
        }

        HashSet<Integer> reservedValues = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            if (sudoku.getSudokuBoxValue(i, column).size() == 1) {
                reservedValues.add(sudoku.getSudokuBoxValueInteger(i, column) );
            }
            if (sudoku.getSudokuBoxValue(row, i).size() == 1) {
                reservedValues.add(sudoku.getSudokuBoxValueInteger(row, i));
            }
        }

        row /= 3;
        row *= 3;
        column /= 3;
        column *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getSudokuBoxValue(row+i, column+j).size() == 1) {
                    reservedValues.add(sudoku.getSudokuBoxValueInteger(row+i, column+j));
                }
            }
        }

        possibleValuesHashSet.removeAll(reservedValues);
        return new ArrayList<>(possibleValuesHashSet);
    }
}
