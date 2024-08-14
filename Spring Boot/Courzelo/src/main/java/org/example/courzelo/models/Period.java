package org.example.courzelo.models;

public enum Period {
    P1("8:00 - 10:00"),
    P2("10:30 - 12:30"),
    P3("13:30 - 15:30"),
    P4("16:00 - 18:30");

    private String timeRange;

    Period(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getTimeRange() {
        return timeRange;
    }
}

