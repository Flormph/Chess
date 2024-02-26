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
    boolean successful;
    String message;
}
