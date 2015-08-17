//package org.dbpedia.keywordsearch.initializer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.dbpedia.keywordsearch.Initializer.initializer;
//import org.dbpedia.keywordsearch.datastructures.MapperDataStruct;
//import org.dbpedia.keywordsearch.datastructures.NGramStruct;
//import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
//import org.dbpedia.keywordsearch.ngramgenerator.NGramModel;
//import org.junit.Test;
//import static org.junit.Assert.assertEquals;
//
///*** Junit tests for Initializer unit ***/
//public class initializertestcases {
//    
//    /***   Test Case 1 for Initializer ***/
//    @Test
//    public void testcase1(){
//        List<NGramStruct> ngrams;
//        System.out.println();
//        System.out.println("==========Test Case 1==========");
//        String Query = "google founder birthplace michigan";
//        System.out.println();
//        System.out.println("Entered Query : "+Query);
//        System.out.println();
//        
//        /***    Calling NGram Generator from the user inserted query. This method returns the list of ngrams ***/
//        NGramModel fetchedngrams=new NGramModel(Query);
//        ngrams=fetchedngrams.getNGramMod();
//       
//        /*** This is the calling of URI Mapper. 'mappings' will be returned
//              by URI Mapper. For this test case we are creating it manually ***/
//        Map<Integer,MapperDataStruct> mappings = new HashMap<>();               /**********/
//        List<String> URIgoogle = new ArrayList<>();                             /**********/
//        URIgoogle.add("dbr/google");                                            /**********/
//        URIgoogle.add("dbr/googler");                                           /**********/
//        List<String> labelgoogle = new ArrayList<>();                           /**********/ 
//        labelgoogle.add("google");                                              /**********/
//        labelgoogle.add("googler");                                             /**********/
//        MapperDataStruct google = new MapperDataStruct(URIgoogle,labelgoogle);  /**********/
//                                                                                /**********/
//        List<String> URIfounder = new ArrayList<>();                            /**********/
//        URIfounder.add("dbo/founder");                                          /**********/
//        List<String> labelfounder = new ArrayList<>();                          /**********/
//        labelfounder.add("founder");                                            /**********/
//        MapperDataStruct foundr = new MapperDataStruct(URIfounder,labelfounder);/**********/
//                                                                                /**********/
//        List<String> URIsbirthplace = new ArrayList<>();                        /**********/
//        URIsbirthplace.add("dbo/birthplace");                                   /**********/
//        URIsbirthplace.add("dbo/place_of_birth");                               /**********/
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
//        
//        /*** Expected Result creation ****/
//        List<ResultDataStruct> expected=new ArrayList<>();
//        ResultDataStruct google1ds=new ResultDataStruct(URIgoogle.get(0),1.0,10.0,0,0);
//        ResultDataStruct google2ds=new ResultDataStruct(URIgoogle.get(1),1.0,8.5,0,0);
//        ResultDataStruct founderds=new ResultDataStruct(URIfounder.get(0),1.0,10.0,1,1);
//        ResultDataStruct birthplace1ds=new ResultDataStruct(URIsbirthplace.get(0),1.0,10.0,2,2);
//        ResultDataStruct birthplace2ds=new ResultDataStruct(URIsbirthplace.get(1),1.0,0.0,2,2);
//        ResultDataStruct michigands=new ResultDataStruct(URIscity.get(0),1.0,10.0,3,3);
//        expected.add(google1ds);expected.add(google2ds);expected.add(founderds);expected.add(birthplace1ds);
//        expected.add(birthplace2ds);expected.add(michigands);
//        /***   Expected result creation ends here  ***/
//        
//        /*** Calling Initializer unit for generating actuals results. This method returns List of initialized nodes ***/
//        initializer init= new initializer(mappings,ngrams);
//        List<ResultDataStruct> actual= init.getResultsList();
//        
//        /**   Using Junit to check if actual results matches expected  ***/
//        System.out.println("============Initiated Nodes==========");
//        System.out.println();
//        for(int i=0; i<actual.size();i++){
//            System.out.println("Node "+i+" initiated {" + "URI : "+actual.get(i).getURI()+
//                                ", Exp score: "+actual.get(i).getExplainationScore()+
//                                ", Energy Score: "+actual.get(i).getEnergyScore()+
//                                ", Colour set: "+actual.get(i).getColors()+" }");   
//            
//            assertEquals(expected.get(i).getURI(),actual.get(i).getURI());
//            assertEquals(expected.get(i).getExplainationScore(),actual.get(i).getExplainationScore());
//            assertEquals(expected.get(i).getEnergyScore(),actual.get(i).getEnergyScore(),0.1);
//            assertEquals(expected.get(i).getColors(),actual.get(i).getColors());
//        }
//    }
//}