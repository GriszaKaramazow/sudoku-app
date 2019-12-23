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

//        printSudoku();
//
//        Sudoku sudokuToSolve = new Sudoku(sudoku);
//
//        PrintingService printingService = new PrintingService(sudokuToSolve);
//        CheckingService checkingService = new CheckingService(sudokuToSolve);
//        SolvingService solvingService = new SolvingService(sudokuToSolve, checkingService, printingService);
//
//        solvingService.solveSudoku(true, false);

        SudokuGenerator sudokuGenerator = new SudokuGenerator();

        for (int i = 0; i < 20; i++) {
            System.out.println("#" + (i+1) + ": ");
            SimpleSudoku simpleSudoku = sudokuGenerator.generateSudoku();
            simpleSudoku.printSolvedSudoku();
            simpleSudoku.printGeneratedSudoku();
            Sudoku sudoku = new Sudoku(simpleSudoku.getGeneratedSudoku());
            PrintingService printingService = new PrintingService(sudoku);
            CheckingService checkingService = new CheckingService(sudoku);
            SolvingService solvingService = new SolvingService(sudoku, checkingService, printingService);
            solvingService.solveSudoku(true, false);
        }

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
