package pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions;

public class InsufficientDataException extends RuntimeException {
    public InsufficientDataException(String message) {
        super(message);
    }
}
