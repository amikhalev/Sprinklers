package org.amikhalev.sprinklers;

import org.amikhalev.sprinklers.model.Program;
import org.amikhalev.sprinklers.model.SectionModel;
import org.amikhalev.sprinklers.repositories.ProgramRepository;
import org.amikhalev.sprinklers.repositories.SectionModelRepository;
import org.amikhalev.sprinklers.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by alex on 4/20/15.
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");

        SectionModelRepository sectionModelRepository = (SectionModelRepository) context.getBean("sectionModelRepository");
        ProgramRepository programRepository = (ProgramRepository) context.getBean("programRepository");

        Iterable<SectionModel> sections = sectionModelRepository.findAll();
        logger.info("all sections: {}", sections);

        Iterable<Program> programs = programRepository.findAll();
        logger.info("all programs: {}", programs);

        for (Program program : programs) {
            logger.info("Scheduling program " + program.getName());
            scheduler.schedule(program);
            scheduler.execute(program);
        }
    }
}
