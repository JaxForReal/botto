package com.jaxforreal.botto;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Botto extends HackChatClient {
    static final String trigger = ":";
    Map<String, Command> commands;
    private Map<String, PrivilegeLevel> privileges;

    protected List<HistoryEntry> history;

    private Thread consoleInputThread;

    Botto(URI uri, String nick, String pass, String channel) {
        super(uri, nick, pass, channel);

        history = new ArrayList<>();
        consoleInputThread = new Thread(new ConsoleInputThread(this));

        //todo put into config
        privileges = new HashMap<>();
        privileges.put("jax#xh7Atl", PrivilegeLevel.ADMIN);

        commands = BottoCommands.getCommands();
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

    private PrivilegeLevel getPrivilege(String nick, String trip) {
        PrivilegeLevel priv = privileges.get(nick + "#" + trip);
        return priv != null ? priv : PrivilegeLevel.USER;
    }

    void doCommand(String text, String nick, String trip, long time, PrivilegeLevel priv) {
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
