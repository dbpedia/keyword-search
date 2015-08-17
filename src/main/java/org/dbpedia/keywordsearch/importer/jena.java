
package org.dbpedia.keywordsearch.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

/* This class return an iterator on all the rdf triples from the given rdf file */
public class jena {
    /* Jena is used for extracting rdf triple from .ttl files */
    public static StmtIterator jena(String rdfpath) {
        FileManager.get().addLocatorClassLoader(neo4j.class.getClassLoader());
        Model model = FileManager.get().loadModel(rdfpath, null, "TURTLE");
        
        /* Creates an iterator on the rdf triples from the specified file */
        StmtIterator iter = model.listStatements();
        return iter;
    }
}
