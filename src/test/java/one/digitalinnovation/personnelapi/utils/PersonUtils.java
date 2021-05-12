package one.digitalinnovation.personnelapi.utils;

import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.entity.Person;

import java.time.LocalDate;
import java.util.Collections;

public class PersonUtils {

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Teste";
    private static final String LAST_NAME = "da Silva";
    private static final String BIRTHDATE = "01-01-1970";
    private static final String CPF = "35109652058";


    public static PersonDTO createPersonDTO() {
        return PersonDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .cpf(CPF)
                .birthDate(BIRTHDATE)
                .phones(Collections.singletonList(PhoneUtils.createPhoneEntity()))
                .build();
    }

    public static Person createPersonEntity() {
        return Person.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .cpf(CPF)
                .birthDate(LocalDate.of(1970, 1, 1))
                .phones(Collections.singletonList(PhoneUtils.createPhoneEntity()))
                .build();
    }

}
