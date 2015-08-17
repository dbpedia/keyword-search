package org.dbpedia.keywordsearch.Initializer.interfaces;

public interface EnergyFunction {
    public Double energy_score(String ngram, String label);
}
