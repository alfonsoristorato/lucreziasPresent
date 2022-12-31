package com.alfonsoristorato.lucreziaspresentbackend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import com.alfonsoristorato.lucreziaspresentbackend.utils.FileUtils;

@Service
public class EntryService {
    @Autowired
    EntryRepository entryRepository;

    public List<Entry> findAll() throws IOException {
        List<Entry> entryToReturn = entryRepository.findAll();
        entryToReturn.forEach(e -> e.setFile(FileUtils.decompressImage(e.getFile())));
        return entryToReturn;
    }

    public Entry saveEntry(EntryFormWrapper entryFormWrapper) throws IOException {

        return entryRepository.save(Entry.builder()
                .name(entryFormWrapper.getName())
                .content(entryFormWrapper.getContent())
                .title(entryFormWrapper.getTitle())
                .icon(entryFormWrapper.getIcon())
                .color(entryFormWrapper.getColor())
                .date(entryFormWrapper.getDate())
                .file(FileUtils.compressImage(entryFormWrapper.getFile().getBytes())).build());
    }
}
