package exceptions;

/**
 * Custom exception for invalid API responses.
 * Thrown when the API response structure is invalid or cannot be parsed.
 */
public class InvalidResponseException extends ApiException {

    public InvalidResponseException(String message) {
        super(message);
    }
}


