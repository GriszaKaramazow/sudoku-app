package sudoku;

import java.util.*;

class Sudoku {

    private SudokuBox[][] sudoku = new SudokuBox[9][9];

    private List<Integer> emptyBoxes = new ArrayList<>();
    private List<Integer> filledBoxes = new ArrayList<>();

    Sudoku(int[][] sudoku) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                SudokuBox sudokuBox = new SudokuBox(row, column, sudoku[row][column]);
                this.sudoku[row][column] = sudokuBox;
            }
        }
    }

    Sudoku() { // generates empty sudoku
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                SudokuBox sudokuBox = new SudokuBox(row, column, 0);
                this.sudoku[row][column] = sudokuBox;
            }
        }
    }

    int[][] generateSimpleSudoku() {
        int[][] simpleSudoku = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].isSolved()) {
                    simpleSudoku[i][j] = getSudokuBoxValueInteger(i, j);
                } else {
                    simpleSudoku[i][j] = 0;
                }
            }
        }
        return simpleSudoku;
    }

    SudokuBox getSudokuBox(int row, int column) {
        return sudoku[row][column];
    }

    void setSudokuBox(SudokuBox sudokuBox) {
        sudoku[sudokuBox.getRowNumber()][sudokuBox.getColumnNumber()] = sudokuBox;
    }

    List<Integer> getSudokuBoxValue(int row, int column) {
        return sudoku[row][column].getBoxValue();
    }

    public void setSudokuBoxValue(int i, int j, ArrayList<Integer> values) {
        sudoku[i][j].setBoxValue(values);
    }

    int getSudokuBoxValueInteger(int row, int column) {
        return sudoku[row][column].getBoxValueInteger();
    }

    void setSudokuBoxValueInteger(int row, int column, int value) {
        sudoku[row][column].setBoxValueInteger(value);
    }

    List<Integer> getEmptyBoxes() {
        return emptyBoxes;
    }

    void resetEmptyBoxesList() {
        this.emptyBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku[i][j].isSolved()) {
                    this.emptyBoxes.add(i * 10 + j);
                }
            }
        }
    }

    void addToEmptyBoxes(int address) {
        emptyBoxes.add(address);
    }

    void addToEmptyBoxes(int row, int column) {
        if (!emptyBoxes.contains(row * 10 + column)) {
            emptyBoxes.add(row * 10 + column);
        }
    }

    void addToEmptyBoxes(List<Integer> emptyBoxes) {
        for (Integer emptyBox : emptyBoxes) {
            int row = emptyBox / 10;
            int column = emptyBox % 10;
            addToEmptyBoxes(row, column);
        }
    }

    void removeFromEmptyBoxes(int index) {
        this.emptyBoxes.remove(index);
    }

    List<Integer> getFilledBoxes() {
        return filledBoxes;
    }

    void resetFilledBoxesList() {
        this.filledBoxes.clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j].getBoxValue().size() == 1) {
                    this.filledBoxes.add(i * 10 + j);
                }
            }
        }
    }

    public void addToFilledBoxes(int row, int column) {
        this.filledBoxes.add(row * 10 + column);
    }

    void removeFromFilledBoxes(int index) {
        this.filledBoxes.remove(index);
    }

    void resetSudokuBox(int row, int column) {
        sudoku[row][column].resetBox();
    }

    void removeAllEmptyBoxes() {
        if (emptyBoxes.size() == 0) {
            return;
        }
        for (Integer emptyBox : emptyBoxes) {
            int row = emptyBox / 10;
            int column = emptyBox % 10;
            sudoku[row][column].resetBox();
        }
    }

}
