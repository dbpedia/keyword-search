package org.dbpedia.keywordsearch.indexer.Interface;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.elasticsearch.search.SearchHit;

public interface IndexerInterface {
    
    public void StartCluster(String clustername);
    /*
    Methods for indexing needs to be declared in the implementing class
    Because of their varying nature
    */
    public SearchHit[] transportclient(String query, String path);    

    public void lemoncluster(String homeKartikSinghalhomeIndexingDatadbpedia_, String classes) throws FileNotFoundException, IOException;

    public void rdfcluster(String homeKartikSinghalhomeIndexingDataen_surfa, String surfaceforms) throws FileNotFoundException, IOException;

    public void datatypeindex(String homeKartikSinghalhomeIndexingDatadatatype, String datatypes) throws FileNotFoundException, IOException;

    }
