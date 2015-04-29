package org.amikhalev.sprinklers.service.impl;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by alex on 4/21/15.
 */
public class HumanReadableScheduleTrigger implements Trigger {
    private HumanReadableSchedule schedule;

    public HumanReadableScheduleTrigger(HumanReadableSchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        DateTime nextDate = null;
        DateTime now = DateTime.now();
        for (HumanReadableSchedule.Subschedule subschedule : schedule.getSubschedules()) {
            DateTime reference = now;
            EnumSet<DayOfWeek> daysOfWeek = subschedule.getDays();
            DateTime from = subschedule.getFrom();
            DateTime until = subschedule.getUntil();
            if (until != null && until.getYear() == 0) {
                until = until.year().setCopy(now.getYear());
            }
            if (from != null) {
                if (from.getYear() == 0) {
                    from = from.year().setCopy(now.getYear());
                    if (from.isAfter(until)) {
                        from = from.year().addToCopy(-1);
                    }
                }
                if (from.isAfterNow()) {
                    reference = from;
                }
            }
            for (DayOfWeek day : daysOfWeek) {
                DateTime dateWithDay = reference.dayOfWeek().setCopy(day.getValue());
                if (dateWithDay.isBefore(now.withTime(LocalTime.MIDNIGHT))) {
                    dateWithDay = dateWithDay.plusWeeks(1);
                }
                for (LocalTime time : subschedule.getTimes()) {
                    DateTime date = dateWithDay.withTime(time);
                    if (nextDate == null || (date.isAfterNow() && date.isBefore(nextDate))) {
                        if (from != null && date.isBefore(from)) {
                            continue;
                        }
                        if (until != null && date.isAfter(until)) {
                            continue;
                        }
                        nextDate = date;
                    }
                }
            }

        }
        return nextDate == null ? null : nextDate.toDate();
    }
}
