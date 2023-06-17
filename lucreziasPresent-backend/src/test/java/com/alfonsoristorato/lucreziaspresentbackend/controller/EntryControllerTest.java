package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.service.EntryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryControllerTest {
    @InjectMocks
    private EntryController entryController;

    @Mock
    private EntryService entryService;

    @Mock
    private Principal principal;

    @Test
    void findAllEntriesReturnsOk_and_callsEntryService() {
        List<Entry> entryList = List.of(Entry.builder().build());
        when(entryService.findAll()).thenReturn(entryList);

        ResponseEntity<List<Entry>> response = entryController.findAllEntries();

        Assertions.assertThat(response.getBody()).isEqualTo(entryList);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(entryService).findAll();
    }

    @Test
    void addEntryReturnsCreated_and_callsEntryService() {
        EntryFormWrapper entryFormWrapper = EntryFormWrapper.builder().build();

        ResponseEntity<Void> response = entryController.addEntry(entryFormWrapper, principal);

        Assertions.assertThat(response.getBody()).isNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(entryService).saveEntry(entryFormWrapper,principal);
    }

    @Test
    void editEntryReturnsNoContent_and_callsEntryService() {
        EntryFormWrapper entryFormWrapper = EntryFormWrapper.builder().build();

        ResponseEntity<Void> response = entryController.editEntry(entryFormWrapper, 1, principal);

        Assertions.assertThat(response.getBody()).isNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(entryService).editEntry(entryFormWrapper,1,principal);
    }

    @Test
    void deleteEntryReturnsNoContent_and_callsEntryService() {
        ResponseEntity<Void> response = entryController.deleteEntry(1, principal);

        Assertions.assertThat(response.getBody()).isNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(entryService).deleteEntry(1,principal);
    }

}

