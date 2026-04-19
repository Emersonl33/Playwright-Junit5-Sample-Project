package advantageonlineshopping.com.exceptions;

/**
 * Custom exception for API-related errors.
 * Used when API communication or processing fails.
 */
public class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

