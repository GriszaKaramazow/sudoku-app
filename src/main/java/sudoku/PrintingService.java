package sudoku;

class PrintingService {

    private SudokuBox[][] sudoku;

    private final String resetFontColor = "\u001B[0m"; // resets font color
    private final String blueFontColor = "\u001B[34m"; // sets font color to blue

    public PrintingService(SudokuBox[][] sudoku) {
        this.sudoku = sudoku;
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

}
