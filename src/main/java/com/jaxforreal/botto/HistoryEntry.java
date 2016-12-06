package com.jaxforreal.botto;

class HistoryEntry {
    public final String nick;
    public final String trip;
    public final String text;
    public final long time;

    public HistoryEntry(String nick, String trip, String text, long time) {
        this.nick = nick;
        this.trip = trip;
        this.text = text;
        this.time = time;
    }
}
