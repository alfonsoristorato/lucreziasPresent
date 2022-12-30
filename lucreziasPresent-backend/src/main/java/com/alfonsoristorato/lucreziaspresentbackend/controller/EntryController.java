package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/entry")
public class EntryController {
    @Autowired
    private EntryRepository entryRepository;

    @GetMapping
    public ResponseEntity<List<Entry>> findAllEntries(){
        return new ResponseEntity<>(entryRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Entry> addEntry(@RequestBody Entry entry){
        return new ResponseEntity<>(entryRepository.save(entry), HttpStatus.CREATED);
    }

}
