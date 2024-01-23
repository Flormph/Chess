package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        start = startPosition;
        end = endPosition;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        promoteTo = promotionPiece;
    }

    ChessPosition start;
    ChessPosition end;
    ChessPiece.PieceType promoteTo = null;

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promoteTo;
    }
    @Override
    public int hashCode() {
        return Objects.hash(start, end, promoteTo) * 7 * end.getRow();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove that = (ChessMove) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && promoteTo == that.promoteTo;
    }
    @Override
    public String toString() {
        return start.toString() + '\n' + end.toString() + '\n' + promoteTo + "\n\n";
    }
}
