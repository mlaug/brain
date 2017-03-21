package com.thoughtworks.bulb.repository;

import com.thoughtworks.bulb.domain.Bulb;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface BulbRepository extends GraphRepository<Bulb> {

}
