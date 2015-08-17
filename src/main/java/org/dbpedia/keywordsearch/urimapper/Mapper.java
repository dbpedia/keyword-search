
package org.dbpedia.keywordsearch.urimapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dbpedia.keywordsearch.Initializer.interfaces.EnergyFunction;
import org.dbpedia.keywordsearch.datastructures.MapperDataStruct;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;
import org.dbpedia.keywordsearch.indexer.ESNode;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.dbpedia.keywordsearch.urimapper.interfaces.MapperInterface;
import org.elasticsearch.search.SearchHit;

public class Mapper implements MapperInterface{
    private final Map<Integer,MapperDataStruct> mappings=new HashMap<Integer,MapperDataStruct>();
    
    @Override
    public void BuildMappings(IndexerInterface node, List<NGramStruct> ngramlist){
        List<String> tempURIs;
        List<String> tempLabels;
        
        for (NGramStruct ngram : ngramlist) {
            tempURIs=new ArrayList<String>();
            tempLabels=new ArrayList<String>();
            QuerySearch buildquery=new QuerySearch(node, ngram);
            MapperDataStruct mapping = new MapperDataStruct(buildquery.getURIList(),buildquery.getLabelList(),buildquery.getEnergyScoreList());
            mappings.put(ngram.getIndex(), mapping);
        }
    }
    
    @Override
    public Map<Integer,MapperDataStruct> getMappings(){ return this.mappings; }
}
