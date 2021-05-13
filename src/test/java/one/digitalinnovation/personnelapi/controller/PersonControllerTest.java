package one.digitalinnovation.personnelapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.digitalinnovation.personnelapi.dto.PersonDTO;
import one.digitalinnovation.personnelapi.entity.Person;
import one.digitalinnovation.personnelapi.exception.PersonAlreadyRegisteredException;
import one.digitalinnovation.personnelapi.exception.PersonNotFoundException;
import one.digitalinnovation.personnelapi.service.PersonService;
import one.digitalinnovation.personnelapi.utils.JsonConversionUtils;
import one.digitalinnovation.personnelapi.utils.PersonUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String SERVER_PATH = "http://localhost";
    private static final String PERSON_API_URL_PATH = "/api/v1/person";
    private static final long VALID_ID = 1;
    private static final long INVALID_ID = 50L;

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTisCalledWithValidDTOThenPersonIsCreatedAndStatusCreatedIsReturned()
            throws Exception {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();
        Person savedPerson = PersonUtils.createPersonEntity();

        // When
        when(personService.create(personDTO)).thenReturn(savedPerson);

        // Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personDTO)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(SERVER_PATH + PERSON_API_URL_PATH + "/" + savedPerson.getId()))
        ;
    }

    @Test
    void whenPOSTIsCalledWithDuplicateCPFThenStatusBadRequestIsReturned() throws Exception {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();

        // When
        when(personService.create(personDTO)).thenThrow(PersonAlreadyRegisteredException.class);

        // Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPOSTIsCalledWithInvalidFieldThenStatusBadRequestIsReturned() throws Exception {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();
        personDTO.setFirstName(null);

        // Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenPersonDTOAndStatusOkAreReturned() throws Exception {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();
        personDTO.setId(VALID_ID);

        // When
        when(personService.findById(VALID_ID)).thenReturn(personDTO);

        // Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH + "/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is(personDTO.getCpf())))
                .andExpect(jsonPath("$.firstName", is(personDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDTO.getLastName())));
    }

    @Test
    void whenGETIsCalledWithInvalidIdThenStatusNotFoundIsReturned() throws Exception {
        // When
        when(personService.findById(VALID_ID)).thenThrow(PersonNotFoundException.class);

        // Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH + "/" + VALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETIsCalledWithNoIdThenReturnAListAndStatusOk() throws Exception {
        // Given
        PersonDTO personDTO = PersonUtils.createPersonDTO();
        List<PersonDTO> personDTOS = Collections.singletonList(personDTO);

        // When
        when(personService.findAll()).thenReturn(personDTOS);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf", is(personDTO.getCpf())))
                .andExpect(jsonPath("$[0].firstName", is(personDTO.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(personDTO.getLastName())));
    }

    @Test
    void whenDELETEIsCalledWithValidIDThenStatusNoContentIsReturned() throws Exception {
        // When
        doNothing().when(personService).deleteById(VALID_ID);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(PERSON_API_URL_PATH + "/" + VALID_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIDThenStatusNotFoundsReturned() throws Exception {
        // When
        doThrow(PersonNotFoundException.class).when(personService).deleteById(INVALID_ID);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(PERSON_API_URL_PATH + "/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledWithValidIDAndDTOThenStatusNoContentIsReturned() throws Exception {
        // Given
        PersonDTO personToUpdateDTO = PersonUtils.createPersonDTO();

        // When
        doNothing().when(personService).updateById(VALID_ID, personToUpdateDTO);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(PERSON_API_URL_PATH + "/" + VALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personToUpdateDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPUTIsCalledWithValidIDAndInvalidDTOThenStatusBadRequestIsReturned() throws Exception {
        // Given
        PersonDTO personToUpdateDTO = PersonUtils.createPersonDTO();
        personToUpdateDTO.setFirstName(null);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(PERSON_API_URL_PATH + "/" + VALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personToUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTIsCalledWithInvalidIDThenStatusNotFoundIsReturned() throws Exception {
        // Given
        PersonDTO personToUpdateDTO = PersonUtils.createPersonDTO();

        // When
        doThrow(PersonNotFoundException.class).when(personService).updateById(INVALID_ID, personToUpdateDTO);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(PERSON_API_URL_PATH + "/" + INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(personToUpdateDTO)))
                .andExpect(status().isNotFound());
    }
}
