package org.dbpedia.keywordsearch.datastructures;
import java.util.ArrayList;
import java.util.List;


/*This class defines the datastructure for Mappings. The URIMapper will output a list of MapperDataStruct type*/
public class MapperDataStruct {
    private List<String> URIList = new ArrayList<String>();
    private List<String> LabelList = new ArrayList<String>();
    private List<Double> EnergyScore=new ArrayList<Double>();
    public MapperDataStruct(List<String> URIs, List<String> Labels, List<Double> EnergyScore){
        this.URIList.addAll(URIs);
        this.LabelList.addAll(Labels);
        this.EnergyScore.addAll(EnergyScore);
    }

    public void setURIList(List<String> URIs){this.URIList.clear(); this.URIList.addAll(URIs);}
    public void setLabelList(List<String> Labels){this.LabelList.clear(); this.LabelList.addAll(Labels);}
    public void setEnergyScore(List<Double> EnergyScore) {this.EnergyScore.clear(); this.EnergyScore.addAll(EnergyScore);}
    
    public List<String> getURIList(){return this.URIList;}
    public List<String> getLabelList(){return this.LabelList;}
    public List<Double> getEnergyScore(){ return this.EnergyScore; }
    
}
