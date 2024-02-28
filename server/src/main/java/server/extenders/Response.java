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
        returnString1 = returnString;
        this.message = null;
    }

    protected Response(String return1, String return2) {
        successful = true;
        returnString1 = return1;
        returnString2 = return2;
        this.message = null;
    }
    boolean successful;
    String message;
    String returnString1;
    String returnString2;
}
