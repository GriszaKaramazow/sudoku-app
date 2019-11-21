package sudoku;

public class SudokuSolver {

    private Sudoku sudoku;
    private final PrintingService printingService;
    private final CheckingService checkingService;
    private final SolvingService solvingService;

    public SudokuSolver(Sudoku sudoku) {
        this.sudoku = sudoku;
        printingService = new PrintingService(sudoku.getSudoku());
        checkingService = new CheckingService(sudoku.getSudoku());
        solvingService = new SolvingService(sudoku.getSudoku(), checkingService);
    }

    boolean solveSudoku(boolean printSolved, boolean printSteps) {
        int solvingStepsCount = 0;
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
        return checkingService.checkIfSodokuSolvedProperly(printSolved || printSteps,
                sudoku.getSolvingTime());
    }
}
