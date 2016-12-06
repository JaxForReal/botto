package com.jaxforreal.botto;

import java.util.HashMap;
import java.util.Map;

class BottoCommands {
    static Map<String, Command> getCommands() {
        Map<String, Command> commands = new HashMap<>();

        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("test", new TextCommand("%nick%: %args%"));
        commands.put("source", new TextCommand("http://github.com/JaxForReal/botto"));
        commands.put("uptime", new TextCommand("∞ hours, 4 minutes"));

        //todo fix on backslash
        commands.put("unrender", new Command() {
            @Override
            public String getHelp() {
                return "Usage: `unrender`, prints the source of the last LaTeX message\n" +
                        "`unrender <num>` skips num posts, and displays that latex source\n" +
                        "eg. `unrender 1` will return the second to last latex post's source";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                int numToSkip;
                try {
                    numToSkip = text.equals("") ? 0 : Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    numToSkip = 0;
                }
                if (numToSkip < 0) numToSkip = 0;

                for (HistoryEntry entry : bot.history) {
                    if (entry.text.contains("$")) {
                        //continue to fetch next if a skip is needed
                        if(numToSkip > 0) {
                            numToSkip --;
                            continue;
                        }
                        //bot.sendChat("found");
                        String escapedText = entry.text.replaceAll("\\$", "<dollar>");
                        bot.sendChat("Message by " + entry.nick + " at " + Util.dateString(entry.time) + "\n" + escapedText);
                        //System.out.println("Message by " + entry.nick + " at " + Util.dateString(entry.time) + "\n" + entry.text);
                        return;
                    }
                }
                bot.sendChat("could not find any latex messages");
            }
        });

        commands.put("say", new Command() {
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

        Command help = new Command() {
            @Override
            public String getHelp() {
                return "returns generic help into or help test";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                if (text.equals("")) {
                    String message = "Usage: help <command>\n\n" +
                            "Botto, by @jax#xh7Atl\n" +
                            "Precede all commands with '" + Botto.trigger + "'.\n";
                    for (Map.Entry<String, Command> command : bot.commands.entrySet()) {
                        //only show user level commands
                        if (PrivilegeLevel.USER.outranksOrEqual(command.getValue().getPrivilegeLevel())) {
                            message += Botto.trigger + command.getKey() + ", ";
                        }
                    }

                    bot.sendChat(message);
                } else {
                    Command helpCommand = commands.get(text);
                    if(helpCommand != null) {
                        bot.sendChat(commands.get(text).getHelp());
                    } else {
                        bot.sendChat("Command not found");
                    }
                }
            }
        };
        commands.put("help", help);
        commands.put("h", help);

        return commands;
    }
}
