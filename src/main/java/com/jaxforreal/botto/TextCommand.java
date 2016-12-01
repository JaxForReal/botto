package com.jaxforreal.botto;

import java.util.regex.Pattern;

//Text commands always send a string to chat
//%nick% is replaced with the command executor's nickname
//%args% is replaced with the args passed to the command
public class TextCommand implements Command {
    private final String text;

    public TextCommand(String text) {
        this.text = text;
    }

    @Override
    public String getHelp() {
        return "Simple text command";
    }

    @Override
    public void execute(String message, String nick, String trip, Botto bot) {
        //escape backslashe
        message = message.replace("\\", "\\\\");
        String messageText = text.replaceAll(Pattern.quote("%nick%"), nick);
        messageText = messageText.replaceAll(Pattern.quote("%args%"), message);
        bot.sendChat(messageText);
    }
}
