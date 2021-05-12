package one.digitalinnovation.personnelapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonAlreadyRegisteredException extends Exception {
    public PersonAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }
}
