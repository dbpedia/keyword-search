
package org.dbpedia.keywordsearch.importer.interfaces;

import java.io.IOException;
import org.neo4j.graphdb.GraphDatabaseService;

public interface GDBInterface {
    public GraphDatabaseService getgdbservice();
    public void shutDown();
    public void clearDb(String graphpath) throws IOException;
    public void graphdbform(GraphDatabaseService graphdb, String rdfpath);
}
