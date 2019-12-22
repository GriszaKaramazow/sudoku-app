package sudoku;

import java.util.*;

class Sudoku {

    private SudokuBox[][] sudoku = new SudokuBox[9][9];

    private List<Integer> emptyBoxes = new ArrayList<>();
    private List<Integer> filledBoxes = new ArrayList<>();
    private List<Integer> emptyBoxValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    private long solvingTime;
    private int solvingStepsCount;

    Sudoku(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuBox sudokuBox = new SudokuBox(i, j, sudoku[i][j]);
                this.sudoku[i][j] = sudokuBox;
            }
        }
    }

    Sudoku() { // generates empty sudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuBox sudokuBox = new SudokuBox(i, j, 0);
                sudoku[i][j] = sudokuBox;
            }
        }
    }

    int[][] generateSimpleSudoku() {
        int[][] simpleSudoku = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                simpleSudoku[i][j] = getSudokuBoxValueInteger(i,j);
            }
        }
        return simpleSudoku;
    }

    long getSolvingTime() {
        return solvingTime;
    }

    void setSolvingTime(long solvingTime) {
        this.solvingTime = solvingTime;
    }

    int getSolvingStepsCount() {
        return solvingStepsCount;
    }

    void setSolvingStepsCount(int solvingStepsCount) {
        this.solvingStepsCount = solvingStepsCount;
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
                    this.emptyBoxes.add(i*10+j);
                }
            }
        }
    }

    void addToEmptyBoxes(int address) {
        this.emptyBoxes.add(address);
    }

    void addToEmptyBoxes(int row, int column) {
        this.emptyBoxes.add(row*10+column);
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
                if (sudoku[i][j].getBoxValueInteger() != 0) {
                    this.filledBoxes.add(i*10+j);
                }
            }
        }
    }

    public void addToFilledBoxes(int row, int column) {
        this.filledBoxes.add(row*10+column);
    }

    void removeFromFilledBoxes(int index) {
        this.filledBoxes.remove(index);
    }

    void resetSudokuBox(int row, int column) {
        sudoku[row][column].resetBox();
    }

    SudokuBox[][] getSudoku() {
        return sudoku;
    }

    void removeAllEmptyBoxes() {
        for (Integer emptyBox : emptyBoxes) {
            sudoku[filledBoxes.get(emptyBox)/10][filledBoxes.get(emptyBox)%10].resetBox();
        }
    }

}
