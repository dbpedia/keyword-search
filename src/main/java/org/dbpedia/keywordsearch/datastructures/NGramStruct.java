package org.dbpedia.keywordsearch.datastructures; 
import org.dbpedia.keywordsearch.ngramgenerator.*;

/* This class defines the datastructure for ngrams. The NGram Generator will return a list of this datastructure*/
public class NGramStruct {
    private int index;
    private int begin;
    private int end;
    private String label;

   // constructor
   public NGramStruct(int index, int begin,int end,String label) {
      this.index=index;
      this.begin=begin;
      this.end=end;
      this.label=label;
   }

       // getter
       public int getIndex() {return index;}
       public int getBegin() { return begin; }
       public int getEnd() { return end; }
       public String getLabel() { return label; }
       // setter
       public void setIndex(int index){this.index=index;}
       public void setLabel(String label) { this.label = label; }
       public void setEnd(int end) { this.end = end; }
       public void setBegin(int begin) { this.begin = begin; }
    }
