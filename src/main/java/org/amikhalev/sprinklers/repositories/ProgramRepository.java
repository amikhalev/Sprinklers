package org.amikhalev.sprinklers.repositories;

import org.amikhalev.sprinklers.model.Program;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alex on 4/30/15.
 */
public interface ProgramRepository extends CrudRepository<Program, Integer> {
}
