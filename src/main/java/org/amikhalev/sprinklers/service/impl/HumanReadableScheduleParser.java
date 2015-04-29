package org.amikhalev.sprinklers.service.impl;

import org.amikhalev.sprinklers.service.ScheduleParseException;
import org.amikhalev.sprinklers.service.ScheduleParser;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;

import java.time.DayOfWeek;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.EnumSet.allOf;
import static java.util.EnumSet.noneOf;
import static java.util.EnumSet.range;
import static org.amikhalev.sprinklers.service.impl.HumanReadableScheduleParser.Type.*;

/**
 * Created by alex on 4/20/15.
 */
public class HumanReadableScheduleParser implements ScheduleParser {
    private static final Logger logger = LoggerFactory.getLogger(HumanReadableScheduleParser.class);
    private List<Token> tokens;
    private int nextToken;
    private String input;

    public HumanReadableScheduleParser() {
    }

    public Trigger parse(String input) throws ScheduleParseException {
        return new HumanReadableScheduleTrigger(parseToSchedule(input));
    }

    public HumanReadableSchedule parseToSchedule(String input) throws ScheduleParseException {
        logger.debug("tokenizing");
        this.input = input.toLowerCase();
        tokenize();
        logger.debug("done. tokens: " + tokens.toString());
        nextToken = 0;
        HumanReadableSchedule schedule = parseSchedule();
        if (!nextIs(EOF)) {
            throw new ScheduleParseException("Tokens left over, did you forget an AND?", input, peek().getStart(), input.length() - 1);
        }
        logger.info("parsed schedule: " + schedule);
        return schedule;
    }

    private ScheduleParseException error(String message, Token token) {
        return new ScheduleParseException(message, input, token.getStart(), token.getEnd());
    }

    private Token peek() {
        return tokens.get(nextToken);
    }

    private boolean nextIs(Type expectedType) {
        return peek().getType() == expectedType;
    }

    private Token acceptToken(Type expectedType) {
        Token token = peek();
        if (token.getType() != expectedType) {
            return null;
        }
        nextToken++;
        return token;
    }

    private boolean accept(Type expectedType) {
        return acceptToken(expectedType) != null;
    }

    private Token expect(Type expectedType) throws ScheduleParseException {
        Token token = peek();
        Type foundType = token.getType();
        if (foundType != expectedType) {
            throw error("Expected " + expectedType + ", found " + foundType, token);
        }
        nextToken++;
        return token;
    }

    private HumanReadableSchedule parseSchedule() throws ScheduleParseException {
        List<HumanReadableSchedule.Subschedule> daysAndTimeses = new ArrayList<HumanReadableSchedule.Subschedule>();
        do {
            HumanReadableSchedule.Subschedule subschedule = parseDaysAndTimes();
            daysAndTimeses.add(subschedule);
        } while (accept(AND));
        return new HumanReadableSchedule(daysAndTimeses);
    }

    private Token peek2() {
        if (nextToken + 1 > tokens.size()) {
            return null;
        }
        return tokens.get(nextToken + 1);
    }

    private boolean nextIs2(Type expectedType) {
        return peek2() != null && peek2().getType() == expectedType;
    }

    private HumanReadableSchedule.Subschedule parseDaysAndTimes() throws ScheduleParseException {
        expect(AT);
        Set<LocalTime> times = new HashSet<LocalTime>();
        while (true) {
            LocalTime time = parseTime();
            times.add(time);
            if (nextIs(AND) && !nextIs2(AT)) {
                accept(AND);
            } else {
                break;
            }
        }
        EnumSet<DayOfWeek> daysOfWeek;
        if (nextIs(ON)) {
            daysOfWeek = parseDaysOfWeek();
        } else {
            daysOfWeek = allOf(DayOfWeek.class);
        }
        DateTime from = null;
        if (accept(FROM)) {
            from = parseDateTime();
        }
        DateTime to = null;
        if (accept(TO)) {
            to = parseDateTime();
        }
        return new HumanReadableSchedule.Subschedule(times, daysOfWeek, from, to);
    }

    private DateTime parseDateTime() throws ScheduleParseException {
        LocalTime time = LocalTime.MIDNIGHT;
        if (nextIs(INTEGER) && (nextIs2(COLON) || nextIs2(AM) || nextIs2(PM))) {
            time = parseTime();
        }
        LocalDate date = parseDate();
        return date.toDateTime(time);
    }

    private LocalDate parseDate() throws ScheduleParseException {
        int month = parseInt();
        expect(SLASH);
        int day = parseInt();
        int year = 0;
        if (accept(SLASH)) {
            year = parseInt();
            if (year < 100) {
                year += (DateTime.now().getYear() / 100) * 100;
            }
        }
        return new LocalDate(year, month, day);
    }

