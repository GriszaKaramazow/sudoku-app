package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class GeneratingService {

    private SudokuBox[][] sudoku;

    private List<Integer> emptyBoxes = new ArrayList<>();
    private List<Integer> filledBoxes = new ArrayList<>();

    private Random randomGenerator = new Random();


    public GeneratingService(SudokuBox[][] sudoku) {
        this.sudoku = sudoku;
    }

    private void resetEmptyBoxesList() {
        this.emptyBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku[i][j].isSolved()) {
                    this.emptyBoxes.add(i*10+j);
                }
            }
        }
    }

    private void resetFilledBoxesList() {
        this.filledBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValueInteger() != 0) {
                    this.filledBoxes.add(i*10+j);
                }
            }
        }
    }

    void fillDiagonalSquaresWithRandomValues() {
        for (int i = 0; i < 9; i += 3) {
            List<Integer> possibilities = new ArrayList<>(emptyBoxes);
            List<Integer> addresses = new ArrayList<>(Arrays.asList(0, 1, 2, 10, 11, 12, 20, 21, 22));
            for (Integer address : addresses) {
                Integer value = possibilities.get(randomGenerator.nextInt(possibilities.size()));
                sudoku[i + address / 10][i + address % 10].setBoxValueInteger(value);
                possibilities.remove(value);
            }
        }
    }

    boolean fillRandomBoxWithRandomValue() {
        resetEmptyBoxesList();
        int randomAddress;
        if (emptyBoxes.size() > 0) {
            randomAddress = randomGenerator.nextInt(emptyBoxes.size());
            int rowNumber = emptyBoxes.get(randomAddress)/10;
            int columnNumber = emptyBoxes.get(randomAddress)%10;
            this.emptyBoxes.remove(randomAddress);
            List<Integer> possibleValues = sudoku[rowNumber][columnNumber].getBoxValue();
            if (sudoku[rowNumber][columnNumber].getBoxValue().size() == 1) {
                sudoku[rowNumber][columnNumber].setBoxValueInteger(sudoku[rowNumber][columnNumber].getBoxValueInteger());
            } else {
                if (sudoku[rowNumber][columnNumber].getBoxValue().size() != 0) {
                    int randomValue = possibleValues.get(randomGenerator.nextInt(possibleValues.size()));
                    sudoku[rowNumber][columnNumber].setBoxValueInteger(randomValue);
                } else {
                    return false;
                }
            }

        }
        return true;
    }

    void removeBox() {
        resetFilledBoxesList();
        int randomAddress = randomGenerator.nextInt(filledBoxes.size());
        emptyBoxes.add(randomAddress);
    }

}
