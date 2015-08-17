package org.dbpedia.keywordsearch.serverproperties;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* This class sets the pathvariables for the graph database and rdf data*/

/* The paths are set as defined in the server properties file in conf folder */
final public class pathvariables {
    
    private String rdffolder;
    private String graphfolder;
    public String getrdf() { return this.rdffolder; }
    public String getgraph() { return this.graphfolder; }
    private void setpaths() throws IOException{
        BufferedReader in = new BufferedReader(new FileReader("/home/enigmatus/NetBeansProjects/test/KeywordSearch/conf/keywordsearch.properties"));
        String line="";
        while((line=in.readLine())!=null){
            if(line.contains("#")||line.equals("")){
                continue;
            }
            if(line.contains("graph")){
                int i = line.indexOf("=");
                this.graphfolder=line.substring(i+1);
            }
            else if(line.contains("rdf")){
                int i = line.indexOf("=");
                this.rdffolder=line.substring(i+1);
            }
        }
    }
    public pathvariables() { 
        try{
            setpaths();
        }catch(IOException ex){
            System.out.println("Server Properties not properly configured"+ex);
        }
    }
    
}
