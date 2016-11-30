package com.jaxforreal.botto;

/**
 * Created by liam on 1/12/16.
 */
class Util {
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
}
