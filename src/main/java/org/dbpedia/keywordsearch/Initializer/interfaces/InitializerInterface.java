
package org.dbpedia.keywordsearch.Initializer.interfaces;

import java.util.List;
import java.util.Map;
import org.dbpedia.keywordsearch.datastructures.MapperDataStruct;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;

public interface InitializerInterface {
    public void initiate (Map<Integer,MapperDataStruct> urimaps, List<NGramStruct> ngrams);
    public List<ResultDataStruct> getResultsList();
}
