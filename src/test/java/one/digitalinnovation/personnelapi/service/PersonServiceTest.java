package one.digitalinnovation.personnelapi.service;

import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.dto.mapper.PersonMapper;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.entity.Phone;
import one.digitalinnovation.personnelapi.enums.PhoneType;
import one.digitalinnovation.personnelapi.exception.PersonAlreadyRegisteredException;
import one.digitalinnovation.personnelapi.repository.PersonRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Test
    void whenPersonInformedThenItShouldBeCreated() throws PersonAlreadyRegisteredException {
        // Given
        List<Phone> phones = List.of(new Phone(null, PhoneType.HOME, "2345678"));
        PersonDTO personDTO = new PersonDTO(null, "Teste", "da Silva", "35109652058", "01-01-1970", phones);
        Person expectedSavedPerson = personMapper.toModel(personDTO);

        //when
        when(personRepository.findByCpf(personDTO.getCpf())).thenReturn(Optional.empty());
        when(personRepository.save(expectedSavedPerson)).thenReturn(expectedSavedPerson);

        // then
        Person savedPerson = personService.create(personDTO);

        MatcherAssert.assertThat(savedPerson.getId(), Matchers.is(expectedSavedPerson.getId()));
        MatcherAssert.assertThat(savedPerson.getCpf(), Matchers.is(expectedSavedPerson.getCpf()));

    }

    @Test
    void whenPersonInformedWithDuplicateCPFThenExceptionShouldBeThrown() throws PersonAlreadyRegisteredException {
        // Given
        List<Phone> phones = List.of(new Phone(null, PhoneType.HOME, "2345678"));
        PersonDTO personDTO = new PersonDTO(null, "Teste", "da Silva", "35109652058", "01-01-1970", phones);
        Person duplicatedPerson = personMapper.toModel(personDTO);

        //when
        when(personRepository.findByCpf(personDTO.getCpf())).thenReturn(Optional.of(duplicatedPerson));


        // then
        Assertions.assertThrows(PersonAlreadyRegisteredException.class, () -> personService.create(personDTO));
    }
}
