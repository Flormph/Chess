package server.login;

 /* ClearApplicationResponse - stores possible responses for ClearApplication
         */
public class Response extends server.extenders.Response{
    public Response() {super();}
    /**
     * Constructor - fail case, sets success to "false"
     * @param errorMessage describes the error
     */
    public Response(String errorMessage) {
        super(errorMessage);
    }
    public Response(String username, String token) { super(username, token); }

}
