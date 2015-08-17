//package org.dbpedia.keywordsearch.propagator;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.dbpedia.keywordsearch.Initializer.initializer;
//import org.dbpedia.keywordsearch.datastructures.ListFunctions;
//import org.dbpedia.keywordsearch.datastructures.MapperDataStruct;
//import org.dbpedia.keywordsearch.datastructures.NGramStruct;
//import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
//import org.dbpedia.keywordsearch.importer.neo4j;
//import org.dbpedia.keywordsearch.ngramgenerator.NGramModel;
//import org.dbpedia.keywordsearch.serverproperties.pathvariables;
//import static org.junit.Assert.assertEquals;
//import org.junit.Before;
//import org.junit.Test;
//import org.neo4j.graphdb.GraphDatabaseService;
//
//public class propagatortestcases {
//   GraphDatabaseService gdb;
//   List<NGramStruct> ngrams;
//   Map<Integer,MapperDataStruct> mappings;
//   neo4j graphdb;
//   
//    @Before 
//    public void setUp() {
//        
//        /* path settings for the graph and data  */
//        pathvariables Instance=new pathvariables();
//        File folder = new File(Instance.getrdf());
//        File[] listOfFiles = folder.listFiles();
//        String graphpath = Instance.getgraph();
//        
//        /*  Initiating the graph database server  */
//        System.out.println(" Starting the graph database server at the specified location..... ");
//        System.out.println();
//        graphdb = new neo4j(graphpath);
//        gdb=graphdb.getgdbservice();
//        
//        /* This is to avoid error caused when a logger is created before log4j is initialized.
//            log4j is used external package used in neo4j while creating and inserting nodes in it. */
//        Logger.getRootLogger().setLevel(Level.OFF);
//        
//        /* Creating the Graph  */
//        System.out.println(" Inserting the nodes in the graph.... ");
//        System.out.println();
//        
//        for (File file : listOfFiles) { 
//          if (file.isFile()) /*extracting all the files in the specified folder*/ {
//               graphdb.graphdbform(gdb, Instance.getrdf()+'/'+file.getName());
//           }
//        }
//        
//        
//        System.out.println();
//        System.out.println("==========Test Case 1==========");
//        String Query = "google founder birthplace michigan";
//        System.out.println();
//        System.out.println("Entered Query : "+Query);
//        System.out.println();
//        
//        /***    Calling NGram Generator from the user inserted query. This method returns the list of ngrams ***/
//
//        System.out.println(" Constructing n-gram hierarchy out of the query....");
//        System.out.println();
//        NGramModel fetchedngrams=new NGramModel(Query);
//        ngrams=fetchedngrams.getNGramMod();
//       
//        /*** This is the calling of URI Mapper. 'mappings' will be returned
//              by URI Mapper. For this test case we are creating it manually ***/
//        System.out.println(" Manually constructing the mappings for the respective ngrams.... ");
//        System.out.println();
//        mappings = new HashMap<>();                                             /**********/
//        List<String> URIgoogle = new ArrayList<>();                             /**********/
//        URIgoogle.add("Google@en");                                             /**********/
//        URIgoogle.add("http://dbr.com/Googler");                                /**********/
//        List<String> labelgoogle = new ArrayList<>();                           /**********/ 
//        labelgoogle.add("google");                                              /**********/
//        labelgoogle.add("googler");                                             /**********/
//        MapperDataStruct google = new MapperDataStruct(URIgoogle,labelgoogle);  /**********/
//                                                                                /**********/
//        List<String> URIfounder = new ArrayList<>();                            /**********/
//        URIfounder.add("http://dbo.com/founder");                               /**********/
//        List<String> labelfounder = new ArrayList<>();                          /**********/
//        labelfounder.add("founder");                                            /**********/
//        MapperDataStruct foundr = new MapperDataStruct(URIfounder,labelfounder);/**********/
//                                                                                /**********/
//        List<String> URIsbirthplace = new ArrayList<>();                        /**********/
//        URIsbirthplace.add("http://dbo.com/birthplace");                        /**********/
//        URIsbirthplace.add("http://dbo.com/place_of_birth");                    /**********/
//        List<String> labelsbirthplace = new ArrayList<>();                      /**********/
//        labelsbirthplace.add("birthplace");                                     /**********/
//        labelsbirthplace.add("place of birth");                                 /**********/
//        MapperDataStruct birthplace = new MapperDataStruct(URIsbirthplace,      /**********/
//                                                            labelsbirthplace);  /**********/
//                                                                                /**********/
//        List<String> URIscity = new ArrayList<>();                              /**********/
//        URIscity.add("michigan@en");                                            /**********/
//        List<String> labelscity= new ArrayList<>();                             /**********/
//        labelscity.add("michigan");                                             /**********/
//        MapperDataStruct city = new MapperDataStruct(URIscity,labelscity);      /**********/
//        mappings.put(6, google);mappings.put(7,foundr);                         /**********/
//        mappings.put(8, birthplace);mappings.put(9,city);                       /**********/
//        
//        /*** URIMapper manual creation ends here ***/
//    }
//   
//    @Test
//    public void testPropagatorDemoQuery(){
//        /*  Activating initial nodes  */
//        System.out.println(" Activating the Initial nodes from the mappings.... ");
//        System.out.println();
//        initializer init= new initializer(mappings,ngrams);
//        
//        /* Propagating on the activated nodes by calling the propagator constructor  */
//        System.out.println(" Propagating the activated nodes ....  ");
//        System.out.println();
//        propagator finalresults = new propagator(gdb,init.getResultsList());
//        List<ResultDataStruct> actual= init.getResultsList();
//        
//        /* Sorting the final nodes according to explanation score and energy score  */
//        ListFunctions.sortresults(actual);
//        
//        /* Retrieving the results in order of relevancy the query */
//        System.out.println(" Retrieving results in decreasing order of relevancy.... ");
//        System.out.println();
//        for(int i=actual.size()-1;i>=0;i--){
//            System.out.println(" URI : "+actual.get(i).getURI()+" {Explanation score : "+actual.get(i).getExplainationScore()+", Energy Score : " + actual.get(i).getEnergyScore()+", Colors : "+actual.get(i).getColors()+"}");
//        }
//        
//        assertEquals("http://dbr.com/Larry_Page",actual.get(actual.size()-1).getURI());
//    }
//    
//    public void tearDown(  ) {
//        graphdb.shutDown();
//   }
//}
