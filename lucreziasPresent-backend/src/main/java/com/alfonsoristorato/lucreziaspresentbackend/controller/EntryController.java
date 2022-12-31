package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import com.alfonsoristorato.lucreziaspresentbackend.service.EntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Entry> addEntry(@ModelAttribute EntryFormWrapper entry)
            throws IOException {
        return new ResponseEntity<>(entryService.saveEntry(entry), HttpStatus.CREATED);
    }

}