    private EnumSet<DayOfWeek> parseDaysOfWeek() throws ScheduleParseException {
        expect(ON);
        EnumSet<DayOfWeek> daysOfWeek = noneOf(DayOfWeek.class);
        while (true) {
            DayOfWeek day = parseDayOfWeek();
            if (accept(THROUGH)) {
                DayOfWeek throughDay = parseDayOfWeek();
                if (day.compareTo(throughDay) < 0) {
                    daysOfWeek.addAll(range(day, throughDay));
                } else {
                    daysOfWeek.addAll(range(day, DayOfWeek.SUNDAY));
                    daysOfWeek.addAll(range(DayOfWeek.MONDAY, throughDay));
                }
            } else {
                daysOfWeek.add(day);
            }
            if (nextIs(AND) && !nextIs2(AT)) {
                accept(AND);
            } else {
                break;
            }
        }
        return daysOfWeek;
    }

    private DayOfWeek parseDayOfWeek() throws ScheduleParseException {
        if (accept(Type.MONDAY)) {
            return DayOfWeek.MONDAY;
        } else if (accept(TUESDAY)) {
            return DayOfWeek.TUESDAY;
        } else if (accept(WEDNESDAY)) {
            return DayOfWeek.WEDNESDAY;
        } else if (accept(THURSDAY)) {
            return DayOfWeek.THURSDAY;
        } else if (accept(FRIDAY)) {
            return DayOfWeek.FRIDAY;
        } else if (accept(SATURDAY)) {
            return DayOfWeek.SATURDAY;
        } else if (accept(Type.SUNDAY)) {
            return DayOfWeek.SUNDAY;
        } else {
            throw error("Expected day of week, found " + peek().getType(), peek());
        }
    }

    private LocalTime parseTime() throws ScheduleParseException {
        int hours = parseInt();
        int minutes = 0;
        int seconds = 0;
        if (accept(COLON)) {
            minutes = parseInt();
            if (accept(COLON)) {
                seconds = parseInt();
            }
        }
        if (accept(AM)) {
            if (hours == 12) {
                hours = 0;
            }
        } else if (accept(PM)) {
            if (hours != 12) {
                hours += 12;
            }
        }
        return new LocalTime(hours % 24, minutes, seconds);
    }

    private int parseInt() throws ScheduleParseException {
        String str = expect(INTEGER).getText();
        return Integer.parseInt(str);
    }

    private void tokenize() throws ScheduleParseException {
        int position = 0;
        tokens = new ArrayList<Token>();
        List<Match> matches;
        while (position < input.length()) {
            matches = new ArrayList<Match>();
            for (Type tokenType : values()) {
                if (tokenType == EOF) {
                    continue;
                }
                Matcher matcher = tokenType.getPattern().matcher(input.substring(position));
                if (matcher.find()) {
                    matches.add(new Match(tokenType, matcher.start() + position, matcher.end() + position));
                }
            }
            Match candidate = null;
            for (Match match : matches) {
                if (candidate == null) {
                    if (match.getLength() > 0) {
                        candidate = match;
                    }
                } else if (match.getLength() > candidate.getLength()) {
                    candidate = match;
                }
            }
            if (candidate == null) {
                throw new ScheduleParseException("Bad token", input, position, position);
            } else {
                if (candidate.getType() != WHITESPACE) {
                    String text = input.substring(candidate.getStart(), candidate.getEnd());
                    tokens.add(new Token(candidate.type, text, candidate.getStart(), candidate.getEnd()));
                }
                position = candidate.getEnd();
            }
        }
        tokens.add(new Token(EOF, "", position, position));
    }

    public enum Type {
        EOF("end of file", ""),
        AT("at", "^at"),
        AM("am", "^am"),
        PM("pm", "^pm"),
        ON("on", "^on"),
        FROM("from", "^(from|starting)"),
        TO("to", "^(to|until)"),
        MONDAY("monday", "^mon(day)?"),
        TUESDAY("tuesday", "^tue(sday)?"),
        WEDNESDAY("wednesday", "^wed(nesday)?"),
        THURSDAY("thursday", "^thu(r(sday)?)?"),
        FRIDAY("friday", "^fri(day)?"),
        SATURDAY("saturday", "^sat(urday)?"),
        SUNDAY("sunday", "^sun(day)?"),
        THROUGH("through", "^(through|thru|\\-)"),
        AND("and", "^(and|also)"),
        INTEGER("integer", "^\\d+"),
        SLASH("slash", "^/"),
        COLON("colon", "^:"),
        WHITESPACE("whitespace", "^\\s+");

        private final String name;
        private final Pattern pattern;

        Type(String name, String regex) {
            this.name = name;
            this.pattern = Pattern.compile(regex);
        }

        public String getName() {
            return name;
        }

        public Pattern getPattern() {
            return pattern;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private static class Match {
        private final Type type;
        private final int start;
        private final int end;

        public Match(Type type, int start, int end) {
            this.type = type;
            this.start = start;
            this.end = end;
        }

        public Type getType() {
            return type;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getLength() {
            return getEnd() - getStart();
        }

        @Override
        public String toString() {
            return "Match{" +
                    "type=" + type +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    public static class Token {
        private final Type type;
        private final String text;
        private final int start;
        private final int end;

        public Token(Type type, String text, int start, int end) {
            this.type = type;
            this.text = text;
            this.start = start;
            this.end = end;
        }

        public Type getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "Token " + type.toString() + "(" + text + ")";
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

    }
}
