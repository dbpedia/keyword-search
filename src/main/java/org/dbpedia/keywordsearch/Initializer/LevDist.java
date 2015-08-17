package org.dbpedia.keywordsearch.Initializer;

import org.dbpedia.keywordsearch.Initializer.interfaces.EnergyFunction;

/* This is the class for calculating energy score for given label and the ngram.
    We can enter any other energy function by adding its method in this class. 
    Current energy function used is Levenshtein Distance */
public class LevDist implements EnergyFunction {
    
    @Override
    public Double energy_score(String ngram, String label){
        
        Double energy_score;
        energy_score=similarity(ngram,label);
        return 10*energy_score;
    }
    
    /* This is the method for Levenshtein Distance. Levenshtein distance calculates
        similarity between two strings by comparing minimum number of words needed
        to convert one word to another */
    private Double similarity(String ngram, String label){
        String longer = ngram, shorter = label;
	if (ngram.length() < label.length()) { // longer should always have greater length
	    longer = label; shorter = ngram;
	}
	int longerLength = longer.length();
	if (longerLength == 0) { return 1.0; }
	return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
    
    private static int editDistance(String ngram, String label) {
	ngram = ngram.toLowerCase();
	label = label.toLowerCase();

	int[] costs = new int[label.length() + 1];
	for (int i = 0; i <= ngram.length(); i++) {
            int lastValue = i;
	    for (int j = 0; j <= label.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (ngram.charAt(i - 1) != label.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue),
                                 costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	    }
	    if (i > 0)
	        costs[label.length()] = lastValue;
	}
	return costs[label.length()];
    }
}
