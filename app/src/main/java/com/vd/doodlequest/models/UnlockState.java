package com.vd.doodlequest.models;

import java.util.HashSet;
import java.util.Set;

public class UnlockState {
    private static final Set<Integer> unlockedIndexes = new HashSet<>();

    public static void unlock(int index) {
        unlockedIndexes.add(index);
    }
}
