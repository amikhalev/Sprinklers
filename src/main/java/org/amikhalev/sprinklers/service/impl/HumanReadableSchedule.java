package org.amikhalev.sprinklers.service.impl;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 4/20/15.
 */
public class HumanReadableSchedule {
    private List<Subschedule> subschedules;

    public HumanReadableSchedule(List<Subschedule> subschedules) {
        this.subschedules = subschedules;
    }

    public List<Subschedule> getSubschedules() {
        return subschedules;
    }

    @Override
    public String toString() {
        return "HumanReadableSchedule{" +
                "subschedules=" + subschedules +
                '}';
    }

    public static class Subschedule {
        private Set<LocalTime> times;
        private EnumSet<DayOfWeek> days;
        private DateTime from;
        private DateTime until;

        public Subschedule(Set<LocalTime> times, EnumSet<DayOfWeek> days) {
            this(times, days, null, null);
        }

        public Subschedule(Set<LocalTime> times, EnumSet<DayOfWeek> days, DateTime from, DateTime until) {
            this.times = times;
            this.days = days;
            this.from = from;
            this.until = until;
        }

        public Set<LocalTime> getTimes() {
            return times;
        }

        public EnumSet<DayOfWeek> getDays() {
            return days;
        }

        public DateTime getFrom() {
            return from;
        }

        public DateTime getUntil() {
            return until;
        }

        @Override
        public String toString() {
            return "at " + times + " on " + days +
                    (from == null ? "" : " from " + from) +
                    (until == null ? "" : " until " + until);
        }
    }
}
