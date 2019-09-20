package pl.dopierala.reactburgerapi.model.customer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomerSerializerIdAndEmailOnly extends JsonSerializer<Customer> {
    @Override
    public void serialize(Customer customer, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id",customer.getId());
        gen.writeStringField("email",customer.getEmail());
        gen.writeEndObject();
    }
}
