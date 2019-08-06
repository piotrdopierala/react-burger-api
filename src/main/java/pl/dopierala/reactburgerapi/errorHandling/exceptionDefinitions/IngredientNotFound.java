package pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions;

public class IngredientNotFound extends RuntimeException {
    public IngredientNotFound(String message) {
        super(message);
    }
}
