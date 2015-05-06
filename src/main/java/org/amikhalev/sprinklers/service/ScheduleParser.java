package org.amikhalev.sprinklers.service;

import org.amikhalev.sprinklers.exceptions.ScheduleParseException;
import org.springframework.scheduling.Trigger;

/**
 * Created by alex on 4/21/15.
 */
public interface ScheduleParser {
    /**
     * Parses a schedule into a spring scheduling Trigger
     *
     * @param input The schedule string to parse
     * @return The trigger
     * @throws org.amikhalev.sprinklers.exceptions.ScheduleParseException If the input string is invalid
     */
    public Trigger parse(String input) throws ScheduleParseException;
}
