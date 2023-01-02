package com.alfonsoristorato.lucreziaspresentbackend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

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
        return entryRepository.findAll();
    }

    public Entry saveEntry(EntryFormWrapper entryFormWrapper) throws IOException {
        Entry entryToSave = new Entry();
        if (entryFormWrapper.getFile() != null) {
            entryToSave = Entry.builder()
                    .name(entryFormWrapper.getName())
                    .content(entryFormWrapper.getContent())
                    .title(entryFormWrapper.getTitle())
                    .icon(entryFormWrapper.getIcon())
                    .color(entryFormWrapper.getColor())
                    .date(entryFormWrapper.getDate())
                    .owner(entryFormWrapper.getOwner())
                    .file(FileUtils.compressImage(entryFormWrapper.getFile()))
                    .build();
        } else {
            entryToSave = Entry.builder()
                    .name(entryFormWrapper.getName())
                    .content(entryFormWrapper.getContent())
                    .title(entryFormWrapper.getTitle())
                    .icon(entryFormWrapper.getIcon())
                    .color(entryFormWrapper.getColor())
                    .date(entryFormWrapper.getDate())
                    .owner(entryFormWrapper.getOwner())
                    .build();
        }
        return entryRepository.save(entryToSave);
    }

    public Entry editEntry(EntryFormWrapper entryFormWrapper, Integer entryId) {
        Optional<Entry> entryToSave = entryRepository.findById(((long) entryId));
        entryToSave.get().setColor(entryFormWrapper.getColor());
        entryToSave.get().setContent(entryFormWrapper.getContent());
        entryToSave.get().setDate(entryFormWrapper.getDate());
        entryToSave.get().setName(entryFormWrapper.getName());
        entryToSave.get().setTitle(entryFormWrapper.getTitle());
        return entryRepository.save(entryToSave.get());
    }

    public void deleteEntry(Integer entryId) {
        entryRepository.deleteById((long) entryId);
    }
}
