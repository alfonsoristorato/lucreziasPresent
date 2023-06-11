package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.exception.EntryException;
import com.alfonsoristorato.lucreziaspresentbackend.model.Entry;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryError;
import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import com.alfonsoristorato.lucreziaspresentbackend.repository.EntryRepository;
import com.alfonsoristorato.lucreziaspresentbackend.utils.ImageCompressor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryServiceTest {

    @InjectMocks
    private EntryService entryService;
    @Mock
    private EntryRepository entryRepository;
    @Mock
    private Principal principal;
    @Mock
    private ImageCompressor imageCompressor;
    @Captor
    private ArgumentCaptor<Entry> entryCaptor;

    private final LocalDate now = LocalDate.now();

    private final EntryFormWrapper genericEntryForm = EntryFormWrapper.builder()
            .name("name")
            .content("content")
            .title("title")
            .icon(1)
            .color("red")
            .date(now)
            .build();

    @Test
    void findAll_returnsASortedByDateListOfEntries() {
        Entry entryOne = Entry.builder().date(LocalDate.of(2010, 10, 1)).build();
        Entry entryTwo = Entry.builder().date(LocalDate.of(2011, 10, 1)).build();

        List<Entry> unorderedListToReturn = Arrays.asList(entryTwo, entryOne);

        when(entryRepository.findAll()).thenReturn(unorderedListToReturn);
        List<Entry> listReturned = entryService.findAll();

        Assertions.assertThat(listReturned.get(0)).isNotEqualTo(entryTwo);
        Assertions.assertThat(listReturned.get(0)).isEqualTo(entryOne);
        Assertions.assertThat(listReturned.get(1)).isEqualTo(entryTwo);
    }

    @Test
    void saveEntry_savesAnEntryWithDataFromParams_andFileIsNullIfNotPresentInParams() {
        when(principal.getName()).thenReturn("owner");

        entryService.saveEntry(genericEntryForm, principal);

        Entry entryExpected = Entry.builder()
                .name(genericEntryForm.getName())
                .content(genericEntryForm.getContent())
                .title(genericEntryForm.getTitle())
                .icon(genericEntryForm.getIcon())
                .color(genericEntryForm.getColor())
                .date(genericEntryForm.getDate())
                .owner(principal.getName())
                .build();
        verify(entryRepository).save(entryCaptor.capture());
        Entry capturedEntry = entryCaptor.getValue();
        Assertions.assertThat(entryExpected).isEqualTo(capturedEntry);
        Assertions.assertThat(capturedEntry.getFile()).isNull();
    }

    @ParameterizedTest
    @CsvSource({
            "file.jpg, image/jpg",
            "file.gif, image/gif"
    })
    void saveEntry_savesAnEntryWithDataFromParams_andFileIsPresentIfPresentInParams(String fileName, String contentType) throws IOException {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", fileName, contentType, "nonCompressedImage".getBytes());
        EntryFormWrapper entryFormWrapper = genericEntryForm;
        entryFormWrapper.setFile(mockMultipartFile);


        when(principal.getName()).thenReturn("owner");
        if (!contentType.equals("image/gif")) {
            when(imageCompressor.compressImage(mockMultipartFile)).thenReturn("compressedImage".getBytes());
        }

        entryService.saveEntry(entryFormWrapper, principal);

        Entry entryExpected = Entry.builder()
                .name(entryFormWrapper.getName())
                .content(entryFormWrapper.getContent())
                .title(entryFormWrapper.getTitle())
                .icon(entryFormWrapper.getIcon())
                .color(entryFormWrapper.getColor())
                .date(entryFormWrapper.getDate())
                .owner(principal.getName())
                .file(!contentType.equals("image/gif") ? "compressedImage".getBytes() : "nonCompressedImage".getBytes())
                .build();
        verify(entryRepository).save(entryCaptor.capture());
        Entry capturedEntry = entryCaptor.getValue();
        Assertions.assertThat(entryExpected).isEqualTo(capturedEntry);
    }

    @ParameterizedTest
    @CsvSource({
            "file.mp4, video/mp4",
            "file.mp3, audio/mpeg3"
    })
    void saveEntry_throwsExceptionIfFileIsNotSupported(String fileName, String contentType) {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", fileName, contentType, "nonCompressedImage".getBytes());
        EntryFormWrapper entryFormWrapper = genericEntryForm;
        entryFormWrapper.setFile(mockMultipartFile);

        EntryException exception = Assertions.catchThrowableOfType(() -> entryService.saveEntry(entryFormWrapper, principal), EntryException.class);

        Assertions.assertThat(exception).isInstanceOf(EntryException.class);
        Assertions.assertThat(exception.getError()).isEqualTo(EntryError.ENTRY_ERROR("File type not accepted."));
        verify(entryRepository, never()).save(any());
    }

    @Test
    void editEntry_updatesAnEntryWithDataFromParams() {
        Entry entryToSave = Entry.builder()
                .id(1L)
                .name("oldName")
                .content("oldContent")
                .title("oldTitle")
                .icon(1)
                .color("oldColor")
                .date(now.minus(1, ChronoUnit.DAYS))
                .owner("owner")
                .file("image".getBytes())
                .build();
        when(entryRepository.findById(1L)).thenReturn(Optional.of(entryToSave));
        when(principal.getName()).thenReturn("owner");

        entryService.editEntry(genericEntryForm, 1, principal);

        verify(entryRepository).save(entryToSave);
        Assertions.assertThat(entryToSave.getColor()).isEqualTo("red");
        Assertions.assertThat(entryToSave.getContent()).isEqualTo("content");
        Assertions.assertThat(entryToSave.getDate()).isEqualTo(now);
        Assertions.assertThat(entryToSave.getName()).isEqualTo("name");
        Assertions.assertThat(entryToSave.getTitle()).isEqualTo("title");
    }

    @Test
    void editEntry_throwsExceptionIfEntryIsNotFound() {
        when(entryRepository.findById(1L)).thenReturn(Optional.empty());

        EntryException exception = Assertions.catchThrowableOfType(() -> entryService.editEntry(genericEntryForm, 1, principal), EntryException.class);

        Assertions.assertThat(exception).isInstanceOf(EntryException.class);
        Assertions.assertThat(exception.getError()).isEqualTo(EntryError.ENTRY_NOT_FOUND());
        verify(entryRepository, never()).save(any());

    }

    @Test
    void editEntry_throwsExceptionIfEntryBelongsToAnotherUser() {
        Entry entryToSave = Entry.builder()
                .id(1L)
                .name("oldName")
                .content("oldContent")
                .title("oldTitle")
                .icon(1)
                .color("oldColor")
                .date(now.minus(1, ChronoUnit.DAYS))
                .owner("owner")
                .file("image".getBytes())
                .build();
        when(entryRepository.findById(1L)).thenReturn(Optional.of(entryToSave));
        when(principal.getName()).thenReturn("notTheOwner");

        EntryException exception = Assertions.catchThrowableOfType(() -> entryService.editEntry(genericEntryForm, 1, principal), EntryException.class);
        Assertions.assertThat(exception).isInstanceOf(EntryException.class);
        Assertions.assertThat(exception.getError()).isEqualTo(EntryError.ENTRY_ERROR("This entry belongs to another user"));
        verify(entryRepository, never()).save(any());

    }

    @Test
    void deleteEntry_deletesAnEntry() {
        Entry entryToFind = Entry.builder()
                .id(1L)
                .name("oldName")
                .content("oldContent")
                .title("oldTitle")
                .icon(1)
                .color("oldColor")
                .date(now.minus(1, ChronoUnit.DAYS))
                .owner("owner")
                .file("image".getBytes())
                .build();
        when(entryRepository.findById(1L)).thenReturn(Optional.of(entryToFind));
        when(principal.getName()).thenReturn("owner");

        entryService.deleteEntry(1, principal);

        verify(entryRepository).deleteById(1L);
    }

    @Test
    void deleteEntry_throwsExceptionIfEntryIsNotFound() {
        when(entryRepository.findById(1L)).thenReturn(Optional.empty());

        EntryException exception = Assertions.catchThrowableOfType(() -> entryService.deleteEntry(1, principal), EntryException.class);

        Assertions.assertThat(exception).isInstanceOf(EntryException.class);
        Assertions.assertThat(exception.getError()).isEqualTo(EntryError.ENTRY_NOT_FOUND());
        verify(entryRepository, never()).deleteById(any());
    }

    @Test
    void deleteEntry_throwsExceptionIfEntryBelongsToAnotherUser() {
        Entry entryToSave = Entry.builder()
                .id(1L)
                .name("oldName")
                .content("oldContent")
                .title("oldTitle")
                .icon(1)
                .color("oldColor")
                .date(now.minus(1, ChronoUnit.DAYS))
                .owner("owner")
                .file("image".getBytes())
                .build();
        when(entryRepository.findById(1L)).thenReturn(Optional.of(entryToSave));
        when(principal.getName()).thenReturn("notTheOwner");

        EntryException exception = Assertions.catchThrowableOfType(() -> entryService.deleteEntry(1, principal), EntryException.class);

        Assertions.assertThat(exception).isInstanceOf(EntryException.class);
        Assertions.assertThat(exception.getError()).isEqualTo(EntryError.ENTRY_ERROR("This entry belongs to another user"));
        verify(entryRepository, never()).deleteById(any());
    }
}
