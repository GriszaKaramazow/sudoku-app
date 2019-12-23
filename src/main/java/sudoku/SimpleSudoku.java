package sudoku;

import java.util.List;

public class SimpleSudoku {

    private int[][] generatedSudoku;
    private int[][] solvedSudoku;
    private int emptyBoxes;

    public SimpleSudoku(int[][] solvedSudoku) {
        this.solvedSudoku = solvedSudoku;
    }

    public int[][] getGeneratedSudoku() {
        return generatedSudoku;
    }

    public int[][] getSolvedSudoku() {
        return solvedSudoku;
    }

    public int getEmptyBoxes() {
        return emptyBoxes;
    }

    int[][] createSudoku(List<Integer> emptyBoxes) {
        int[][] generatedSudoku = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (emptyBoxes.contains(row * 10 + column)) {
                    generatedSudoku[row][column] = 0;
                } else {
                    generatedSudoku[row][column] = solvedSudoku[row][column];
                }
            }
        }
        this.generatedSudoku = generatedSudoku;
        countEmptyBoxes();
        return generatedSudoku;
    }

    private void countEmptyBoxes() {
        int emptyBoxes = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (generatedSudoku[i][j] == 0) {
                    emptyBoxes++;
                }
            }
        }
        this.emptyBoxes = emptyBoxes;
    }

    void printSolvedSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (solvedSudoku[i][j] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("[" + solvedSudoku[i][j] + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void printGeneratedSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (generatedSudoku[i][j] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("[" + generatedSudoku[i][j] + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
