package pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions;

public class CustomerEmailAlreadyRegisteredException extends RuntimeException {
    public CustomerEmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
