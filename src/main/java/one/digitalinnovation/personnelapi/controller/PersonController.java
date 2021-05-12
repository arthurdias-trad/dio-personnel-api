package one.digitalinnovation.personnelapi.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personnelapi.dto.MessageResponseDTO;
import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.exception.PersonAlreadyRegisteredException;
import one.digitalinnovation.personnelapi.exception.PersonNotFoundException;
import one.digitalinnovation.personnelapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/test")
    public MessageResponseDTO test() {
        return MessageResponseDTO
                .builder()
                .message("Testing!")
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid PersonDTO personDTO)
            throws PersonAlreadyRegisteredException {
        Person savedPerson = personService.create(personDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPerson.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> findById(@PathVariable Long id) throws PersonNotFoundException {
        PersonDTO personDTO = this.personService.findById(id);

        return ResponseEntity.ok(personDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PersonNotFoundException {
        this.personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid PersonDTO personDTO)
            throws PersonNotFoundException {
        this.personService.updateById(id, personDTO);
        return ResponseEntity.noContent().build();
    }

}
