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

    protected Response(int dummy, String returnObject) {
        successful = true;
        this.returnObject = returnObject;
        this.message = null;
    }
    boolean successful;
    String message;
    String returnObject;
}
