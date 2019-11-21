package sudoku;

class CheckingService {

    private SudokuBox[][] sudoku;

    public CheckingService(SudokuBox[][] sudoku) {
        this.sudoku = sudoku;
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

    boolean checkIfSodokuSolvedProperly(boolean printMessageIfSolvedProperly, long sudokusSolvingTime) {
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
            String message = "Sudoku has been solved properly in " + sudokusSolvingTime + " ms";
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
}
