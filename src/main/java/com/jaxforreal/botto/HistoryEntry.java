package com.jaxforreal.botto;

class HistoryEntry {
    String nick, trip, text;
    long time;

    public HistoryEntry(String nick, String trip, String text, long time) {
        this.nick = nick;
        this.trip = trip;
        this.text = text;
        this.time = time;
    }
}
