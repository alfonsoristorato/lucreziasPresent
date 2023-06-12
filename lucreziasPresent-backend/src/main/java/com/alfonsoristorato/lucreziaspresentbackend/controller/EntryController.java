package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.service.EntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/entry")
@PreAuthorize("principal.user.firstLogin == false")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    public ResponseEntity<List<Entry>> findAllEntries() {
        return new ResponseEntity<>(entryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addEntry(@Valid @ModelAttribute EntryFormWrapper entry, Principal user) {
        entryService.saveEntry(entry, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{entryId}")
    public ResponseEntity<Entry> editEntry(@Valid @ModelAttribute EntryFormWrapper entry,
                                           @PathVariable Integer entryId, Principal user) {
        entryService.editEntry(entry, entryId, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer entryId, Principal user) {
        entryService.deleteEntry(entryId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
