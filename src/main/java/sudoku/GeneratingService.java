package sudoku;

import java.util.*;

class GeneratingService {

    private Sudoku sudoku;
    private SimpleSudoku simpleSudoku;
    private ArrayList<Integer> recentlyRemovedBoxes;
    private final SolvingService solvingService;
    private final CheckingService checkingService;

    private Random randomGenerator = new Random();

    GeneratingService(Sudoku sudoku, CheckingService checkingService, SolvingService solvingService) {
        this.sudoku = sudoku;
        recentlyRemovedBoxes = new ArrayList<>();
        this.checkingService = checkingService;
        this.solvingService = solvingService;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public SimpleSudoku getSimpleSudoku() {
        return simpleSudoku;
    }

    void fillDiagonalSquaresWithRandomValues() {
        for (int i = 0; i < 9; i += 3) {
            List<Integer> possibilities = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            List<Integer> addresses = new ArrayList<>(Arrays.asList(0, 1, 2, 10, 11, 12, 20, 21, 22));
            for (Integer address : addresses) {
                Integer value = possibilities.get(randomGenerator.nextInt(possibilities.size()));
                sudoku.setSudokuBoxValueInteger(i + address / 10, i + address % 10, value);
                possibilities.remove(value);
            }
        }
    }

    boolean fillRandomBoxWithRandomValue() {
        sudoku.resetEmptyBoxesList();

        if (sudoku.getEmptyBoxes().size() == 0) {
            return false;
        }

        int randomAddress = randomGenerator.nextInt(sudoku.getEmptyBoxes().size());
        int row = sudoku.getEmptyBoxes().get(randomAddress) / 10;
        int column = sudoku.getEmptyBoxes().get(randomAddress) % 10;

        List<Integer> possibleValues = sudoku.getSudokuBoxValue(row, column);

        if (sudoku.getSudokuBoxValue(row, column).size() != 0) {
            int randomValue = possibleValues.get(randomGenerator.nextInt(possibleValues.size()));
            sudoku.setSudokuBoxValueInteger(row, column, randomValue);
            return true;
        }

        return false;
    }

    boolean fillWithRandom() {
        fillDiagonalSquaresWithRandomValues();
        while (checkingService.checkIfSudokuCanBeFurtherFilled()) {
            fillRandomBoxWithRandomValue();
            solvingService.solveSudoku(false, false);
        }
        return (checkingService.checkIfSudokuIsSolvedProperly(false));
    }

    void removeRandomBox() {
        sudoku.resetFilledBoxesList();

        if (sudoku.getFilledBoxes().size() == 0) {
            return;
        }

        int randomAddress = randomGenerator.nextInt(sudoku.getFilledBoxes().size());
        int row = sudoku.getFilledBoxes().get(randomAddress) / 10;
        int column = sudoku.getFilledBoxes().get(randomAddress) % 10;
        sudoku.addToEmptyBoxes(randomAddress);
    }

    void removeNineRandomBoxes() {
        recentlyRemovedBoxes.clear();
        List<Integer> rows = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        List<Integer> columns = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        for (int i = 0; i < 9; i++) {
            int randomRowsIndex = randomGenerator.nextInt(rows.size());
            int randomColumnsIndex = randomGenerator.nextInt(columns.size());
            int randomRow = rows.get(randomRowsIndex);
            int randomColumn = columns.get(randomColumnsIndex);
            rows.remove(randomRowsIndex);
            columns.remove(randomColumnsIndex);
            recentlyRemovedBoxes.add(randomRow * 10 + randomColumn);
        }
    }

    SimpleSudoku createSudoku() {
        SimpleSudoku simpleSudoku = new SimpleSudoku(sudoku.generateSimpleSudoku());
        sudoku.resetEmptyBoxesList();
        recentlyRemovedBoxes.clear();
        while (solvingService.solveSudoku(false, false)){
            sudoku.addToEmptyBoxes(recentlyRemovedBoxes);
            removeNineRandomBoxes();
            removeRecentlyRemovedBoxes();
            sudoku.removeAllEmptyBoxes();
        }
        System.out.println("(" + sudoku.getEmptyBoxes().size() + " empty boxes)");
        System.out.println();
        simpleSudoku.createSudoku(sudoku.getEmptyBoxes());
        this.simpleSudoku = simpleSudoku;
        return simpleSudoku;
    }

    private void removeRecentlyRemovedBoxes() {
        if (recentlyRemovedBoxes.size() == 0) {
            return;
        }
        for (Integer recentlyRemovedBox : recentlyRemovedBoxes) {
            int row = recentlyRemovedBox / 10;
            int column = recentlyRemovedBox % 10;
            sudoku.resetSudokuBox(row, column);
        }
    }

    void reducePossibleValuesInBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBoxValue(i, j).size() > 1) {
                    sudoku.setSudokuBoxValue(i, j, solvingService.possibleValuesInTheBox(i, j));
                }
            }
        }
    }

    private static void printSudoku(int[][] sudoku) {
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
}
