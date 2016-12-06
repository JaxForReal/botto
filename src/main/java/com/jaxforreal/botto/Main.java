package com.jaxforreal.botto;

import java.net.URI;
import java.net.URISyntaxException;

class Main {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        HackChatClient client = new Botto(new URI("wss://hack.chat/chat-ws"), "botto", "trippo", "programming");

        client.connectBlocking();
    }
}
