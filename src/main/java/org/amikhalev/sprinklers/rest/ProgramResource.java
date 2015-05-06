package org.amikhalev.sprinklers.rest;

import org.amikhalev.sprinklers.dto.ProgramObject;
import org.amikhalev.sprinklers.dto.ProgramSectionObject;
import org.amikhalev.sprinklers.model.Program;
import org.amikhalev.sprinklers.model.ProgramSection;
import org.amikhalev.sprinklers.repositories.ProgramRepository;
import org.amikhalev.sprinklers.service.Scheduler;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by alex on 5/3/15.
 */
@Path("/programs")
@Produces(MediaType.APPLICATION_JSON)
public class ProgramResource {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private Mapper beanMapper;

    @Autowired
    private Scheduler scheduler;

    @GET
    public Iterable<ProgramObject> list() {
        return StreamSupport.stream(programRepository.findAll().spliterator(), false)
                .map(program -> beanMapper.map(program, ProgramObject.class))
                .collect(Collectors.toList());
    }

    @POST
    public ProgramObject add(@RequestBody ProgramObject programObject) {
        Program program = beanMapper.map(programObject, Program.class);
        Program result = programRepository.save(program);
        return beanMapper.map(result, ProgramObject.class);
    }

    @Path("/{id}")
    @GET
    public ProgramObject findById(@PathParam("id") Integer id) {
        return beanMapper.map(programRepository.findOne(id), ProgramObject.class);
    }

    @Path("/{id}")
    @PUT
    public ProgramObject update(@PathParam("id") Integer id, @RequestBody ProgramObject programObject) {
        Program program = programRepository.findOne(id);
        beanMapper.map(programObject, program);
        Program result = programRepository.save(program);
        return beanMapper.map(result, ProgramObject.class);
    }

    @Path("/{id}")
    @DELETE
    public void remove(@PathParam("id") Integer id) {
        programRepository.delete(id);
    }

    @Path("/{id}/execute")
    @POST
    public void execute(@PathParam("id") Integer id) {
        Program program = programRepository.findOne(id);
        scheduler.execute(program);
    }

    @Path("/{id}/schedule")
    @POST
    public void schedule(@PathParam("id") Integer id) {
        Program program = programRepository.findOne(id);
        scheduler.schedule(program);
    }

    @Path("/{id}/sections")
    @GET
    public Iterable<ProgramSectionObject> listSections(@PathParam("id") Integer id) {
        Program program = programRepository.findOne(id);
        return program.getSections()
                .stream()
                .map(programSection -> beanMapper.map(programSection, ProgramSectionObject.class))
                .collect(Collectors.toList());
    }

    @Path("/{id}/sections")
    @PUT
    public Iterable<ProgramSectionObject> setSections(@PathParam("id") Integer id, @RequestBody Iterable<ProgramSectionObject> sections) {
        Program program = programRepository.findOne(id);
        List<ProgramSection> programSections = StreamSupport.stream(sections.spliterator(), false)
                .map(programSectionObject -> beanMapper.map(programSectionObject, ProgramSection.class))
                .collect(Collectors.toList());
        program.setSections(programSections);
        programRepository.save(program);
        return sections;
    }
}
