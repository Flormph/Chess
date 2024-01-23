package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public ChessPosition(int newrow, int newcol) {
        row = newrow;
        column = newcol;
    }

    int row = -1; // negative to throw an error if the location is not set
    int column = -1;

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column;
    }

    public ChessPosition edit(int newRow, int newColumn) {
        return new ChessPosition(row + newRow, column + newColumn);
    }

    public boolean isInBounds() {
        return 0 < row && row < 9 && 0 < column && column < 9;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
    @Override
    public String toString() {
        return "(" + getRow() + "," + getColumn() + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && column == that.column;
    }

}
