package com.thoughtworks.bulb.repository;

import com.thoughtworks.bulb.domain.Bulb;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface BulbRepository extends GraphRepository<Bulb> {

    @Query("MATCH (parent:Bulb),(child:Bulb) WHERE parent.uuid = {0} AND child.uuid = {1} CREATE (parent)-[r:LINKED_TO]->(child) RETURN r")
    void linkBulbs(String parent, String child);

    @Query("MATCH (bulb:Bulb),(reference:BulbReference) WHERE bulb.uuid = {0} AND reference.uuid = {1} CREATE (bulb)-[r:BACKED_BY_REFERENCE]->(reference) RETURN r")
    void addReference(String bulb, String reference);

    Bulb findByUuid(String uuid);

    Iterable<Bulb> findByUserId(String userId);
}
