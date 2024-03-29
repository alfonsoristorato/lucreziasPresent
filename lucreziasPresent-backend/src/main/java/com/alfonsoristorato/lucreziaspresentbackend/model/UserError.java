package com.alfonsoristorato.lucreziaspresentbackend.model;

public record UserError(String description, String details) {
    public static UserError USER_ERROR(String details) {
        return new UserError("User error", details);
    }

    public static UserError USER_NOT_FOUND() {
        return new UserError("User error", "User not found.");
    }
    public static UserError DISALLOWED_CHANGE(){
        return new UserError("User error", "You cannot change the password for another user.");
    }

}
