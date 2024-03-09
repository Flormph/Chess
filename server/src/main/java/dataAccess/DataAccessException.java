package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class  DataAccessException extends Exception{
    int code;

    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
}
