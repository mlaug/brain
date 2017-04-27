package com.thoughtworks.bulb.repository;

import com.thoughtworks.bulb.domain.Bulb;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface BulbRepository extends GraphRepository<Bulb> {

    @Query("MATCH (parent:Bulb),(child:Bulb) WHERE parent.uuid = {0} AND child.uuid = {1} CREATE (parent)-[r:PARENT_OF]->(child) RETURN r")
    void linkParentToChild(String parent, String child);

    @Query("MATCH (s:Bulb) WHERE NOT (s)<-[:PARENT_OF]-() RETURN s")
    List<Bulb> findRoots();

    Bulb findByUuid(String uuid);

}
