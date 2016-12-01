package com.jaxforreal.botto;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Botto extends HackChatClient {
    private static final String trigger = ":";
    private Map<String, Command> commands;
    private Map<String, PrivilegeLevel> privileges;
    private List<HistoryEntry> history;

    private Thread consoleInputThread;

    public Botto(URI uri, String nick, String pass, String channel) {
        super(uri, nick, pass, channel);

        history = new ArrayList<>();
        consoleInputThread = new Thread(new ConsoleInputThread(this));

        privileges = new HashMap<>();
        privileges.put("jax#xh7Atl", PrivilegeLevel.ADMIN);

        commands = new HashMap<>();
        setupCommands();
    }

    @Override
    public void sendChat(String message) {
        System.out.println("<self>: " + message);
        super.sendChat(message);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        consoleInputThread.interrupt();

        super.onClose(i, s, b);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        super.onOpen(serverHandshake);

        consoleInputThread.start();
    }

    @Override
    public void onChat(String text, String nick, String trip, long time) {
        System.out.println(nick + ": " + text);
        history.add(0, new HistoryEntry(nick, trip, text, time));

        PrivilegeLevel priv = getPrivilege(nick, trip);
        doCommand(text, nick, trip, time, priv);
    }

    @Override
    public void onOtherMessage(Map<String, Object> data) {
        System.out.println(data);
        super.onOtherMessage(data);
    }

    public PrivilegeLevel getPrivilege(String nick, String trip) {
        PrivilegeLevel priv = privileges.get(nick + "#" + trip);
        return priv != null ? priv : PrivilegeLevel.USER;
    }

    private void setupCommands() {
        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));

        //todo fix on backslash
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
                        String escapedText = entry.text.replaceAll("\\$", "<dollar>");
                        bot.sendChat("Message by " + entry.nick + " at " + Util.dateString(entry.time) + " " + escapedText);
                        //System.out.println("Message by " + entry.nick + " at " + Util.dateString(entry.time) + "\n" + entry.text);
                        return;
                    }
                }
                bot.sendChat("could not find latex message");
            }
        });

        commands.put("botsay", new Command() {
            @Override
            public String getHelp() {
                return "talk through the bot";
            }

            @Override
            public void execute(String text, String nick, String trip, HackChatClient bot) {
                bot.sendChat(text);
            }

            @Override
            public PrivilegeLevel getPrivilegeLevel() {
                return PrivilegeLevel.CONSOLE;
            }
        });
    }

    protected void doCommand(String text, String nick, String trip, long time, PrivilegeLevel priv) {
        if (text.startsWith(trigger)) {
            //remove trigger from text
            text = text.substring(trigger.length());

            //1st element is command name, 2nd is args
            String[] commandAndArgs = Util.getCommandAndArgs(text);

            Command command = commands.get(commandAndArgs[0]);
            if (command == null) {
                sendChat("unrecognised command: " + commandAndArgs[0]);
            } else if(!priv.outranksOrEqual(command.getPrivilegeLevel())) {
                sendChat("you need " + command.getPrivilegeLevel().name() + " privilege or higher to do that.");
            } else {
                command.execute(commandAndArgs[1], nick, trip, this);
            }
        }
    }
}
