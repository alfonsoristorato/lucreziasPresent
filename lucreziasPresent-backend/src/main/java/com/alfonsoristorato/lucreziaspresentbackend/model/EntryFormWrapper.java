package com.alfonsoristorato.lucreziaspresentbackend.model;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EntryFormWrapper {
    private String name;
    private String content;
    private String title;
    private Integer icon;
    private String color;
    private LocalDate date;
    private MultipartFile file;
}
