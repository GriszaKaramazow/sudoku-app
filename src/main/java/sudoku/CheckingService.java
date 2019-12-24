package sudoku;

class CheckingService {

    private Sudoku sudoku;

    public CheckingService(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public void setSudoku(Sudoku sudoku) {
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

    boolean checkIfSudokuIsFlawless(boolean printMessageIfSolvedProperly) {
        if (checkIfSudokuHasBlankBoxes() || checkIfSudokuHasDuplicatedBoxes()) {
            return false;
        }
        if (printMessageIfSolvedProperly) {
            String message = "Sudoku has been solved properly";
            StringBuilder dashes = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                dashes.append("-");
            }
            System.out.println(dashes);
            System.out.println(message);
            System.out.println(dashes);
        }
        return true;
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

    // works well
    boolean checkIfSudokuHasDuplicatedBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (checkIfValueIsDuplicated(sudoku.getSudokuBox(i, j))) {
                    return true;
                }
            }
        }
        return false;
    }

    //works well
    boolean checkIfSudokuHasBlankBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBoxValue(i, j).size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkIfSudokuCanBeFurtherFilled() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBoxValue(i,j).size() > 1) {
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
                return true;
            }
            if (i != column && sudoku.getSudokuBoxValueInteger(row, i) == boxValue) {
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
                        return true;
                    }
                }
            }
        }
        return false;
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
