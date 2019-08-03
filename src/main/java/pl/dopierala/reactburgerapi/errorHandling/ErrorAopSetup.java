package pl.dopierala.reactburgerapi.errorHandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.InsufficientDataException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorAopSetup {

    private ResponseEntity prepareResponseEntity(String simpleName, String message, RuntimeException e) {
        Map<String,Object> body = new HashMap<>();
        body.put("date:", LocalDateTime.now());
        body.put("exception:", simpleName);
        body.put("message:", message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(InsufficientDataException.class)
    public ResponseEntity insuffitientDataExceptionHandler(InsufficientDataException e, final HttpServletResponse response){
        return prepareResponseEntity(e.getClass().getSimpleName(), e.getMessage(),e);
    }
}
