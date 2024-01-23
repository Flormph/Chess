package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        ChessPiece[][] board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard new_board) {
        board = new_board.getBoard();
    }

    ChessPiece[][] board = new ChessPiece[8][8]; //new board

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) { //takes an already instantiated piece and places it on the board
        if(board == null) {
             board = new ChessPiece[8][8];
        }
        if(getPiece(position) == null) { //checks if the location is empty (if not it will return null
            board[position.getRow() - 1][position.getColumn() - 1]  = (ChessPiece) piece;
        }
        else {
            return; // maybe throw an exception later
        }
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) { //returns the piece at given position
        if(position.isInBounds()) {
            return board[position.getRow() - 1][position.getColumn() - 1];
        }
        else {
            return null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8]; // Clears board by newing up a new one (eat your heart out c++)

        //This portion instantiates the piece types for easier coding later on (it actually adds lines but it makes the code more readable
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

        for(int i = 0; i < 2; ++i) { //Places the horizontally-symmetric pieces
            addPiece(new ChessPosition(1, 7 * i + 1), whiteRook);
            addPiece(new ChessPosition(8, 7 * i + 1), blackRook);
            addPiece(new ChessPosition(1, 5 * i + 2), whiteKnight);
            addPiece(new ChessPosition(8, 5 * i + 2), blackKnight);
            addPiece(new ChessPosition(1, 3 * i + 3), whiteBishop);
            addPiece(new ChessPosition(8, 3 * i + 3), blackBishop);
        }

        //Places non-horizontally-symmetric pieces
        addPiece(new ChessPosition(1, 4), whiteQueen);
        addPiece(new ChessPosition(8, 4), blackQueen);
        addPiece(new ChessPosition(1, 5), whiteKing);
        addPiece(new ChessPosition(8, 5), blackKing);

        //Places pawn rows
        Arrays.fill(board[1], whitePawn);
        Arrays.fill(board[6], blackPawn);

        System.out.print(toString());
        return;
    }

    public ChessPosition findWhiteKing() {
        for(int i = 1; i < 9; ++i) {
            for(int j = 1; j < 9; ++j) {
                ChessPosition currPos = new ChessPosition(i,j);
                if(getPiece(currPos) != null) {
                    if (getPiece(currPos).getPieceType() == ChessPiece.PieceType.KING && getPiece(currPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        return currPos;
                    }
                }
            }
        }
        return null;
    }

    public ChessPosition findBlackKing() {
        for(int i = 1; i < 9; ++i) {
            for(int j = 1; j < 9; ++j) {
                ChessPosition currPos = new ChessPosition(i,j);
                if(getPiece(currPos) != null) {
                    if (getPiece(currPos).getPieceType() == ChessPiece.PieceType.KING && getPiece(currPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        return currPos;
                    }
                }
            }
        }
        return null;
    }

    public void setPiece(ChessPosition pos, ChessPiece piece) {
        board[pos.getRow() - 1][pos.getColumn() - 1] = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(int i = 8; i > 0; --i) {
            for(int j = 1; j < 9; ++j) {
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece currPiece = (ChessPiece) getPiece(currPos);
                out.append('|');
                if(currPiece != null) {
                    out.append(getPiece(currPos).toString());
                }
                else {
                    out.append(" ");
                }
                if(j == 8) {out.append('|');}
            }
            out.append('\n');
        }
        return out.toString();
    }
}
