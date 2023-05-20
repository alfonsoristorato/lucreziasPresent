package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.exception.EntryException;
import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryError;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import com.alfonsoristorato.lucreziaspresentbackend.utils.ImageCompressor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final ImageCompressor imageCompressor;

    public EntryService(EntryRepository entryRepository, ImageCompressor imageCompressor) {
        this.entryRepository = entryRepository;
        this.imageCompressor = imageCompressor;
    }

    public List<Entry> findAll() {
        return entryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Entry::getDate))
                .collect(Collectors.toList());
    }

    public void saveEntry(EntryFormWrapper entryFormWrapper, Principal user) throws IOException {
        MultipartFile file = entryFormWrapper.getFile();
        byte[] fileContent = (file != null && file.getContentType().equalsIgnoreCase("image/gif"))
                ? file.getBytes()
                : (file != null ? imageCompressor.compressImage(file) : null);

        Entry entry = buildEntry(entryFormWrapper, user.getName(), fileContent);
        entryRepository.save(entry);
    }

    public void editEntry(EntryFormWrapper entryFormWrapper, Integer entryId, Principal user) {
        entryRepository.findById((long) entryId)
                .map(entry -> {
                    checkEntryBelongsToCaller(entry, user);
                    entry.setColor(entryFormWrapper.getColor());
                    entry.setContent(entryFormWrapper.getContent());
                    entry.setDate(entryFormWrapper.getDate());
                    entry.setName(entryFormWrapper.getName());
                    entry.setTitle(entryFormWrapper.getTitle());
                    return entry;
                })
                .ifPresentOrElse(
                        entryRepository::save,
                        () -> {
                            throw new EntryException(EntryError.ENTRY_NOT_FOUND());
                        }
                );
    }

    public void deleteEntry(Integer entryId, Principal user) {
        entryRepository.findById((long) entryId)
                .ifPresentOrElse(entry -> {
                    checkEntryBelongsToCaller(entry, user);
                    entryRepository.deleteById((long) entryId);
                }, () -> {
                    throw new EntryException(EntryError.ENTRY_NOT_FOUND());
                });
    }

    private Entry buildEntry(EntryFormWrapper form, String owner, byte[] fileContent) {
        return Entry.builder()
                .name(form.getName())
                .content(form.getContent())
                .title(form.getTitle())
                .icon(form.getIcon())
                .color(form.getColor())
                .date(form.getDate())
                .owner(owner)
                .file(fileContent)
                .build();
    }

    private void checkEntryBelongsToCaller(Entry entry, Principal user) {
        if (!entry.getOwner().equals(user.getName())) {
            throw new EntryException(EntryError.ENTRY_ERROR("This entry belongs to another user"));
        }
    }
}
