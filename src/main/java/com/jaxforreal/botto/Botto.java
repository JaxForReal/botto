package com.jaxforreal.botto;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liam on 30/11/16.
 */
public class Botto extends HackChatClient {
    Map<String, Command> commands;

    public static String trigger = "cucc ";

    public Botto(URI uri, String nick, String pass, String channel) {
        super(uri, nick, pass, channel);

        commands = new HashMap<>();
        setupCommands();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        super.onOpen(serverHandshake);
        sendChat("HI!, I'm cuccbot, here for all your cucking needs :)");
    }

    @Override
    public void onChat(String text, String nick, String trip, long time) {
        System.out.println(nick + ": " + text);

        if (text.startsWith(trigger)) {
            //remove trigger from text
            text = text.substring(trigger.length());

            //commandName is the first word in text
            String commandName = text.split(" ")[0];
            System.out.println(commandName);

            String args;
            //edge case of no args
            if (text.equals(commandName)) {
                args = "";
            } else {
                args = text.substring(commandName.length() + 1);
            }

            Command command = commands.get(commandName);
            if (command == null) {
                sendChat("unrecognised command: " + commandName);
            } else {
                command.execute(args, nick, trip, this);
            }
        }
    }

    @Override
    public void onOtherMessage(Map<String, Object> data) {
        System.out.println(data);
        super.onOtherMessage(data);
    }

    public void setupCommands() {
        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));
    }
}
