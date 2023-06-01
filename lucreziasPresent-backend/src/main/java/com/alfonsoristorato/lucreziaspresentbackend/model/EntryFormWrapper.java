package com.alfonsoristorato.lucreziaspresentbackend.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EntryFormWrapper {
    @NotNull
    private String name;
    @NotNull
    private String content;
    @NotNull
    private String title;
    @NotNull
    private Integer icon;
    @NotNull
    private String color;
    @NotNull
    private LocalDate date;
    private MultipartFile file;
}
