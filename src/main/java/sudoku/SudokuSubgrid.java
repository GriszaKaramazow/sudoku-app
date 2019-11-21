package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SudokuSubgrid {

    private SudokuBox[] sudokuSubgrid = new SudokuBox[9];
    private int sudokuBoxesCount = 0;

    void addSudokuBox(SudokuBox sudokuBox) { // adds a box into the subgrid
        sudokuSubgrid[sudokuBoxesCount++] = sudokuBox;
    }

    SudokuBox[] getSudokuSubgrid() {
        return sudokuSubgrid;
    }

    void reduceUnknownValues() {
        List<Integer> singleIntegersArrayList = new ArrayList<>();
        for (SudokuBox sudokuBox : sudokuSubgrid) {
            if (sudokuBox.isSolved()) {
                singleIntegersArrayList.add(sudokuBox.getBoxValueInteger());
            }
        }

        for (SudokuBox sudokuBox : sudokuSubgrid) {
            if (!sudokuBox.isSolved()) {
                ArrayList<Integer> reducedArrayList = new ArrayList<>(sudokuBox.getBoxValue());
                reducedArrayList.removeAll(singleIntegersArrayList);
                if (reducedArrayList.size() != sudokuBox.getBoxValue().size()) {
                    sudokuBox.setBoxValue(reducedArrayList);
                }

            }
        }
    }

    void checkIfDigitAppearsOnce() {
        for (int i = 0; i < sudokuSubgrid.length; i++) {
            if (!sudokuSubgrid[i].isSolved()) {
                Set<Integer> currentBoxValues = new HashSet<>(sudokuSubgrid[i].getBoxValue());
                Set<Integer> remainingBoxesValues = new HashSet<>();
                for (int j = 0; j < sudokuSubgrid.length; j++) {
                    if (i != j) {
                        remainingBoxesValues.addAll(sudokuSubgrid[j].getBoxValue());
                    }
                }
                currentBoxValues.removeAll(remainingBoxesValues);
                if (currentBoxValues.size() == 1) {
                    sudokuSubgrid[i].setBoxValue(new ArrayList<>(currentBoxValues));
                }
            }
        }
    }

    boolean checkIfSudokuGridIsSolvedProperly() {
        Set<Integer> boxesValues = new HashSet<>();
        for (SudokuBox sudokuBox : sudokuSubgrid) {
            if (sudokuBox.getBoxValue().size() != 1) {
                return true;
            } else {
                boxesValues.add(sudokuBox.getBoxValueInteger());
            }
        }
        return boxesValues.size() != 9;
    }

    public String toString() {
        StringBuilder sudokuSubgridString = new StringBuilder();
        for (SudokuBox sudokuBox : sudokuSubgrid) {
            sudokuSubgridString.append(sudokuBox.getBoxValue().toString());
        }
        return sudokuSubgridString.toString();
    }

}
