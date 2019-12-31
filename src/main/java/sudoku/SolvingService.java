package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
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

        while (checkingService.checkIfAnyBoxWasChangedRecently(false)) {
            checkingService.resetRecentChanges(true, true);
            solveRowsColumnsAndSquares();
            solvingStepsCount++;
            if (checkingService.checkIfAnyBoxWasChangedRecently(true) && printSteps) {
                System.out.println("Step " + solvingStepsCount + ":");
                printingService.printSudoku(false,true);
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

        return checkingService.checkIfSudokuIsSolvedProperly(printSolved || printSteps);
    }

    void solveRowsColumnsAndSquares() {

        for (int row = 0; row < 9; row++) {
            SudokuSubgrid sudokuRow = new SudokuSubgrid();
            for (int column = 0; column < 9; column++) {
                sudokuRow.addSudokuBox(sudoku.getSudokuBox(column,row));
            }
            updateSudoku(sudokuRow);
        }

        for (int row = 0; row < 9; row++) {
            SudokuSubgrid sudokuColumn = new SudokuSubgrid();
            for (int column = 0; column < 9; column++) {
                sudokuColumn.addSudokuBox(sudoku.getSudokuBox(row,column));
            }
            updateSudoku(sudokuColumn);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SudokuSubgrid sudokuSquare = new SudokuSubgrid();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        sudokuSquare.addSudokuBox(sudoku.getSudokuBox(i*3+x, j*3+y));
                    }
                }
                updateSudoku(sudokuSquare);
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

    ArrayList<Integer> possibleValuesInTheBox (int inputRow, int inputColumn) {
        HashSet<Integer> possibleValuesHashSet = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        HashSet<Integer> reservedValues = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            if (sudoku.getSudokuBoxValueInteger(i, inputColumn) != 0) {
                reservedValues.add(sudoku.getSudokuBoxValueInteger(i, inputColumn) );
            }
            if (sudoku.getSudokuBoxValueInteger(inputRow, i) != 0) {
                reservedValues.add(sudoku.getSudokuBoxValueInteger(inputRow, i));
            }
        }

        inputRow /= 3;
        inputRow *= 3;
        inputColumn /= 3;
        inputColumn *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getSudokuBoxValueInteger(inputRow+i, inputColumn+j) != 0) {
                    reservedValues.add(sudoku.getSudokuBoxValueInteger(inputRow+i, inputColumn+j));
                }
            }

        }

        possibleValuesHashSet.removeAll(reservedValues);
        return new ArrayList<>(possibleValuesHashSet);
    }
}
