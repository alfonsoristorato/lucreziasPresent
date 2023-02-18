package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import com.alfonsoristorato.lucreziaspresentbackend.utils.FileUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EntryService {
    @Autowired
    EntryRepository entryRepository;

    public List<Entry> findAll() {
        List<Entry> entries = entryRepository.findAll();
        entries.sort(Comparator.comparing(Entry::getDate));
        return entries;
    }

    public Entry saveEntry(EntryFormWrapper entryFormWrapper, Principal user) throws IOException {
        Entry entryToSave;

        if (entryFormWrapper.getFile() != null) {
            MultipartFile file = entryFormWrapper.getFile();
            entryToSave = Entry.builder()
                    .name(entryFormWrapper.getName())
                    .content(entryFormWrapper.getContent())
                    .title(entryFormWrapper.getTitle())
                    .icon(entryFormWrapper.getIcon())
                    .color(entryFormWrapper.getColor())
                    .date(entryFormWrapper.getDate())
                    .owner(user.getName())
                    .file(file.getContentType().equalsIgnoreCase("image/gif")
                            ? file.getBytes()
                            : FileUtils.compressImage(file))
                    .build();
        } else {
            entryToSave = Entry.builder()
                    .name(entryFormWrapper.getName())
                    .content(entryFormWrapper.getContent())
                    .title(entryFormWrapper.getTitle())
                    .icon(entryFormWrapper.getIcon())
                    .color(entryFormWrapper.getColor())
                    .date(entryFormWrapper.getDate())
                    .owner(user.getName())
                    .build();
        }
        return entryRepository.save(entryToSave);
    }

    @SneakyThrows
    public Entry editEntry(EntryFormWrapper entryFormWrapper, Integer entryId, Principal user) {
        Optional<Entry> entryToSave = entryRepository.findById((long) entryId);
        if (entryToSave.isPresent()) {
            if (entryToSave.get().getOwner().equals(user.getName())) {
                entryToSave.get().setColor(entryFormWrapper.getColor());
                entryToSave.get().setContent(entryFormWrapper.getContent());
                entryToSave.get().setDate(entryFormWrapper.getDate());
                entryToSave.get().setName(entryFormWrapper.getName());
                entryToSave.get().setTitle(entryFormWrapper.getTitle());
                return entryRepository.save(entryToSave.get());
            }
            throw new Exception("This entry belongs to another user");
        }
        throw new Exception("No entry with given id");

    }

    @SneakyThrows
    public void deleteEntry(Integer entryId, Principal user) {
        Optional<Entry> entryToSave = entryRepository.findById((long) entryId);
        if (entryToSave.isPresent()) {

            if (entryToSave.get().getOwner().equals(user.getName())) {

                entryRepository.deleteById((long) entryId);
            } else {
                throw new Exception("This entry belongs to another user");
            }
        } else {
            throw new Exception("No entry with given id");
        }

    }
}
