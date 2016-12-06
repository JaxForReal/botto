package com.jaxforreal.botto;

import java.io.IOException;

public class FormatCommand implements Command {
    @Override
    public String getHelp() {
        return "Usage: format <font> <text>\n" +
                "Uses the linux figlet command to format supplied text\n" +
                "Supported fonts are: banner, big, block, bubble, digital, ivrit, lean,\n" +
                "mini, mnemonic, script, shadow, smshadow, smslant, standard, term";
    }

    @Override
    public void execute(String text, String nick, String trip, Botto bot) {
        String[] cmdArgs = Util.getCommandAndArgs(text);
        String cmd = cmdArgs[0];
        String args = cmdArgs[1];

        if(cmd.equals("") || args.equals("")) {
            bot.sendChat(getHelp());
            return;
        }

        Process figlet;
        try {
            figlet = new ProcessBuilder(new String[]{"figlet", "-f", cmd, args}).start();
        } catch (IOException e) {
            bot.doError(e);
            return;
        }
        //System.out.println(output;);
        bot.sendChat("@" + nick + "\n" + Util.getOutput(figlet));
    }
}
