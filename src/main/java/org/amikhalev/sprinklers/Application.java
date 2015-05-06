package org.amikhalev.sprinklers;

import com.sun.net.httpserver.HttpServer;
import org.amikhalev.sprinklers.dto.UncaughtExceptionObject;
import org.amikhalev.sprinklers.model.Program;
import org.amikhalev.sprinklers.model.SectionModel;
import org.amikhalev.sprinklers.repositories.ProgramRepository;
import org.amikhalev.sprinklers.repositories.SectionModelRepository;
import org.amikhalev.sprinklers.service.Scheduler;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import java.net.URI;

/**
 * Created by alex on 4/20/15.
 */
public class Application extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private ApplicationContext context;
    private HttpServer server;

    public Application() {
        packages("org.amikhalev.sprinklers.rest");
        register(new ExceptionMapper<Exception>() {
            @Override
            public Response toResponse(Exception exception) {
                logger.error("rest error", exception);
                UncaughtExceptionObject entity = new UncaughtExceptionObject(exception);
                return Response
                        .status(500)
                        .entity(entity)
                        .build();
            }
        });
    }

    public static void main(String[] args) {
        new Application().start();
    }

    public void start() {
        context = new ClassPathXmlApplicationContext("spring-config.xml");
        this.property("contextConfig", context);

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
//            scheduler.execute(program);
        }

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9899).build();
        server = JdkHttpServerFactory.createHttpServer(baseUri, this);
    }
}
