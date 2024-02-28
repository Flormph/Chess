package server.list;

import model.Records;

import java.util.ArrayList;
import java.util.HashSet;

public class Response extends server.extenders.Response{
    public Response() {super();}
    /**
     * Constructor - fail case, sets success to "false"
     * @param errorMessage describes the error
     */
    public Response(String errorMessage) {
        super(errorMessage);
    }
    public Response(HashSet<Records.GameData> games) {super(games); }

}
