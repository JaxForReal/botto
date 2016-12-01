package com.jaxforreal.botto;

class HistoryEntry {
    final String nick;
    final String trip;
    final String text;
    final long time;

    public HistoryEntry(String nick, String trip, String text, long time) {
        this.nick = nick;
        this.trip = trip;
        this.text = text;
        this.time = time;
    }
}
