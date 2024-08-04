package org.example.courzelo.models;

import java.util.HashSet;
import java.util.Set;

public enum UserInterest {
    JAVA, C, PYTHON;

    private static final Set<String> additionalInterests = new HashSet<>();

    public static boolean contains(String test) {
        if (test == null) {
            return false;
        }
        // Check in enum constants
        for (UserInterest interest : UserInterest.values()) {
            if (interest.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        // Check in additional interests
        return additionalInterests.contains(test.toUpperCase());
    }

    public static void addInterest(String newInterest) {
        if (newInterest != null && !newInterest.trim().isEmpty()) {
            additionalInterests.add(newInterest.toUpperCase());
        }
    }

    public static Set<String> getAllInterests() {
        Set<String> allInterests = new HashSet<>();
        for (UserInterest interest : UserInterest.values()) {
            allInterests.add(interest.name());
        }
        allInterests.addAll(additionalInterests);
        return allInterests;
    }
}