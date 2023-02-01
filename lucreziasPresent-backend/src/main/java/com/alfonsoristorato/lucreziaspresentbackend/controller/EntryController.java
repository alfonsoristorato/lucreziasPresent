package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.service.EntryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/entry")
public class EntryController {

    @Autowired
    private EntryService entryService;

    @GetMapping
    public ResponseEntity<List<Entry>> findAllEntries() throws IOException {
        return new ResponseEntity<>(entryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Entry> addEntry(@ModelAttribute EntryFormWrapper entry, Principal user)
            throws IOException {
        return new ResponseEntity<>(entryService.saveEntry(entry, user), HttpStatus.CREATED);
    }

    @SneakyThrows
    @PatchMapping("/{entryId}")
    public ResponseEntity<Entry> editEntry(@ModelAttribute EntryFormWrapper entry,
            @PathVariable Integer entryId, Principal user)
            throws IOException {
        return new ResponseEntity<>(entryService.editEntry(entry, entryId, user), HttpStatus.CREATED);
    }

    @SneakyThrows
    @DeleteMapping("/{entryId}")
    public ResponseEntity<String> deleteEntry(
            @PathVariable Integer entryId, Principal user)
            throws IOException {
        entryService.deleteEntry(entryId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
