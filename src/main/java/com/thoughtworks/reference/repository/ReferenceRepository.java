package com.thoughtworks.reference.repository;

import com.thoughtworks.reference.domain.BulbReference;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ReferenceRepository extends GraphRepository<BulbReference> {

    BulbReference findByUuid(String uuid);

}
