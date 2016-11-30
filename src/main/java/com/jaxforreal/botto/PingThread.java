package com.jaxforreal.botto;

/**
 * Created by liam on 1/12/16.
 */

//this thread simply pings the server every <refreshRate> seconds
public class PingThread implements Runnable {
    private HackChatClient client;
    private long refreshRate;

    public PingThread(HackChatClient client, long refreshRate) {
        this.client = client;
        this.refreshRate = refreshRate;
    }

    @Override
    public void run() {
        while (true) {
            client.send("{\"cmd\": \"ping\"}");
            //System.out.println("pinging");
            try {
                Thread.sleep(refreshRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.close();
            }
        }
    }
}
