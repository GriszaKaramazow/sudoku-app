package sudoku;

class SolvingService {

    private SudokuBox[][] sudoku;
    private final CheckingService checkingService;

    SolvingService(SudokuBox[][] sudoku, CheckingService checkingService) {
        this.sudoku = sudoku;
        this.checkingService = checkingService;
    }

    void solveRowsColumnsAndSquares() {

        // solving rows
        for (SudokuBox[] sudokuBox : sudoku) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < sudokuBox.length; j++) {
                sudokuSubgrid.addSudokuBox(sudokuBox[j]);
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving columns
        for (int i = 0; i < sudoku.length; i++) {
            SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
            for (int j = 0; j < sudoku[i].length; j++) {
                sudokuSubgrid.addSudokuBox(sudoku[j][i]);
            }
            updateSudoku(sudokuSubgrid);
        }

        // solving squares
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SudokuSubgrid sudokuSubgrid = new SudokuSubgrid();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        sudokuSubgrid.addSudokuBox(sudoku[i*3+x][j*3+y]);
                    }
                }
                updateSudoku(sudokuSubgrid);
            }
        }
    }

    private void updateSudoku(SudokuSubgrid sudokuSubgrid) {
        sudokuSubgrid.reduceUnknownValues();
        SudokuBox[] sudokuBoxes = sudokuSubgrid.getSudokuSubgrid();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku[sudokuBox.getRowNumber()][sudokuBox.getColumnNumber()] = sudokuBox;
        }
        sudokuSubgrid.checkIfDigitAppearsOnce();
        for (SudokuBox sudokuBox : sudokuBoxes) {
            sudoku[sudokuBox.getRowNumber()][sudokuBox.getColumnNumber()] = sudokuBox;
        }
    }
}
