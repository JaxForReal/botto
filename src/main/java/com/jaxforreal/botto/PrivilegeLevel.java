package com.jaxforreal.botto;

public enum PrivilegeLevel {
    USER(0),
    ADMIN(10),
    CONSOLE(20);

    private int level;
    PrivilegeLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean outranksOrEqual(PrivilegeLevel other) {
        return this.level >= other.level;
    }
}
