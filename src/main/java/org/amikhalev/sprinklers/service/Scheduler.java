package org.amikhalev.sprinklers.service;

import org.amikhalev.sprinklers.model.Program;

/**
 * Created by alex on 4/28/15.
 */
public interface Scheduler {
    /**
     * Executes this program immediatly
     *
     * @param program The program to execute
     */
    void execute(Program program);

    /**
     * Schedules a program to be executed at it's defined time. If the program has already been scheduled in the past,
     * it will cancel it and reschedule it.
     *
     * @param program The program to schedule
     */
    void schedule(Program program);

    /**
     * Cancels a program's execution.
     */
    void cancel(Integer id);

    /**
     * Cancels all programs' executions.
     */
    void cancelAll();
}
