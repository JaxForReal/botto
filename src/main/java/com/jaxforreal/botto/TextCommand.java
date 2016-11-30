package com.jaxforreal.botto;

/**
 * Created by liam on 1/12/16.
 */

//Text commands always send a string to chat
//%nick% is replaced with the command executer's nickname
//%args% is replaced with the args passed to the command
public class TextCommand implements Command {
    public String text;

    public TextCommand(String text) {
        this.text = text;
    }

    @Override
    public String getHelp() {
        return "Simple text command";
    }

    @Override
    public void execute(String message, String nick, String trip, HackChatClient bot) {
        String messageText = text.replaceAll("%nick%", nick);
        messageText = messageText.replaceAll("%args%", message);
        bot.sendChat(messageText);
    }
}
