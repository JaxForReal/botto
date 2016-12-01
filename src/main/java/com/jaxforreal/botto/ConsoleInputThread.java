package com.jaxforreal.botto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

//repeatedly waits for input and sends it to the bot as a command
class ConsoleInputThread implements Runnable {
    private Botto bot;
    private BufferedReader reader;

    ConsoleInputThread(Botto bot) {
        this.bot = bot;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while(true) {
            try {
                String text = reader.readLine();
                bot.doCommand(text, "console", "console", Instant.now().toEpochMilli(), PrivilegeLevel.CONSOLE);
            } catch (IOException e) {
                e.printStackTrace();
                bot.close();
            }
        }
    }
}
