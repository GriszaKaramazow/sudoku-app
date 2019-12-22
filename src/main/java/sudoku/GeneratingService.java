package sudoku;

import java.util.*;

class GeneratingService {

    private Sudoku sudoku;
    private int[][] generatedSudoku;
    private final SolvingService solvingService;
    private final CheckingService checkingService;

    private Random randomGenerator = new Random();

    GeneratingService(Sudoku sudoku, CheckingService checkingService, SolvingService solvingService) {
        this.sudoku = sudoku;
        this.checkingService = checkingService;
        this.solvingService = solvingService;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public int[][] getGeneratedSudoku() {
        return generatedSudoku;
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
        int randomAddress;
        if (sudoku.getEmptyBoxes().size() > 0) {
            randomAddress = randomGenerator.nextInt(sudoku.getEmptyBoxes().size());
            int row = sudoku.getEmptyBoxes().get(randomAddress)/10;
            int column = sudoku.getEmptyBoxes().get(randomAddress)%10;
            sudoku.removeFromEmptyBoxes(randomAddress);
            List<Integer> possibleValues = sudoku.getSudokuBoxValue(row, column);
            if (sudoku.getSudokuBoxValue(row, column).size() == 1) {
                sudoku.setSudokuBoxValueInteger(row, column, sudoku.getSudokuBoxValueInteger(row, column));
            } else {
                if (sudoku.getSudokuBoxValue(row, column).size() != 0) {
                    int randomValue = possibleValues.get(randomGenerator.nextInt(possibleValues.size()));
                    sudoku.setSudokuBoxValueInteger(row, column, randomValue);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    boolean fillWithRandom() {
        fillDiagonalSquaresWithRandomValues();
        while (checkingService.checkIfSudokuCanBeFurtherFilled()) {
            fillRandomBoxWithRandomValue();
            solvingService.solveSudoku(false, false);
        }
        return (checkingService.checkIfSudokuIsFlawless(false));
    }

    void removeRandomBox() {
        if (sudoku.getFilledBoxes().size() == 0) {
            return;
        }
        sudoku.resetFilledBoxesList();
        int randomAddress = randomGenerator.nextInt(sudoku.getFilledBoxes().size());
        int row = sudoku.getFilledBoxes().get(randomAddress)/10;
        int column = sudoku.getFilledBoxes().get(randomAddress)%10;
        sudoku.resetSudokuBox(row, column);
    }

    void createSudoku() {
        while(solvingService.solveSudoku(false, false)) {
            generatedSudoku = sudoku.generateSimpleSudoku();
            removeRandomBox();
        }
    }

    // TODO: clean
    void reduceFilledBoxes(int numberOfEmptyBoxes) {
        boolean generate = true;
        while (generate) {
            for (int i = 0; i < numberOfEmptyBoxes; i++) {
                sudoku.resetFilledBoxesList();
                removeRandomBox();
                removeAllEmptyBoxes();
                if (!solvingService.solveSudoku(false, false)) {
                    System.out.println("Step #" + (i+1) + ": Unsolvable");
                    break;
                }
            }
            removeAllEmptyBoxes();
            generate = false;
        }
    }

    void reducePossibleValuesInBoxes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getSudokuBoxValue(i, j).size() > 1) {
                    sudoku.setSudokuBoxValue(i, j, solvingService.possibleValuesInTheBox(i,j));
                }
            }
        }
    }

    // TODO: clean
    void removeAllEmptyBoxes() {
        for (Integer emptyBox : sudoku.getEmptyBoxes()) {
            sudoku.getSudokuBox(sudoku.getEmptyBoxes().get(emptyBox)/10,sudoku.getEmptyBoxes().get(emptyBox)%10).resetBox();
        }
    }
}
