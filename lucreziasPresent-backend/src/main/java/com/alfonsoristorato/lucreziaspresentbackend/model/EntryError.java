package com.alfonsoristorato.lucreziaspresentbackend.model;

public record EntryError(String description, String details) {
    public static EntryError ENTRY_ERROR(String details) {
        return new EntryError("Entry error", details);
    }

    public static EntryError DISALLOWED_CHANGE() {
        return new EntryError("Entry error", "This entry belongs to another user.");
    }

    public static EntryError ENTRY_NOT_FOUND() {
        return new EntryError("Entry error", "Entry not found.");
    }
}
