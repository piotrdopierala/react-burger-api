package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.CustomerEmailAlreadyRegisteredException;
import pl.dopierala.reactburgerapi.model.customer.CustomerDTO;
import pl.dopierala.reactburgerapi.service.CustomerService;

import java.io.IOException;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerServiceMock;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void Should_signUp_throw_ResponseStatusException_BadRequest_400() throws Exception {
        //given
        String jsonWithoutPassword = "";
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("email", "testEmail");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeString(jsonWithoutPassword);
        jsonGenerator.close();
        jsonWithoutPassword = writer.toString();
        //when
        //then
        mockMvc.perform(
                post("/burger/api/cust/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWithoutPassword))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void Should_signUp_same_email_throw_ResponseStatusException_Conflict_409() throws Exception {
        //given
        final String emailAddressThatExists = "testSameEmail";

        String jsonValidEmailAndPassword = "";
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("email", emailAddressThatExists);
        jsonGenerator.writeStringField("password", "testPassword");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeString(jsonValidEmailAndPassword);
        jsonGenerator.close();
        jsonValidEmailAndPassword = writer.toString();
        //when
        doThrow(CustomerEmailAlreadyRegisteredException.class).when(customerServiceMock).saveNewCustomer(any());
        //then
        mockMvc.perform(
                post("/burger/api/cust/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonValidEmailAndPassword))
                .andExpect(status().isConflict());
    }
}