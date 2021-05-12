package one.digitalinnovation.personnelapi;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.entity.Phone;
import one.digitalinnovation.personnelapi.enums.PhoneType;
import one.digitalinnovation.personnelapi.repository.PersonRepository;
import one.digitalinnovation.personnelapi.service.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PersonnelApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonnelApplication.class, args);

	}




}
