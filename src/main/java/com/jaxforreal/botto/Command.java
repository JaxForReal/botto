package com.jaxforreal.botto;

/**
 * Created by liam on 1/12/16.
 */
public interface Command {
    String getHelp();

    void execute(String text, String nick, String trip, HackChatClient bot);

    default PrivledgeLevel getPrivledgeLevel() {
        return PrivledgeLevel.NONE;
    }
}
