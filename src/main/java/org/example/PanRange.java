package org.example;

public enum PanRange {
    APP_MIN(1000000000000000L),
    APP_MAX(1000005999999999L),
    BRW_MIN(2000000000000000L),
    BRW_MAX(2000001234567890L),
    MIN_3RI(3000000000000000L),
    MAX_3RI(3000000000000100L);

    public final long value;

    PanRange(long value) {
        this.value = value;
    }
}
