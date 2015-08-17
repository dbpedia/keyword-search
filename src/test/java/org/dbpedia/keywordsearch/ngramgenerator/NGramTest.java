package org.dbpedia.keywordsearch.ngramgenerator;
import java.util.ArrayList;
import java.util.List;
import org.dbpedia.keywordsearch.datastructures.NGramStruct;
import org.dbpedia.keywordsearch.ngramgenerator.interfaces.NGramInterface;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class NGramTest {
    
    @Test
    public void testNgramgenerator1(){
        List<NGramStruct> ngrams= new ArrayList<NGramStruct>();
        System.out.println();
//        System.out.println("==========Test Case 1==========");
        String Query = "birthplace bill gates wife";
        System.out.println();
        System.out.println("Entered Query : "+Query);
        NGramStruct ngram1=new NGramStruct(0,0,3,"birthplace bill gates wife");
        NGramStruct ngram2=new NGramStruct(1,0,2,"birthplace bill gates");
        NGramStruct ngram3=new NGramStruct(2,1,3,"bill gates wife");
        NGramStruct ngram4=new NGramStruct(3,0,1,"birthplace bill");
        NGramStruct ngram5=new NGramStruct(4,1,2,"bill gates");
        NGramStruct ngram6=new NGramStruct(5,2,3,"gates wife");
        NGramStruct ngram7=new NGramStruct(6,0,0,"birthplace");
        NGramStruct ngram8=new NGramStruct(7,1,1,"bill");
        NGramStruct ngram9=new NGramStruct(8,2,2,"gates");
        NGramStruct ngram10=new NGramStruct(9,3,3,"wife");
        ngrams.add(ngram1);ngrams.add(ngram2);ngrams.add(ngram3);ngrams.add(ngram4);ngrams.add(ngram5);
        ngrams.add(ngram6);ngrams.add(ngram7);ngrams.add(ngram8);ngrams.add(ngram9);ngrams.add(ngram10);
        System.out.println("===========Result of NGram Generator=======");
        System.out.println();
        NGramInterface fetchedngrams = new NGramModel();
        fetchedngrams.CreateNGramModel(Query);
        for(int i=0;i<ngrams.size();i++){
            assertEquals(ngrams.get(i).getBegin(),fetchedngrams.getNGramMod().get(i).getBegin());
            assertEquals(ngrams.get(i).getEnd(),fetchedngrams.getNGramMod().get(i).getEnd());
            assertEquals(ngrams.get(i).getIndex(),fetchedngrams.getNGramMod().get(i).getIndex());
            assertEquals(ngrams.get(i).getLabel(),fetchedngrams.getNGramMod().get(i).getLabel());
            System.out.println("Index: " + fetchedngrams.getNGramMod().get(i).getIndex()+" NGram: "+ fetchedngrams.getNGramMod().get(i).getLabel());
        }
    }
    
    @Test
    public void testNgramgenerator2(){
        List<NGramStruct> ngrams= new ArrayList<NGramStruct>();
        System.out.println();
        System.out.println("==========Test Case 2==========");
        String Query = "Arnold Schwarzenegger Gender";
        System.out.println();
        System.out.println("Entered Query : "+Query);
        NGramStruct ngram1=new NGramStruct(0,0,2,"Arnold Schwarzenegger Gender");
        NGramStruct ngram2=new NGramStruct(1,0,1,"Arnold Schwarzenegger");
        NGramStruct ngram3=new NGramStruct(2,1,2,"Schwarzenegger Gender");
        NGramStruct ngram4=new NGramStruct(3,0,0,"Arnold");
        NGramStruct ngram5=new NGramStruct(4,1,1,"Schwarzenegger");
        NGramStruct ngram6=new NGramStruct(5,2,2,"Gender");
        ngrams.add(ngram1);ngrams.add(ngram2);ngrams.add(ngram3);ngrams.add(ngram4);ngrams.add(ngram5);
        ngrams.add(ngram6);
        System.out.println("===========Result of NGram Generator=======");
        System.out.println();
        NGramInterface fetchedngrams = new NGramModel();
        fetchedngrams.CreateNGramModel(Query);
        for(int i=0;i<ngrams.size();i++){
            assertEquals(ngrams.get(i).getBegin(),fetchedngrams.getNGramMod().get(i).getBegin());
            assertEquals(ngrams.get(i).getEnd(),fetchedngrams.getNGramMod().get(i).getEnd());
            assertEquals(ngrams.get(i).getIndex(),fetchedngrams.getNGramMod().get(i).getIndex());
            assertEquals(ngrams.get(i).getLabel(),fetchedngrams.getNGramMod().get(i).getLabel());
            System.out.println("Index: " + fetchedngrams.getNGramMod().get(i).getIndex()+" NGram: "+ fetchedngrams.getNGramMod().get(i).getLabel());
        }
    }
}
