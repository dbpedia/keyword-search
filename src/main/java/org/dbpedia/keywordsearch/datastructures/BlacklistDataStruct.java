
package org.dbpedia.keywordsearch.datastructures;

/* This datastructure is for the blacklisted URIs whose combinations, the propagator has already considered*/
public class BlacklistDataStruct {
   private String URI1;
   private String URI2;
   public BlacklistDataStruct(String URI1,String URI2){
       this.URI1=URI1;
       this.URI2=URI2;
   } 
   
   public String getfirstURI(){return this.URI1;}
   public String getsecondURI(){return this.URI2;}
}
