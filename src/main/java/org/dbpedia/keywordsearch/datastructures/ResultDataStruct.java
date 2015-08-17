package org.dbpedia.keywordsearch.datastructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*This class defines the datastructure for results. The initializer and propagator will return a list of this datastructure*/
public class ResultDataStruct {
    private List<Double> scores=new ArrayList<Double>();
    private String URI; 
    private Set<Integer> colors=new HashSet<Integer>();
    private String URIimage;
    private String Activation;
    public ResultDataStruct(String URI,Double explaination_score, Double energy_score, int begin, int end){
        this.scores.add(explaination_score);
        this.scores.add(energy_score);
        this.URI=URI;
        for(int i=begin;i<=end;i++){
            this.colors.add(i);
        }
    }
    public ResultDataStruct(String URI,Double explaination_score, Double energy_score, Set<Integer> colors){
        this.scores.add(explaination_score);
        this.scores.add(energy_score);
        this.URI=URI;
        this.colors.addAll(colors);
    }
   
    public Double getExplainationScore() {return this.scores.get(0);}
    public Double getEnergyScore() {return this.scores.get(1);}
    public String getURI() {return this.URI;}
    public String getImage() {return this.URIimage;}
    public Set<Integer> getColors(){return this.colors;}
    
    public void setExplainationScore(Double explaination_score) {this.scores.set(0, explaination_score);}
    public void setEnergyScore(Double energy_score) {this.scores.set(1, energy_score);}
    public void setImage(String image) {this.URIimage=image;}
    public void setURI(String URI) {this.URI=URI;}
    public void setColors(Set<Integer> colors){this.colors.clear();this.colors.addAll(colors);}
    public void setActivation(String Method){this.Activation=Method;}
    
    public boolean isSubClassActivated(){
        if(this.Activation.equals("Type")){
            return true;
        }
        else{
            return false;
        }
    }
    
    public static Set<Integer> intersect (Set<Integer> Setcolors1, Set<Integer> Setcolors2){
        Set<Integer> secondcolors=new HashSet<Integer>();
        secondcolors.addAll(Setcolors1);
        Set<Integer> firstcolors=new HashSet<Integer>();
        firstcolors.addAll(Setcolors2);
        Set<Integer> intersect=new HashSet<Integer>();
        boolean flag=false;
        if(secondcolors.size()<firstcolors.size()){
            for(int i : secondcolors){
                if(firstcolors.contains(i)){
                    flag=true;
                    if(!intersect.contains(i))
                        intersect.add(i);
                }
            }
        }else{
            for(int i : firstcolors){
                if(secondcolors.contains(i)){
                    flag=true;
                    if(!intersect.contains(i))
                        intersect.add(i);
                }
            }
        }
        if(flag){return intersect;}
        else {return null;}
    }
    
    public static Set<Integer> union (Set<Integer> Setcolors1, Set<Integer> Setcolors2){
        Set<Integer> secondcolors=new HashSet<Integer>();
        secondcolors.addAll(Setcolors1);
        Set<Integer> firstcolors=new HashSet<Integer>();
        firstcolors.addAll(Setcolors2);
        Set<Integer> union=new HashSet<Integer>();
        union.addAll(firstcolors);
        for(int i : secondcolors){
            if(!firstcolors.contains(i)){
                union.add(i);
            }
        }
        return union;
    }
}
