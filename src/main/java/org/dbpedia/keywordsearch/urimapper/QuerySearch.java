
package org.dbpedia.keywordsearch.urimapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dbpedia.keywordsearch.Initializer.EnergyFactory;
import org.dbpedia.keywordsearch.Initializer.interfaces.EnergyFunction;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.elasticsearch.search.SearchHit;

public class QuerySearch {
    private final List<String> URIs=new ArrayList<String>();
    private final List<String> Labels=new ArrayList<String>();
    private final List<Double> EnergyScoreList = new ArrayList<Double>();
    private final EnergyFactory Energy = new EnergyFactory();
    private final EnergyFunction EnergyCalc=Energy.getEnergyFunction("LevDist");
    public QuerySearch(IndexerInterface node, NGramStruct ngram){
        buildquery(node,ngram);
    }
    
    private void buildquery(IndexerInterface node, NGramStruct ngram) {
       SearchInLemonCluster(node,ngram.getLabel(),"classes");
       SearchInLemonCluster(node,ngram.getLabel(),"properties");
       SearchInRDFCluster(node,ngram.getLabel(),"surfaceforms");
       SearchInRDFCluster(node,ngram.getLabel(),"dbpedialabels");
       if(!extractNumber(ngram.getLabel()).equals("")){
           DatatypeNormalize(node,ngram.getLabel());
       }
    }
    
    private void SearchInLemonCluster(IndexerInterface node, String label, String index){
        SearchHit[] results=node.transportclient(label,index);
        for (SearchHit hit : results) {
            Map<String,Object> result = hit.getSource();
            this.Labels.add(result.values().toString().split(", ")[0].split("\"")[1]);
            this.URIs.add(result.values().toString().split(", ")[1].replaceAll("\\]", ""));
            Double EnergyScore=EnergyCalc.energy_score(result.values().toString().split(", ")[0].split("\"")[1], label);
            this.EnergyScoreList.add(EnergyScore);
            }
    }
    
    private void SearchInRDFCluster(IndexerInterface node, String label, String index){
        SearchHit[] results=node.transportclient(label, index);
        for (SearchHit hit : results) {
            Map<String,Object> result = hit.getSource();
            this.Labels.add(result.values().toString().split(", ")[0].replaceAll("\\[",""));
            this.URIs.add(result.values().toString().split(", ")[1].replaceAll("\\]", ""));
            Double EnergyScore=EnergyCalc.energy_score(result.values().toString().split(", ")[0].replaceAll("\\[",""), label);
            this.EnergyScoreList.add(EnergyScore);
        }
    }
    
    public void DatatypeNormalize(IndexerInterface node, String label){
        String NumberString=extractNumber(label);
        if(label.contains(NumberString+" ")){
            label=label.replace(NumberString+" ", "");
        }
        else if(label.contains(NumberString)){
            label=label.replace(NumberString, "");
        }
        Double Number = Double.parseDouble(NumberString);
        // Datatype edit here
        SearchHit[] results=node.transportclient(label, "datatypes");
        for (SearchHit hit : results) {
            Map<String,Object> result = hit.getSource();
            Number=Number*Double.parseDouble(result.values().toString().split(", ")[0].replaceAll("\\[",""));
            String LabelFactUnit=result.values().toString().split(", ")[2];
            String StdUnit=result.values().toString().split(", ")[3].split("-")[0];
            Double EnergyScore=EnergyCalc.energy_score(label,LabelFactUnit);
            this.EnergyScoreList.add(EnergyScore);
            if ((Number == Math.floor(Number)) && !Double.isInfinite(Number)) {
                this.Labels.add(Number.toString());
                this.URIs.add("\""+String.valueOf(Number.intValue())+"\""+"xsd:Integer");
            }
            else
            this.URIs.add("\""+Number.toString()+"\""+"xsd:Double");
        }
    }
    
    public static String extractNumber(final String str) {                

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(Character c : str.toCharArray()){
            if(Character.isDigit(c)|| c.equals('.')){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;                
            }
        }

        return sb.toString();
    }
    
    public List<String> getURIList(){    return this.URIs;    }
    public List<String> getLabelList(){    return this.Labels;    }
    public List<Double> getEnergyScoreList(){    return this.EnergyScoreList;    }

}
