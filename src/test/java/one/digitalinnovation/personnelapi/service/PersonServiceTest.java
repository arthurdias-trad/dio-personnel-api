package one.digitalinnovation.personnelapi.service;

import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.dto.mapper.PersonMapper;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.entity.Phone;
import one.digitalinnovation.personnelapi.enums.PhoneType;
import one.digitalinnovation.personnelapi.exception.PersonAlreadyRegisteredException;
import one.digitalinnovation.personnelapi.exception.PersonNotFoundException;
import one.digitalinnovation.personnelapi.repository.PersonRepository;
import one.digitalinnovation.personnelapi.utils.PersonUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    private Long VALID_ID = 1L;
    private Long INVALID_ID = 50L;

    @Test
    void whenPersonInformedThenItShouldBeCreated() throws PersonAlreadyRegisteredException {
        // Given

        PersonDTO personDTO = PersonUtils.createPersonDTO();
        Person personToSave = personMapper.toModel(personDTO);
        Person expectedSavedPerson = PersonUtils.createPersonEntity();


        //when
        when(personRepository.findByCpf(personDTO.getCpf())).thenReturn(Optional.empty());
        when(personRepository.save(personToSave)).thenReturn(expectedSavedPerson);

        // then
        Person savedPerson = personService.create(personDTO);

        assertThat(savedPerson.getId(), is(equalTo(expectedSavedPerson.getId())));
        assertThat(savedPerson.getCpf(), is(equalTo(expectedSavedPerson.getCpf())));
        assertThat(savedPerson.getBirthDate(), is(equalTo(expectedSavedPerson.getBirthDate())));

    }

    @Test
    void whenPersonInformedWithDuplicateCPFThenExceptionShouldBeThrown() throws PersonAlreadyRegisteredException {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();
        Person duplicatedPerson = personMapper.toModel(personDTO);

        //when
        when(personRepository.findByCpf(personDTO.getCpf())).thenReturn(Optional.of(duplicatedPerson));


        // then
        assertThrows(PersonAlreadyRegisteredException.class, () -> personService.create(personDTO));
    }

    @Test
    void whenValidIDInformedThenPersonShouldBeReturned() throws PersonNotFoundException {
        // Given
        Person expectedPersonFound = PersonUtils.createPersonEntity();

        // When
        when(personRepository.findById(VALID_ID)).thenReturn(Optional.of(expectedPersonFound));

        // Then
        PersonDTO personFound = personService.findById(VALID_ID);

        assertThat(personFound.getId(), is(equalTo(expectedPersonFound.getId())));
        assertThat(personFound.getCpf(), is(equalTo(expectedPersonFound.getCpf())));
        assertThat(personFound.getBirthDate(), is(equalTo(expectedPersonFound.getBirthDate().toString())));

    }

    @Test
    void whenInvalidIDInformedThenExceptionShouldBeThrown() {

        // When
        when(personRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        // Then
        assertThrows(PersonNotFoundException.class, () -> personService.findById(INVALID_ID));
    }

    @Test
    void whenFindAllIsCalledThenAListOfPersonDTOsShouldBeReturned() {
        // Given
        Person mockPerson = PersonUtils.createPersonEntity();
        PersonDTO mockPersonDTO = personMapper.toDto(mockPerson);
        List<PersonDTO> expectedPersonDTOList = Collections.singletonList(mockPersonDTO);

        // When
        when(personRepository.findAll()).thenReturn(Collections.singletonList(mockPerson));

        // Then
        List<PersonDTO> foundPersonDTOList = personService.findAll();

        assertThat(foundPersonDTOList, is(not(empty())));
        assertThat(foundPersonDTOList.size(), is(equalTo(expectedPersonDTOList.size())));
        assertThat(foundPersonDTOList.get(0), is(equalTo(expectedPersonDTOList.get(0))));
    }

    @Test
    void whenDeleteIsCalledWithValidIDThenPersonShouldBeDeleted() throws PersonNotFoundException {
        // Given
        Person personToDelete = PersonUtils.createPersonEntity();

        // When
        when(personRepository.findById(VALID_ID)).thenReturn(Optional.of(personToDelete));
        doNothing().when(personRepository).deleteById(VALID_ID);

        // Then
        personService.deleteById(VALID_ID);

        verify(personRepository, times(1)).findById(personToDelete.getId());
        verify(personRepository, times(1)).deleteById(personToDelete.getId());
    }

    @Test
    void whenDeleteIsCalledWithInvalidIDThenExceptionShouldBeThrown() {
        // When
        when(personRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        // Then
        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(INVALID_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidIDThenUpdatePerson() throws PersonNotFoundException {
        // Given
        Person personToUpdate = PersonUtils.createPersonEntity();
        PersonDTO personToUpdateDTO = PersonUtils.createPersonDTO();

        // When
        when(personRepository.findById(VALID_ID)).thenReturn(Optional.of(personToUpdate));
        when(personRepository.save(personToUpdate)).thenReturn(personToUpdate);

        // Then
        personService.updateById(VALID_ID, personToUpdateDTO);

        verify(personRepository, times(1)).findById(VALID_ID);
        verify(personRepository, times(1)).save(personToUpdate);
    }

    @Test
    void whenUpdateIsCalledWithInvalidIDThenExceptionShouldBeThrown() {
        // Given
        PersonDTO personToUpdateDTO = PersonUtils.createPersonDTO();

        // When
        when(personRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        // Then
        assertThrows(PersonNotFoundException.class, () -> personService.updateById(INVALID_ID, personToUpdateDTO));
    }
}
