package org.dbpedia.keywordsearch.indexer;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import org.dbpedia.keywordsearch.importer.jena;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import org.elasticsearch.search.SearchHit;

public class ESNode implements IndexerInterface{

    private Node node;
    private  Client client;

    @Override
    public void StartCluster(String clustername){
        /* Initialization of cluster*/
        this.node=nodeBuilder().clusterName(clustername).node();
        /* Starting the central server */
        this.client=this.node.client();
    }
    
    
    @Override
    public void lemoncluster(String labelspath, String path) throws FileNotFoundException, IOException{
         /* Data cleaning and preprocessing*/
        BufferedReader in = new BufferedReader(new FileReader(labelspath));
        String line="";
        StringTokenizer st=null;
        int i=0;
        System.out.println("Building the "+path+" data...........");
        while((line=in.readLine())!=null){
           i++;
           st=new StringTokenizer(line," ");
           String token1=st.nextToken();
           st.nextToken();
           String token2=st.nextToken();
           
           /* Indexing the data in the central server*/
           IndexResponse response = this.client.prepareIndex(path, "mappings", String.valueOf(i))
            .setSource(jsonBuilder()
                        .startObject()
                            .field("uri", token1)
                            .field("label", token2)
                        .endObject()
                      )
            .execute()
            .actionGet();
        }    
        System.out.println("Data Entry completed");
    }
    
    @Override
    public void rdfcluster(String labelspath,String path) throws FileNotFoundException, IOException{
        /* Reading the rdf data with jena */
        StmtIterator iter=jena.jena(labelspath);
        int i=0;
        System.out.println("Building the "+path+" data...........");
        while(iter.hasNext()){
           Statement surfaceform=iter.next();
           i++;
           Resource s= surfaceform.getSubject();
           RDFNode o=surfaceform.getObject();
           
           /* Indexing the data in the central server */
           IndexResponse response = this.client.prepareIndex(path, "mappings", String.valueOf(i))
            .setSource(jsonBuilder()
                        .startObject()
                            .field("uri", s.toString())
                            .field("label", o.toString())
                        .endObject()
                      )
            .execute()
            .actionGet();
        }    
        System.out.println("Data Entry completed");
    }
    
    public void datatypeindex(String filepath,String indexname) throws FileNotFoundException, IOException{
        int i=0;
        System.out.println("Building the "+indexname+" data...........");
        BufferedReader in=new BufferedReader(new FileReader(filepath));
        String line;
        String value="";
        String standardunitdatatype;
        String factorunitdatatype;
        while(!((line=in.readLine())==null)){
            if(line.contains("$")){
                
                line=in.readLine();
                if(line.contains("#")){
                    StringTokenizer standardst = new StringTokenizer(line,"#");
                    if(!standardst.hasMoreTokens()){ System.err.print("Invalid standarddataunit format: err");}
                    standardunitdatatype=standardst.nextToken();
                    while(!("".equals((line=in.readLine())))){
                        if(line.contains("//")||line.contains("missing conversion factor"))
                            continue;
                        if(line.equals(""))
                            break;
                        StringTokenizer factorst = new StringTokenizer(line,"-");
                        factorunitdatatype=factorst.nextToken();
                        String labels=factorst.nextToken();
                        String[] labelarr = labels.split(", ");
                        value="";
                        while(factorst.hasMoreTokens())
                        value=value+factorst.nextToken();
                        for(String label:labelarr){
                            i=i+1;
                            IndexResponse response = this.client.prepareIndex(indexname, "mappings", String.valueOf(i))
                             .setSource(jsonBuilder()
                                         .startObject()
                                             .field("standardunit", standardunitdatatype)
                                             .field("factorunit", factorunitdatatype)
                                             .field("label",label)
                                             .field("value",value)
                                         .endObject()
                                       )
                             .execute()
                             .actionGet();   
                        }
                    }
                }else{System.err.print("Invalid standardunit format format in datatype file: Standardunit not present");}
            } 
            else{
                System.err.print("Invalid dimension format in datatype file");
            }
        }
        System.out.println("Data Entry complete");
    }
    
    @Override
    public SearchHit[] transportclient(String query, String path){
        /* Connecting the remote client with the central cluster */
        Client clientremote = this.node.client();
        
        /* Building the Query */
        MatchQueryBuilder qb = QueryBuilders.matchQuery("label", query);
        SearchRequestBuilder srb = clientremote.prepareSearch(path).setTypes("mappings");
        SearchResponse retrieved = srb.setQuery(qb).execute().actionGet();
        
        /* Retrieving the results from the query */
        SearchHit[] results = retrieved.getHits().getHits();
        return results;
    }
}
