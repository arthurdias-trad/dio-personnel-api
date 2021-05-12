package one.digitalinnovation.personnelapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.dto.mapper.PersonMapper;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.exception.PersonAlreadyRegisteredException;
import one.digitalinnovation.personnelapi.exception.PersonNotFoundException;
import one.digitalinnovation.personnelapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public Person create(PersonDTO personDTO) throws PersonAlreadyRegisteredException {
        if (verifyIfPersonExistsByCPF(personDTO.getCpf())) {
            throw new PersonAlreadyRegisteredException("CPF is already registered");
        }

        Person personToSave = personMapper.toModel(personDTO);

        return personRepository.save(personToSave);
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = verifyIfExistsByIdAndReturnPerson(id);
        return personMapper.toDto(person);
    }

    public List<PersonDTO> findAll() {
        List<Person> persons = this.personRepository.findAll();

        return persons.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws PersonNotFoundException {
        PersonDTO person = findById(id);
        this.personRepository.deleteById(id);
    }

    public void updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExistsByIdAndReturnPerson(id);

        Person personToUpdate = personMapper.toModel(personDTO);
        personToUpdate.setId(id);

        this.personRepository.save(personToUpdate);
    }

    private Person verifyIfExistsByIdAndReturnPerson(Long id) throws PersonNotFoundException {
        return this.personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));
    }

    public boolean verifyIfPersonExistsByCPF(String cpf) {
        return (personRepository.findByCpf(cpf).isPresent());
    }

}
