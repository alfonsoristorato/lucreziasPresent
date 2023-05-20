package com.alfonsoristorato.lucreziaspresentbackend.model;

public record EntryError(String description, String details) {
    public static EntryError ENTRY_ERROR(String details) {
        return new EntryError("Entry error", details);
    }

    public static EntryError ENTRY_NOT_FOUND() {
        return new EntryError("Entry error", "Entry not found");
    }
}
