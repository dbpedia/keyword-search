package org.dbpedia.keywordsearch.propagator.interfaces;

import java.util.List;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
import org.neo4j.graphdb.GraphDatabaseService;

public interface PropagatorInterface {
public void PropagateInit(GraphDatabaseService db, List<ResultDataStruct> results);
}
