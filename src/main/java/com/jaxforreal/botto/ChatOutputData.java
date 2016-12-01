package com.jaxforreal.botto;

//gets serialized to json to send messages
public class ChatOutputData {
    @SuppressWarnings("unused")
    public final String cmd = "chat";
    public String text;

    public ChatOutputData(String text) {
        this.text = text;
    }
}
