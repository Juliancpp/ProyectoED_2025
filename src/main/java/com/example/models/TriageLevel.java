package com.example.models;

public enum TriageLevel {
    NIVEL_1_RESUCITACION(1),
    NIVEL_2_EMERGENCIA(2),
    NIVEL_3_URGENTE(3),
    NIVEL_4_MENOR(4),
    NIVEL_5_NO_URGENTE(5);

    private final int level;

    TriageLevel(int level) { this.level = level; }

    public int getLevel() { return level; }

    public static TriageLevel fromOrdinal(int ordinal) {
        // ordinal 1..5 -> enum
        for (TriageLevel t : values()) if (t.level == ordinal) return t;
        return NIVEL_5_NO_URGENTE;
    }
}
