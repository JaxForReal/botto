package com.jaxforreal.botto;

import java.text.SimpleDateFormat;
import java.util.Date;

class Util {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma z");

    //takes text and separates it into the command (first word), and args (rest)
    //command/args are separated by a space
    static String[] getCommandAndArgs(String text) {
        String commandName = text.split(" ")[0];

        String args;
        //edge case of no args
        if (text.equals(commandName)) {
            args = "";
        } else {
            args = text.substring(commandName.length() + 1);
        }

        return new String[]{commandName, args};
    }

    static String dateString(long unixMillis) {
        Date date = new Date(unixMillis);
        return dateFormat.format(date);
    }
}
