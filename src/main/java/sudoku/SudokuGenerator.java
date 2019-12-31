package sudoku;

public class SudokuGenerator {

    private Sudoku sudoku;
    private final PrintingService printingService;
    private final CheckingService checkingService;
    private final SolvingService solvingService;
    private final GeneratingService generatingService;

    public SudokuGenerator() {
        this.sudoku = new Sudoku();
        this.printingService = new PrintingService(sudoku);
        this.checkingService = new CheckingService(sudoku);
        this.solvingService = new SolvingService(sudoku, checkingService, printingService);
        this.generatingService = new GeneratingService(sudoku, checkingService, solvingService);
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    void resetSudokuGenerator() {
        sudoku = new Sudoku();
        resetSudokuGenerator(sudoku);
    }

    void resetSudokuGenerator(Sudoku sudoku) {
        printingService.setSudoku(sudoku);
        checkingService.setSudoku(sudoku);
        solvingService.setSudoku(sudoku);
        generatingService.setSudoku(sudoku);
    }

    boolean solveSudoku(boolean printSolved, boolean printSteps) {
        return solvingService.solveSudoku(printSolved, printSteps);
    }

    SimpleSudoku generateSimpleSudoku() {
        while(true) {
            if (fillWithRandom()) break;
        }
        return generatingService.createSudoku();
    }

    boolean fillWithRandom() {
        resetSudokuGenerator();
        return generatingService.fillWithRandom();
    }

}
