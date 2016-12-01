package com.jaxforreal.botto;

//this thread simply pings the server every <refreshRate> seconds
class PingThread implements Runnable {
    private final HackChatClient client;
    private final long refreshRate;

    public PingThread(HackChatClient client, long refreshRate) {
        this.client = client;
        this.refreshRate = refreshRate;
    }

    @SuppressWarnings("InfiniteLoopStatement")
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
