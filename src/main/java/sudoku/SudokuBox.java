package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SudokuBox {

    private final int rowNumber;
    private final int columnNumber;
    private List<Integer> boxValue = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    private boolean isGiven;
    private boolean isSolved;
    private boolean hasBeenSolvedRecently;
    private boolean hasBeenChangedRecently;

    SudokuBox(int rowNumber, int columnNumber, int boxValue) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        if (boxValue == 0) {
            this.isGiven = false;
            this.isSolved = false;
        } else {
            this.isGiven = true;
            this.isSolved = true;
            this.boxValue.clear();
            this.boxValue.add(boxValue);
        }
        this.hasBeenSolvedRecently = false;
        this.hasBeenChangedRecently = true;
    }

    List<Integer> getBoxValue() {
        return boxValue;
    }

    void setBoxValue(ArrayList<Integer> boxValue) {
        this.boxValue = boxValue;
        setIfChangesMade();
    }

    void setBoxEmpty() {
        this.boxValue.clear();
    }

    void resetBox() {
        this.boxValue = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        this.isSolved = false;
        this.isGiven = false;
        this.hasBeenSolvedRecently = false;
        this.hasBeenChangedRecently = true;
    }

    int getBoxValueInteger() { // returns integer if box is solved
        int integerBoxValue;
        if (boxValue.size() == 1) {
            integerBoxValue = boxValue.get(0);
        } else {
            integerBoxValue = 0;
        }
        return integerBoxValue;
    }

    void setBoxValueInteger(int boxValueInteger) {
        List<Integer> boxValue = new ArrayList<>();
        boxValue.add(boxValueInteger);
        this.boxValue = boxValue;
        setIfChangesMade();
    }

    String getBoxValueString() {
        String stringBoxValue;
        if (boxValue.size() == 1) {
            stringBoxValue = String.valueOf(boxValue.get(0));
        } else {
            stringBoxValue = " ";
        }
        return stringBoxValue;
    }

    boolean isGiven() {
        return isGiven;
    }

    boolean isSolved() {
        return isSolved;
    }

    boolean hasBeenChangedRecently() {
        return hasBeenChangedRecently;
    }

    void setHasBeenChangedRecently(boolean hasBeenChangedRecently) {
        this.hasBeenChangedRecently = hasBeenChangedRecently;
    }

    boolean hasBeenSolvedRecently() {
        return hasBeenSolvedRecently;
    }

    void setHasBeenSolvedRecently(boolean hasBeenSolvedRecently) {
        this.hasBeenSolvedRecently = hasBeenSolvedRecently;
    }

    int getRowNumber() {
        return rowNumber;
    }

    int getColumnNumber() {
        return columnNumber;
    }

    private void setIfChangesMade() {
        this.hasBeenChangedRecently = true;
        if (this.boxValue.size() == 1) {
            this.isSolved = true;
            this.hasBeenSolvedRecently = true;
        }
    }

}
