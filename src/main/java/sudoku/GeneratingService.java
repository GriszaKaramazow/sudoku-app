package sudoku;

import java.util.*;

class GeneratingService {

    private Sudoku sudoku;
    private SolvingService solvingService;

    private Random randomGenerator = new Random();

    GeneratingService(Sudoku sudoku, SolvingService solvingService) {
        this.sudoku = sudoku;
        this.solvingService = solvingService;
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

    void removeRandomBox() {
        sudoku.resetFilledBoxesList();
        int randomAddress = randomGenerator.nextInt(sudoku.getFilledBoxes().size());
        sudoku.addToEmptyBoxes(randomAddress);
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

    // TODO: clean
    void removeAllEmptyBoxes() {
        for (Integer emptyBox : sudoku.getEmptyBoxes()) {
            // sudoku[sudoku.getEmptyBoxes(emptyBox)/10][filledBoxes.get(emptyBox)%10].resetBox();
        }
    }
}
