package org.dbpedia.keywordsearch.urimapper.interfaces;

import java.util.List;
import java.util.Map;
import org.dbpedia.keywordsearch.datastructures.MapperDataStruct;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;

public interface MapperInterface {
    public void BuildMappings(IndexerInterface node, List<NGramStruct> ngramlist);
    public Map<Integer,MapperDataStruct> getMappings();
}
