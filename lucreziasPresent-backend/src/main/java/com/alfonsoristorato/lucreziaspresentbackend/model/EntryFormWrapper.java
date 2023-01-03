package com.alfonsoristorato.lucreziaspresentbackend.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    private String owner;
    private LocalDate date;
    private MultipartFile file;
}
