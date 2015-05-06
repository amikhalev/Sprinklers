package org.amikhalev.sprinklers.exceptions;

/**
 * Created by alex on 4/20/15.
 */
public class ScheduleParseException extends Exception {
    private String error;
    private String input;
    private int start;
    private int end;

    public ScheduleParseException(String error, String input, int start, int end) {
        super();
        this.error = error;
        this.input = input;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getMessage() {
        String pointer = "";
        for (int i = 0; i < start; i++) {
            pointer += ' ';
        }
        for (int i = start; i <= end; i++) {
            pointer += '^';
        }
        return error + '\n'
                + input + '\n'
                + pointer;
    }

    public String getError() {
        return error;
    }

    public String getInput() {
        return input;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
