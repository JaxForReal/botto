package com.jaxforreal.botto;

import java.util.HashMap;
import java.util.Map;

public class BottoCommands {
    static Map<String, Command> getCommands() {
        Map<String, Command> commands = new HashMap<>();

        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));

        //todo fix on backslash
        commands.put("source", new Command() {
            @Override
            public String getHelp() {
                return "prints the source of the last LaTeX message";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                for (HistoryEntry entry : bot.history) {
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
            public void execute(String text, String nick, String trip, Botto bot) {
                bot.sendChat(text);
            }

            @Override
            public PrivilegeLevel getPrivilegeLevel() {
                return PrivilegeLevel.CONSOLE;
            }
        });

        commands.put("help", new Command() {
            @Override
            public String getHelp() {
                return "returns generic help into or help test";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                String message = "Usage: help <command>\nBotto, by @jax#xh7Atl\nPreceed all commands with '" + bot.trigger + "'.\n";
                for(String commandName: bot.commands.keySet()) {
                    message += bot.trigger + commandName + ", ";
                }

                bot.sendChat(message);
            }
        });

        return commands;
    }
}
