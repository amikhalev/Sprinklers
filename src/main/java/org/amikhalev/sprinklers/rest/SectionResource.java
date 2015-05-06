package org.amikhalev.sprinklers.rest;

import com.google.gson.Gson;
import org.amikhalev.sprinklers.dto.SectionObject;
import org.amikhalev.sprinklers.model.SectionModel;
import org.amikhalev.sprinklers.repositories.SectionModelRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by alex on 5/5/15.
 */
@Path("/sections")
@Produces(MediaType.APPLICATION_JSON)
public class SectionResource {
    @Autowired
    private SectionModelRepository sectionModelRepository;
    @Autowired
    private Mapper beanMapper;
    @Autowired
    private Gson gson;

    @GET
    public Iterable<SectionObject> list() {
        return StreamSupport.stream(sectionModelRepository.findAll().spliterator(), false)
                .map(sectionModel -> beanMapper.map(sectionModel, SectionObject.class))
                .collect(Collectors.toList());
    }

    @Path("/{id}")
    @GET
    public SectionObject findById(@PathParam("id") Integer id) {
        SectionModel sectionModel = sectionModelRepository.findOne(id);
        return beanMapper.map(sectionModel, SectionObject.class);
    }
}
