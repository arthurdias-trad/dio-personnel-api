package one.digitalinnovation.personnelapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
