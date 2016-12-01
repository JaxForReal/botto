package com.jaxforreal.botto;

interface Command {
    String getHelp();

    void execute(String text, String nick, String trip, Botto bot);

    default PrivilegeLevel getPrivilegeLevel() {
        return PrivilegeLevel.USER;
    }
}
