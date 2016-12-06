package com.jaxforreal.botto;

@FunctionalInterface
public interface LambdaCommand extends Command {
    void execute(String text, String nick, String trip, Botto bot);

    @Override
    default String getHelp() {
        return "<lambda commands, no help provided>";
    }
}
