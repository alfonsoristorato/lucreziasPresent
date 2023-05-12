package com.alfonsoristorato.lucreziaspresentbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")

    private String content;
    private String title;
    private Integer icon;
    private String color;
    private String owner;
    private LocalDate date;
    @Lob
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private byte[] file;

}
