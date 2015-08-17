
package org.dbpedia.keywordsearch.Initializer;

import org.dbpedia.keywordsearch.Initializer.interfaces.EnergyFunction;

public class EnergyFactory {
    public EnergyFunction getEnergyFunction(String EnergyType){
        if(EnergyType.equals("LevDist")){
         return new LevDist();   
        }
        else
            return null;
    }
}
