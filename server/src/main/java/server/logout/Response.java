package server.logout;

public class Response extends server.extenders.Response{
    public Response() {super();}
    /**
     * Constructor - fail case, sets success to "false"
     * @param errorMessage describes the error
     */
    public Response(String errorMessage) {
        super(errorMessage);
    }

}
