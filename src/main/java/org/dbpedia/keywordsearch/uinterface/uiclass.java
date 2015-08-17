package org.dbpedia.keywordsearch.uinterface;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dbpedia.keywordsearch.Initializer.initializer;
import org.dbpedia.keywordsearch.datastructures.ListFunctions;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
import org.dbpedia.keywordsearch.importer.neo4j;
import org.dbpedia.keywordsearch.serverproperties.pathvariables;
import org.dbpedia.keywordsearch.indexer.ESNode;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.dbpedia.keywordsearch.ngramgenerator.NGramModel;
import org.dbpedia.keywordsearch.propagator.propagator;
import org.dbpedia.keywordsearch.urimapper.Mapper;
import org.elasticsearch.search.SearchHit;
import org.neo4j.graphdb.GraphDatabaseService;

public class uiclass {
    private pathvariables Instance;
    public uiclass() throws IOException{
            this.Instance=new pathvariables();
    }
    private File[] rdffileiterator(){
        File folder = new File(this.Instance.getrdf());
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }
    private String graphpath(){
        
        return this.Instance.getgraph();
    }
    public static void main(String[] args) throws IOException {
        /*Cluster Initialization*/
        ESNode esnode=new ESNode();
        esnode.StartCluster("DBpediacluster");
       
        /*Indexing of classes*/
        esnode.lemoncluster("./resources/dbpedia_3Eng_class.ttl","classes");
        
        /*Indexing of Properties*/
        esnode.lemoncluster("./resources/dbpedia_3Eng_property.ttl","properties");
        
        /*Enriching them with surfaceforms*/
        esnode.rdfcluster("./resources/en_surface_forms.ttl", "surfaceforms");
        
        /*Indexing DBpedia labels*/
        esnode.rdfcluster("./resources/dbpedia_labels.ttl", "dbpedialabels");
        
        esnode.datatypeindex("./resources/datatypes", "datatypes");
        
        /*A simple query and results*/
        SearchHit[] results=esnode.transportclient("8000kilometre", "datatypes");
        for (SearchHit hit : results) {
            Map<String,Object> result = hit.getSource();
            System.out.println(result.values());
        }
        System.out.println("=================================");
        String Query = "Bristol City FC gender";
        NGramModel ngrams = new NGramModel();
        ngrams.CreateNGramModel(Query);
        
        Mapper mappings = new Mapper();
        mappings.BuildMappings(esnode,ngrams.getNGramMod());
        initializer init= new initializer();
        init.initiate(mappings.getMappings(),ngrams.getNGramMod());
        
        /* extracting paths where the graphdb has to be formed*/
        uiclass pathsetter =new uiclass();
        File[] listoffiles = pathsetter.rdffileiterator();
        String graphpath = pathsetter.graphpath();
        
        /* Formation of graph database at specified path*/
        neo4j graphdb = new neo4j(graphpath);
        GraphDatabaseService gdb=graphdb.getgdbservice();
        System.out.println("asfasf");
        for (File file : listoffiles) { 
          if (file.isFile()) /*extracting all the files in the specified folder*/ {
               graphdb.graphdbform(gdb, pathsetter.Instance.getrdf()+'/'+file.getName());
           }
        }   
        propagator finalresults = new propagator();
        finalresults.PropagateInit(gdb,init.getResultsList());
        List<ResultDataStruct> actual= init.getResultsList();
        
        /* Sorting the final nodes according to explanation score and energy score  */
        ListFunctions.sortresults(actual);
        System.out.println(" Retrieving results in decreasing order of relevancy.... ");
        System.out.println();
        for(int i=actual.size()-1;i>=0;i--){
            System.out.println(" URI : "+actual.get(i).getURI()+", Colors : "+actual.get(i).getImage()+"}");
        }
    }
}
