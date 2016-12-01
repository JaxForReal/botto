package com.jaxforreal.botto;

import java.util.HashMap;
import java.util.Map;

public class BottoCommands {
    static Map<String, Command> getCommands() {
        Map<String, Command> commands = new HashMap<>();

        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));
        commands.put("source", new TextCommand("github.com/JaxForReal/botto"));

        //todo fix on backslash
        commands.put("unrender", new Command() {
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
                if(text.equals("")) {
                    String message = "Usage: help <command>\nBotto, by @jax#xh7Atl\nPreceed all commands with '" + Botto.trigger + "'.\n";
                    for (Map.Entry<String, Command> command : bot.commands.entrySet()) {
                        //only show user level commands
                        if (PrivilegeLevel.USER.outranksOrEqual(command.getValue().getPrivilegeLevel())) {
                            message += Botto.trigger + command.getKey() + ", ";
                        }
                    }

                    bot.sendChat(message);
                } else {
                    //else return the helptext of command specified by text
                    bot.sendChat(commands.get(text).getHelp());
                }
            }
        });

        return commands;
    }
}
