package server.extenders;

public class Response {
    /**
     * default constructor for parent class;
     */
    protected Response() {
        successful = true;
    }

    /**
     * Constructor for parent class (sets success to fail)
     * @param errorMessage message for the failure
     */
    protected Response(String errorMessage) {
        successful = false;
        message = errorMessage;
    }

    protected Response(int dummy, String returnString) {
        successful = true;
        authToken = returnString;
        this.message = null;
    }

    protected Response(String return1, String return2) {
        successful = true;
        username = return1;
        authToken = return2;
        this.message = null;
    }

    protected Response(int return1) {
        successful = true;
        gameID = Integer.toString(return1);
        this.message = null;
        username = null;
        authToken = null;
    }
    boolean successful;
    String message;
    String username;
    String authToken;
    String gameID;
}
