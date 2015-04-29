package org.amikhalev.sprinklers.service.impl;

import org.amikhalev.sprinklers.service.ScheduleParseException;
import org.hamcrest.core.StringContains;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class HumanReadableScheduleParserTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    HumanReadableScheduleParser scheduleParser;

    @org.junit.Test
    public void testParseBadToken() throws Exception {
        exception.expect(ScheduleParseException.class);
        exception.expectMessage("Bad token");
        final String input = "asldfawiueb";
        try {
            scheduleParser.parse(input);
        } catch (ScheduleParseException e) {
            assertEquals("Input in exception doesn't match", input, e.getInput());
            assertEquals("Wrong start", 0, e.getStart());
            assertEquals("Wrong end", 0, e.getEnd());
            assertThat("Incorrect error message", e.getError(), StringContains.containsString("Bad token"));
            throw e;
        }
    }

    @Test
    public void testParseLeftoverTokens() throws Exception {
        exception.expect(ScheduleParseException.class);
        exception.expectMessage("Tokens left over");
        scheduleParser.parse("at 4:00 pm at");
    }

    @Test
    public void testParseDayOfWeekError() throws Exception {
        exception.expect(ScheduleParseException.class);
        exception.expectMessage("Expected day of week");
        final String input = "at 0 on at";
        scheduleParser.parse(input);
    }

    @Test
    public void testParseTime() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 5 am and 6:00 and 10:00 pm and 10:30:30 and 10:30:30 pm");
        List<HumanReadableSchedule.Subschedule> subschedules = schedule.getSubschedules();
        assertEquals("Schedule doesn't have one subschedule", 1, subschedules.size());
        HumanReadableSchedule.Subschedule subschedule = subschedules.get(0);
        Set<LocalTime> times = subschedule.getTimes();
        assertEquals("Subschedule doesn't have 5 times", 5, times.size());
        assertTrue("Subschedule times doesn't contain '5 am'", times.contains(new LocalTime(5, 0)));
        assertTrue("Subschedule times doesn't contain '6:00'", times.contains(new LocalTime(6, 0)));
        assertTrue("Subschedule times doesn't contain '10:00 pm'", times.contains(new LocalTime(10 + 12, 0)));
        assertTrue("Subschedule times doesn't contain '10:30:30'", times.contains(new LocalTime(10, 30, 30)));
        assertTrue("Subschedule times doesn't contain '10:30:30 pm'", times.contains(new LocalTime(10 + 12, 30, 30)));
        Set<DayOfWeek> daysOfWeek = subschedule.getDays();
        assertTrue("Subschedule days doesn't contain all days of week", daysOfWeek.containsAll(EnumSet.allOf(DayOfWeek.class)));
    }

    @Test
    public void testParse12PM() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 12:00 pm");
        Set<LocalTime> times = schedule.getSubschedules().get(0).getTimes();
        assertTrue("'12:00 pm' was not parsed as noon", times.contains(new LocalTime(12, 0)));
    }

    @Test
    public void testParse12AM() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 12:00 am");
        Set<LocalTime> times = schedule.getSubschedules().get(0).getTimes();
        assertTrue("'12:00 am' was not parsed as midnight", times.contains(LocalTime.MIDNIGHT));
    }

    @Test
    public void testParseDays() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 0 on monday and wednesday and friday");
        Set<DayOfWeek> daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain monday", daysOfWeek.remove(DayOfWeek.MONDAY));
        assertTrue("Days of week doesn't contain wednesday", daysOfWeek.remove(DayOfWeek.WEDNESDAY));
        assertTrue("Days of week doesn't contain friday", daysOfWeek.remove(DayOfWeek.FRIDAY));
        assertTrue("Days of week contains extra days: " + daysOfWeek, daysOfWeek.isEmpty());
        schedule = scheduleParser.parseToSchedule("at 0 on tuesday and thursday and saturday");
        daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain tuesday", daysOfWeek.remove(DayOfWeek.TUESDAY));
        assertTrue("Days of week doesn't contain thursday", daysOfWeek.remove(DayOfWeek.THURSDAY));
        assertTrue("Days of week doesn't contain saturday", daysOfWeek.remove(DayOfWeek.SATURDAY));
        assertTrue("Days of week contains extra days: " + daysOfWeek, daysOfWeek.isEmpty());
        schedule = scheduleParser.parseToSchedule("at 0 on sunday");
        daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain sunday", daysOfWeek.remove(DayOfWeek.SUNDAY));
        assertTrue("Days of week contains extra days: " + daysOfWeek, daysOfWeek.isEmpty());
    }

    @Test
    public void testParseDaysThrough() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 0 on monday-wednesday");
        Set<DayOfWeek> daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain monday", daysOfWeek.remove(DayOfWeek.MONDAY));
        assertTrue("Days of week doesn't contain tuesday", daysOfWeek.remove(DayOfWeek.TUESDAY));
        assertTrue("Days of week doesn't contain wednesday", daysOfWeek.remove(DayOfWeek.WEDNESDAY));
        assertTrue("Days of week contains extra days: " + daysOfWeek, daysOfWeek.isEmpty());
        schedule = scheduleParser.parseToSchedule("at 0 on fri through mon");
        daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain friday", daysOfWeek.remove(DayOfWeek.FRIDAY));
        assertTrue("Days of week doesn't contain saturday", daysOfWeek.remove(DayOfWeek.SATURDAY));
        assertTrue("Days of week doesn't contain sunday", daysOfWeek.remove(DayOfWeek.SUNDAY));
        assertTrue("Days of week doesn't contain monday", daysOfWeek.remove(DayOfWeek.MONDAY));
        assertTrue("Days of week contains extra days: " + daysOfWeek, daysOfWeek.isEmpty());
    }

    @Test
    public void testParseShortDays() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 0 on mon and wed and fri and sun");
        Set<DayOfWeek> daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain mon", daysOfWeek.contains(DayOfWeek.MONDAY));
        assertTrue("Days of week doesn't contain wed", daysOfWeek.contains(DayOfWeek.WEDNESDAY));
        assertTrue("Days of week doesn't contain fri", daysOfWeek.contains(DayOfWeek.FRIDAY));
        assertTrue("Days of week doesn't contain sun", daysOfWeek.contains(DayOfWeek.SUNDAY));
        schedule = scheduleParser.parseToSchedule("at 0 on tue and thur and sat");
        daysOfWeek = schedule.getSubschedules().get(0).getDays();
        assertTrue("Days of week doesn't contain tue", daysOfWeek.contains(DayOfWeek.TUESDAY));
        assertTrue("Days of week doesn't contain thur", daysOfWeek.contains(DayOfWeek.THURSDAY));
        assertTrue("Days of week doesn't contain sat", daysOfWeek.contains(DayOfWeek.SATURDAY));
    }

    @Test
    public void testAndAtEnd() throws Exception {
        exception.expect(ScheduleParseException.class);
        exception.expectMessage("Expected");
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 0 and");
    }

    @Test
    public void testFromAndUntil() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 0 from 1/1/10 until 0:0:0 am 1/1");
        HumanReadableSchedule.Subschedule subschedule = schedule.getSubschedules().get(0);
        DateTime from = subschedule.getFrom();
        DateTime until = subschedule.getUntil();
        assertEquals("From doesn't match", new DateTime(2010, 1, 1, 0, 0), from);
        assertEquals("Until doesn't match", new DateTime(0, 1, 1, 0, 0, 0), until);
    }

    @Test
    public void testMultipleSubschedules() throws Exception {
        HumanReadableSchedule schedule = scheduleParser.parseToSchedule("at 6 am and at 7 pm on mon-fri");
        List<HumanReadableSchedule.Subschedule> subschedules = schedule.getSubschedules();
        assertEquals("Schedule doesn't have 2 subschedules", 2, subschedules.size());
        HumanReadableSchedule.Subschedule subschedule0 = subschedules.get(0);
        Set<LocalTime> times = subschedule0.getTimes();
        assertEquals("Subschedule doesn't have 1 times", 1, times.size());
        assertTrue("Subschedule times doesn't contain '6am'", times.contains(new LocalTime(6, 0)));
        Set<DayOfWeek> daysOfWeek = subschedule0.getDays();
        assertTrue("Subschedule days doesn't contain all days of week", daysOfWeek.containsAll(EnumSet.allOf(DayOfWeek.class)));
    }
}