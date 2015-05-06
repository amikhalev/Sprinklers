package org.amikhalev.sprinklers.service.impl;

import org.amikhalev.sprinklers.exceptions.ScheduleParseException;
import org.amikhalev.sprinklers.model.Program;
import org.amikhalev.sprinklers.model.ProgramSection;
import org.amikhalev.sprinklers.service.ScheduleParser;
import org.amikhalev.sprinklers.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static java.lang.Thread.sleep;

/**
 * Created by alex on 4/20/15.
 */
public class SchedulerImpl implements Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(HumanReadableSchedule.class);
    private ThreadPoolTaskScheduler scheduler;
    private ScheduleParser parser;
    private Map<Integer, ScheduledFuture<?>> futures;

    public SchedulerImpl(ThreadPoolTaskScheduler scheduler, ScheduleParser parser) {
        this.scheduler = scheduler;
        this.parser = parser;
        this.futures = new HashMap<>();
    }

    @Override
    public void execute(Program program) {
        logger.info("Executing program " + program.getName());
        scheduler.execute(new ProgramExecutor(program));
    }

    @Override
    public void schedule(Program program) {
        if (futures.containsKey(program.getId())) {
            logger.info("Program " + program.getName() + " has already been scheduled, cancelling...");
            this.cancel(program.getId());
        }
        Trigger trigger;
        try {
            trigger = parser.parse(program.getSchedule());
        } catch (ScheduleParseException e) {
            logger.error("Error while parsing schedule", e);
            return;
        }
        logger.info("next execution time: " + trigger.nextExecutionTime(new SimpleTriggerContext()));
        ScheduledFuture future = scheduler.schedule(new ProgramExecutor(program), trigger);
        futures.put(program.getId(), future);
        logger.debug("scheduled program " + program.getName());
    }

    @Override
    public void cancel(Integer id) {
        futures.get(id).cancel(false);
    }

    @Override
    public void cancelAll() {
        futures.keySet().forEach(this::cancel);
    }

    private class ProgramExecutor implements Runnable {
        private Program program;

        public ProgramExecutor(Program program) {
            this.program = program;
        }

        public Program getProgram() {
            return program;
        }

        public void setProgram(Program program) {
            this.program = program;
        }

        @Override
        public void run() {
            initialize();
            for (ProgramSection programSection : program.getSections()) {
                programSection.getSectionModel().getSection().on();
                try {
                    sleep((long) (programSection.getTime() * 1000));
                } catch (InterruptedException e) {
                    cleanup();
                    throw new Error(e);
                } finally {
                    programSection.getSectionModel().getSection().off();
                }
            }
            cleanup();
        }

        private void initialize() {
            for (ProgramSection programSection : program.getSections()) {
                programSection.getSectionModel().getSection().initialize();
            }
        }

        private void cleanup() {
            for (ProgramSection programSection : program.getSections()) {
                programSection.getSectionModel().getSection().cleanup();
            }
        }

    }
}
