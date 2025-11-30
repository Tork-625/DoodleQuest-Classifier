package com.vd.doodlequest.models;

public class LibraryItem {
    private final String name;
    private final boolean unlocked;

    public LibraryItem(String name, boolean unlocked) {
        this.name = name;
        this.unlocked = unlocked;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlocked() {
        return unlocked;
    }
}

