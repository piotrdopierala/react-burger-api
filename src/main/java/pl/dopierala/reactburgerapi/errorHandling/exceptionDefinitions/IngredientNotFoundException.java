package pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String message) {
        super(message);
    }
}
