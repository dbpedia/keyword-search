package org.dbpedia.keywordsearch.datastructures;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*This datastructure defines some functions for the datastructures defined in this package.*/
 public class ListFunctions {
     public static void sortresults(List<ResultDataStruct> resultslist){
        Collections.sort(resultslist, new Comparator<ResultDataStruct>() {    
            @Override
            public int compare(ResultDataStruct o1, ResultDataStruct o2) {
                int date1Diff = o1.getExplainationScore().compareTo(o2.getExplainationScore());
                return date1Diff == 0 ? 
                        o1.getEnergyScore().compareTo(o2.getEnergyScore()) :
                        date1Diff;
            }               
        });
    }
     
     public static boolean Containselement(NGramStruct ngram, List<NGramStruct> ngrams){
         int i=0;
         boolean flag=false;
         for(i=0;i<=ngrams.size()-1;i++){
             if(ngrams.get(i).getLabel().equals(ngram.getLabel())){
                 flag=true;
             }
         }
         return flag;
     }
     public static ResultDataStruct ContainsResultDataStruct(String URI,List<ResultDataStruct> results){
         for (ResultDataStruct node : results) {
             if(node.getURI().equals(URI)) return node;
         }
         return null;
     }
     
     public static boolean isinBlackList(List<BlacklistDataStruct> blacklist, String URI1, String URI2){
         for(BlacklistDataStruct blacknode : blacklist){
             if(blacknode.getfirstURI().equals(URI1) && blacknode.getsecondURI().equals(URI2)){
                 return true;
             }
             if(blacknode.getsecondURI().equals(URI1) && blacknode.getfirstURI().equals(URI2)){
                 return true;
             }
         }
         return false;
     }
 }