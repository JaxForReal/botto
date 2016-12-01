package com.jaxforreal.botto;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Botto extends HackChatClient {
    private static final String trigger = "cucc ";
    private Map<String, Command> commands;
    private List<HistoryEntry> history;

    public Botto(URI uri, String nick, String pass, String channel) {
        super(uri, nick, pass, channel);

        history = new ArrayList<>();
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
        history.add(0, new HistoryEntry(nick, trip, text, time));

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
        commands.put("source", new Command() {
            @Override
            public String getHelp() {
                return "prints the source of the last LaTeX message";
            }

            @Override
            public void execute(String text, String nick, String trip, HackChatClient bot) {
                for (HistoryEntry entry : history) {
                    if (entry.text.startsWith("$")) {
                        //bot.sendChat("found");
                        String escapedText = entry.text.replace('$', ' ');
                        System.out.println(escapedText);
                        bot.sendChat("Message by " + entry.nick + " at " + Util.dateString(entry.time) + " " + escapedText);
                        //System.out.println("Message by " + entry.nick + " at " + Util.dateString(entry.time) + "\n" + entry.text);
                        return;
                    }
                }
                bot.sendChat("could not find latex message");
            }
        });
    }
}
