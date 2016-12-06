package com.jaxforreal.botto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WisdomCommand implements Command {
    List<String> wisdom;

    public WisdomCommand() {
        wisdom = new ArrayList<>();

        Collections.addAll(wisdom,
                "$\\mathtt{\\color{green}{>implying}}$",
                "$\\mathtt{\\color{red}{[triggered]}}$"
                );
    }

    @Override
    public String getHelp() {
        return "advice from bot";
    }

    @Override
    public void execute(String text, String nick, String trip, Botto bot) {

    }
}
