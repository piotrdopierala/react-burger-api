package pl.dopierala.reactburgerapi.model.ingredient;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IngredientSerializerIngrNameOnly extends JsonSerializer<Ingredient> {
    @Override
    public void serialize(Ingredient value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(value.getName());
    }
}
