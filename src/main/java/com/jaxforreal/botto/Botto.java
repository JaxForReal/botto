package com.jaxforreal.botto;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liam on 30/11/16.
 */
public class Botto extends HackChatClient {
    private static final String trigger = "cucc ";
    private Map<String, Command> commands;

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

            //1st element is command name, 2nd is args
            String[] commandAndArgs = Util.getCommandAndArgs(text);

            Command command = commands.get(commandAndArgs[0]);
            if (command == null) {
                sendChat("unrecognised command: " + commandAndArgs[0]);
            } else {
                command.execute(commandAndArgs[1], nick, trip, this);
            }
        }
    }

    @Override
    public void onOtherMessage(Map<String, Object> data) {
        System.out.println(data);
        super.onOtherMessage(data);
    }

    private void setupCommands() {
        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));
    }
}
