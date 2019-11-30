package sudoku;

class PrintingService {

    private Sudoku sudoku;

    private final String resetFontColor = "\u001B[0m"; // resets font color
    private final String blueFontColor = "\u001B[34m"; // sets font color to blue

    PrintingService(Sudoku sudoku) {
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
                    markBox = !sudoku.getSudokuBox(i, j).isGiven();
                } else if (markRecentlySolved) {
                    markBox = sudoku.getSudokuBox(i, j).hasBeenSolvedRecently();
                } else {
                    markBox = false;
                }
                if (markBox) {
                    System.out.print("[");
                    System.out.print(blueFontColor + sudoku.getSudokuBox(i, j).getBoxValueString() + resetFontColor);
                    System.out.print("]");
                } else System.out.print("[" + sudoku.getSudokuBox(i, j).getBoxValueString() + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printSudokuArrayWithArrayLists() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku.getSudokuBox(i, j).getBoxValue().toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    void printBoxesChangedRecently() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBox(i, j).isSolved()) {
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
