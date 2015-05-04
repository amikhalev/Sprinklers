package org.amikhalev.sprinklers.rest;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.amikhalev.sprinklers.model.Program;
import org.amikhalev.sprinklers.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by alex on 5/3/15.
 */
@Path("programs")
public class ProgramResource {
    @Autowired
    private ProgramRepository programRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPrograms() {
        ArrayList<Program> programs = (ArrayList<Program>) programRepository.findAll();
        return Response
                .status(200)
                .entity(programs)
                .build();
    }
}
