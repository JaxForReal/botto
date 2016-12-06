package com.jaxforreal.botto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

class BottoCommands {
    static Map<String, Command> getCommands() {
        Map<String, Command> commands = new HashMap<>();

        commands.put("about", new TextCommand("Bot by @jax#xh7Atl"));
        commands.put("source", new TextCommand("http://github.com/JaxForReal/botto"));
        commands.put("uptime", new TextCommand("∞ hours, 4 minutes"));
        commands.put("format", new FormatCommand());
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
                        if (numToSkip > 0) {
                            numToSkip--;
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
                return PrivilegeLevel.ADMIN;
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
                    if (helpCommand != null) {
                        bot.sendChat(commands.get(text).getHelp());
                    } else {
                        bot.sendChat("Command not found");
                    }
                }
            }
        };
        commands.put("help", help);
        commands.put("h", help);
        commands.put("alias", new Command() {
            @Override
            public PrivilegeLevel getPrivilegeLevel() {
                return PrivilegeLevel.ADMIN;
            }

            @Override
            public String getHelp() {
                return "admin only boi, don't test me";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                String[] cmdArgs = Util.getCommandAndArgs(text);

                bot.commands.put(cmdArgs[0], new Command() {
                    @Override
                    public String getHelp() {
                        return "Command aliased by " + nick;
                    }

                    @Override
                    public void execute(String text, String nick, String trip, Botto bot) {
                        bot.doCommand(cmdArgs[1], "console", "console", PrivilegeLevel.USER);
                    }
                });
            }
        });

        commands.put("cowsay", new Command() {
            @Override
            public String getHelp() {
                return "linux cowsay command";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                try {
                    bot.sendChat(Util.getOutput(new ProcessBuilder(new String[]{"cowsay", text}).start()));
                } catch (IOException e) {
                    bot.doError(e);
                }
            }
        });

        commands.put("savehistory", new Command() {
            @Override
            public String getHelp() {
                return "save logs";
            }

            @Override
            public PrivilegeLevel getPrivilegeLevel() {
                return PrivilegeLevel.ADMIN;
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                bot.loadHistoryAdd();
                bot.saveHistoryOverwrite();
            }
        });

        commands.put("loadhistory", new Command() {
            @Override
            public String getHelp() {
                return "load logs";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                bot.loadHistoryAdd();
            }
        });

        Random r = new Random();
        commands.put("quote", new Command() {
            @Override
            public String getHelp() {
                return "Usage: quote <user>\n" +
                        "returns a random quote from <user>\n" +
                        "or random quote from anyone is no user specified";
            }

            @Override
            public void execute(String text, String nick, String trip, Botto bot) {
                if (text.equals("")) {
                    //get random if no nick supplied
                    HistoryEntry entry = bot.history.get(r.nextInt(bot.history.size()));
                    bot.sendChat(entry.nick + " at " + Util.dateString(entry.time) + "\n" + entry.text);
                } else {
                    //filter based on nick
                    List<HistoryEntry> relevantEntries = bot.history.stream()
                            .filter(e -> e.nick.toLowerCase().equals(text.toLowerCase()))
                            .collect(Collectors.toList());
                    if (relevantEntries.size() > 0) {
                        HistoryEntry entry = relevantEntries.get(r.nextInt(relevantEntries.size()));
                        bot.sendChat(entry.nick + " at " + Util.dateString(entry.time) + "\n" + entry.text);
                    } else {
                        bot.sendChat("no relevant entries found");
                    }
                }
            }
        });
        return commands;
    }
}
