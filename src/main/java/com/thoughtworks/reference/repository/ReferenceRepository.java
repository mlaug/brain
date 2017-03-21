package com.thoughtworks.reference.repository;

import com.thoughtworks.reference.domain.Reference;
import org.springframework.data.neo4j.repository.GraphRepository;


public interface ReferenceRepository extends GraphRepository<Reference> {

}
