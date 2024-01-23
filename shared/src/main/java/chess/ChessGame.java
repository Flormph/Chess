package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor currTeam = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();
    HashSet<ChessPosition> whitePieces = new HashSet<ChessPosition>();
    HashSet<ChessPosition> blackPieces = new HashSet<ChessPosition>();
    HashSet<ChessPosition> dummyWhitePieces = new HashSet<ChessPosition>();
    HashSet<ChessPosition> dummyBlackPieces = new HashSet<ChessPosition>();

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece = board.getPiece(startPosition);
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        if(currPiece == null) {
            return null;
        }
        currTeam = currPiece.getTeamColor(); //FIXME FOR TESTING
        //if(currPiece.getTeamColor() != currTeam) {
        //    return null;
        //}
        HashSet<ChessMove> pieceMoves = new HashSet<ChessMove>(board.getPiece(startPosition).pieceMoves(board, startPosition));
        for(ChessMove p : pieceMoves) {
            if(validate(p)) {
                validMoves.add(p);
            }
        }
        return validMoves;
    }

    private boolean validate(ChessMove p) {
        ChessBoard tempBoard = new ChessBoard(board);
        tempBoard.setPiece(p.getEndPosition(), (ChessPiece)tempBoard.getPiece(p.getStartPosition()));
        tempBoard.setPiece(p.getStartPosition(), null);
        if(currTeam == TeamColor.WHITE) {
            if(isInCheck(TeamColor.WHITE, tempBoard)) {return false;}
        }
        if(currTeam == TeamColor.BLACK) {
            if(isInCheck(TeamColor.BLACK, tempBoard)) {return false;}
        }

        return true;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != currTeam) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> myMoves = validMoves(move.getStartPosition());
        if(myMoves != null) {
            if (myMoves.contains(move)) {
                if(move.getPromotionPiece() != null) {
                    board.setPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
                }
                else {
                    board.setPiece(move.getEndPosition(), (ChessPiece) board.getPiece(move.getStartPosition()));
                }
                board.setPiece(move.getStartPosition(), null);
            }
            else {
                throw new InvalidMoveException("YOUR TRIED BUT YOU LIED YOUR PIECE IS WRONG (invalid move)");
            }
        }
        else {
            throw new InvalidMoveException("YOUR TRIED BUT YOU LIED YOUR PIECE IS WRONG (invalid move)");
        }
        changeTeam();
        return;
    }

    public void changeTeam() {
        if(currTeam == TeamColor.WHITE) {
            currTeam = TeamColor.BLACK;
        }
        else {
            currTeam = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        checkBoardPieces();
        ChessPosition whiteKingPos = board.findWhiteKing();
        ChessPosition blackKingPos = board.findBlackKing();
        if(teamColor == TeamColor.WHITE) {
            for(ChessPosition pos : blackPieces) {
                for(ChessMove mov : board.getPiece(pos).pieceMoves(board,pos)){
                    ChessPosition currMove = mov.getEndPosition();
                    if(Objects.equals(currMove, (ChessPosition)whiteKingPos)) {
                        return true;
                    }
                }
            }
        }
        if(teamColor == TeamColor.BLACK) {
            for(ChessPosition pos : whitePieces) {
                for(ChessMove mov : board.getPiece(pos).pieceMoves(board,pos)){
                    ChessPosition currMove = mov.getEndPosition();
                    if(Objects.equals(currMove, (ChessPosition)blackKingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        checkBoardPieces(board);
        ChessPosition whiteKingPos = board.findWhiteKing();
        ChessPosition blackKingPos = board.findBlackKing();
        if(teamColor == TeamColor.WHITE) {
            for(ChessPosition pos : dummyBlackPieces) {
                for(ChessMove mov : board.getPiece(pos).pieceMoves(board,pos)){
                    ChessPosition currMove = mov.getEndPosition();
                    if(Objects.equals(currMove, (ChessPosition)whiteKingPos)) {
                        return true;
                    }
                }
            }
        }
        if(teamColor == TeamColor.BLACK) {
            for(ChessPosition pos : dummyWhitePieces) {
                for(ChessMove mov : board.getPiece(pos).pieceMoves(board,pos)){
                    ChessPosition currMove = mov.getEndPosition();
                    if(Objects.equals(currMove, (ChessPosition)blackKingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            return false;
        }
        if(teamColor == TeamColor.WHITE) {
            for(ChessPosition cp : whitePieces) {
                Collection<ChessMove> myMoves = validMoves(cp);
                if(myMoves != null) {
                    if(!validMoves(cp).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        if(teamColor == TeamColor.BLACK) {
            for(ChessPosition cp : blackPieces) {
                Collection<ChessMove> myMoves = validMoves(cp);
                if(myMoves != null) {
                    if(!validMoves(cp).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }
        if(teamColor == TeamColor.WHITE) {
            for(ChessPosition cp : whitePieces) {
                Collection<ChessMove> myMoves = validMoves(cp);
                if(myMoves != null) {
                    if(!validMoves(cp).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        if(teamColor == TeamColor.BLACK) {
            for(ChessPosition cp : blackPieces) {
                Collection<ChessMove> myMoves = validMoves(cp);
                if(myMoves != null) {
                    if(!validMoves(cp).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = (ChessBoard) board;
        checkBoardPieces();
    }

    public void resetBoard(ChessBoard board) {
        board.resetBoard();
        checkBoardPieces();
    }

    private void checkBoardPieces() {
        blackPieces.clear();
        whitePieces.clear();
        for(int i = 1; i < 9; ++i) {
            for(int j = 1; j < 9; ++j) {
                ChessPosition currPos = new ChessPosition(i,j);
                if(board.getPiece(currPos) != null) {
                    if(board.getPiece(currPos).getTeamColor() == TeamColor.WHITE) {
                        whitePieces.add(currPos);
                    }
                    if(board.getPiece(currPos).getTeamColor() == TeamColor.BLACK) {
                        blackPieces.add(currPos);
                    }
                }
            }
        }
    }

    private void checkBoardPieces(ChessBoard board) {
        dummyBlackPieces.clear();
        dummyWhitePieces.clear();
        for(int i = 1; i < 9; ++i) {
            for(int j = 1; j < 9; ++j) {
                ChessPosition currPos = new ChessPosition(i,j);
                if(board.getPiece(currPos) != null) {
                    if(board.getPiece(currPos).getTeamColor() == TeamColor.WHITE) {
                        dummyWhitePieces.add(currPos);
                    }
                    if(board.getPiece(currPos).getTeamColor() == TeamColor.BLACK) {
                        dummyBlackPieces.add(currPos);
                    }
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return (ChessBoard) board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame that = (ChessGame) o;
        return currTeam == that.currTeam && Objects.equals(board, that.board) && Objects.equals(whitePieces, that.whitePieces) && Objects.equals(blackPieces, that.blackPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currTeam, board, whitePieces, blackPieces);
    }


}
