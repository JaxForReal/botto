package com.jaxforreal.botto;

//gets serialized to json to send messages
class ChatOutputData {
    @SuppressWarnings("unused")
    public final String cmd = "chat";
    @SuppressWarnings("WeakerAccess")
    public final String text;

    public ChatOutputData(String text) {
        this.text = text;
    }
}
