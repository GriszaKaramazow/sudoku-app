package sudoku;

class CheckingService {

    private Sudoku sudoku;

    public CheckingService(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    boolean checkIfSudokuIsSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku.getSudokuBox(i, j).isSolved()) {
                    return false;
                }
            }
        }
        return true;
    }

//    boolean checkIfSodokuSolvedProperly(boolean printMessageIfSolvedProperly, long sudokusSolvingTime) {
//        boolean isSudokuSolvedProperly = true;
//
//        for (int i = 0; i < sudoku.length; i++) { // checks rows and columns
//            SudokuSubgrid sudokuSubgridColumns = new SudokuSubgrid();
//            SudokuSubgrid sudokuSubgridRows = new SudokuSubgrid();
//            for (int j = 0; j < sudoku[i].length; j++) {
//                sudokuSubgridColumns.addSudokuBox(sudoku.getSudokuBox(i, j));
//                sudokuSubgridRows.addSudokuBox(sudoku.getSudokuBox(i, j));
//            }
//            if (sudokuSubgridColumns.checkIfSudokuGridIsSolvedProperly()) {
//                System.out.println("Column number " + (i + 1) + ": " + sudokuSubgridColumns.toString() + " contains duplicates");
//                isSudokuSolvedProperly = false;
//            }
//            if (sudokuSubgridRows.checkIfSudokuGridIsSolvedProperly()) {
//                System.out.println("Row number " + (i + 1) + ": " + sudokuSubgridRows.toString() + " contains duplicates");
//                isSudokuSolvedProperly = false;
//            }
//        }
//        for (int i = 0; i < 3; i++) { // checks squares
//            for (int j = 0; j < 3; j++) {
//                SudokuSubgrid sudokuSubgridSquare = new SudokuSubgrid();
//                for (int x = 0; x < 3; x++) {
//                    for (int y = 0; y < 3; y++) {
//                        sudokuSubgridSquare.addSudokuBox(sudoku[i*3+x][j*3+y]);
//                    }
//                }
//                if (sudokuSubgridSquare.checkIfSudokuGridIsSolvedProperly()) {
//                    System.out.println("Square: " + sudokuSubgridSquare.toString() + "contains duplicates");
//                    isSudokuSolvedProperly = false;
//                }
//            }
//        }
//
//        if (isSudokuSolvedProperly && printMessageIfSolvedProperly) {
//            String message = "Sudoku has been solved properly in " + sudokusSolvingTime + " ms";
//            StringBuilder dashes = new StringBuilder();
//            for (int i = 0; i < message.length(); i++) {
//                dashes.append("-");
//            }
//            System.out.println(dashes);
//            System.out.println(message);
//            System.out.println(dashes);
//        }
//        return isSudokuSolvedProperly;
//    }

    boolean checkIfSudokuIsFlawless() {
        boolean sudokuIsFlawless = true;
        if (checkIfSudokuHasEmptyBoxes()) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (checkIfValueIsDuplicated(sudoku.getSudokuBox(i, j))) {
                    System.out.println("Row: " + (i+1) + ", column: " + (j+1) + " is duplicated in ");
                    sudokuIsFlawless = false;
                }
            }
        }
        return sudokuIsFlawless;
    }

    boolean checkIfAnyBoxWasChangedRecently(boolean onlySolved) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (onlySolved && sudoku.getSudokuBox(i, j).hasBeenSolvedRecently()) {
                    return true;
                } else if (!onlySolved && sudoku.getSudokuBox(i, j).hasBeenChangedRecently()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfSudokuHasEmptyBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBox(i, j).getBoxValue().size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfValueIsDuplicated(SudokuBox sudokuBox) {
        if (sudokuBox == null) {
            return false;
        }
        int row = sudokuBox.getRowNumber();
        int column = sudokuBox.getColumnNumber();
        int boxValue = sudokuBox.getBoxValueInteger();
        if (boxValue == 0) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (i != row && sudoku.getSudokuBoxValueInteger(i, column) == boxValue) {
                System.out.println("(in row) row = " + (i+1) + ", column = " + (column+1));
                return true;
            }
            if (i != column && sudoku.getSudokuBoxValueInteger(row, i) == boxValue) {
                System.out.println("(in column) row = " + (row+1) + ", column = " + (i+1));
                return true;
            }
        }
        int squareRowNumber = row/3;
        squareRowNumber *= 3;
        int squareColumn = column/3;
        squareColumn *= 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (row != i + squareRowNumber && column != j + squareColumn) {
                    if (sudoku.getSudokuBoxValueInteger(i + squareRowNumber, j + squareColumn) == boxValue) {
                        System.out.println("(in square) row = " + (i + squareRowNumber + 1) + ", column = " + (j + squareColumn + 1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    int countEmptyBoxes() {
        int numberOfEmptyBoxes = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBox(i, j).getBoxValueInteger() == 0) {
                    numberOfEmptyBoxes++;
                }
            }
        }
        return numberOfEmptyBoxes;
    }


    void resetRecentChanges(boolean resetRecentlySolved, boolean resetRecentlyChanged) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (resetRecentlySolved) {
                    sudoku.getSudokuBox(i, j).setHasBeenSolvedRecently(false);
                }
                if (resetRecentlyChanged) {
                    sudoku.getSudokuBox(i, j).setHasBeenChangedRecently(false);
                }
            }
        }
    }

}
