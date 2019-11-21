package sudoku;

public class Main {

    private static int[][] sudoku = {
            {1, 8, 0, 0, 6, 0, 9, 5, 3},
            {4, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 5, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 7, 3, 0, 0, 8, 9},
            {0, 0, 0, 0, 9, 0, 0, 0, 0},
            {9, 4, 0, 0, 2, 5, 0, 0, 0},
            {0, 0, 0, 0, 0, 8, 0, 6, 0},
            {5, 0, 0, 0, 0, 0, 0, 0, 4},
            {3, 6, 4, 0, 5, 0, 0, 2, 8}};

    public static void main(String[] args) {

        printSudoku();
        Sudoku sudokuArray = new Sudoku(sudoku);
        PrintingService printingService = new PrintingService(sudokuArray.getSudoku());
        sudokuArray.solveSudoku(true, true);

//        for (int i = 0; i < 10; i++) {
//            printSeparator();
//            System.out.println(i+1 + "# :");
//            Sudoku generateSudoku = new Sudoku();
//            generateSudoku.reduceFilledBoxes(60);
//            generateSudoku.solveSudoku(true, false);
//            System.out.println(generateSudoku.checkIfSodokuSolvedProperly(false));
//        }

    }

    private static void printSudoku() {
        System.out.println("Sudoku to solve:");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("[" + sudoku[i][j] + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printSeparator() {
        System.out.println();
        for (int i = 0; i < 80; i++) {
            System.out.print("=");
        }
        System.out.println();
        for (int i = 0; i < 80; i++) {
            System.out.print("=");
        }
        System.out.println();
        System.out.println();
    }

}
