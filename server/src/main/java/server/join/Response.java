package server.join;

import chess.ChessGame;
import model.Records;

public class Response extends server.extenders.Response{
    public Response() {super();}
    /**
     * Constructor - fail case, sets success to "false"
     * @param errorMessage describes the error
     */
    public Response(String errorMessage) {
        super(errorMessage);
    }
    public Response(Records.GameData gameData, ChessGame game) {super(gameData ,game); }

}
