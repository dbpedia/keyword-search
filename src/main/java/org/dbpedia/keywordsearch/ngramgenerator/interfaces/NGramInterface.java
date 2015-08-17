package org.dbpedia.keywordsearch.ngramgenerator.interfaces;

import java.util.List;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;

public interface NGramInterface {
    public void CreateNGramModel(String keyword);
    public List<NGramStruct> getNGramMod();
}
