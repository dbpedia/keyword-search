
package org.dbpedia.keywordsearch.controller;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.dbpedia.keywordsearch.importer.neo4j;
import org.dbpedia.keywordsearch.indexer.ESNode;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.dbpedia.keywordsearch.serverproperties.pathvariables;
import org.neo4j.graphdb.GraphDatabaseService;

public class server {
    
    private ServerSocket serverSocket;
    private int port;
    private pathvariables Instance;
    
    private File[] rdffileiterator(){
        File folder = new File(this.Instance.getrdf());
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }
    private String graphpath(){
        
        return this.Instance.getgraph();
    }
    
    public server(int port) {
        this.port = port;
        this.Instance=new pathvariables();
    }
    
    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);

        Socket client = null;
        System.out.println("Starting ES cluster");
        IndexerInterface esnode = new ESNode();
        esnode.StartCluster("DBpediacluster");
       
//        long startClusterTime = System.currentTimeMillis();
//        /*Indexing of classes*/
//        esnode.lemoncluster("/home/KartikSinghal/home/IndexingData/dbpedia_3Eng_class.ttl","classes");
//        
//        /*Indexing of Properties*/
//        esnode.lemoncluster("/home/KartikSinghal/home/IndexingData/dbpedia_3Eng_property.ttl","properties");
//        
//        /*Enriching them with surfaceforms*/
//        esnode.rdfcluster("/home/KartikSinghal/home/IndexingData/en_surface_forms.ttl", "surfaceforms");
//        
//        /*Indexing DBpedia labels*/
//        esnode.rdfcluster("/home/KartikSinghal/home/IndexingData/labels_en.ttl", "dbpedialabels");
//        
//        esnode.datatypeindex("/home/KartikSinghal/home/IndexingData/datatypes", "datatypes");
//        long endClustertime = System.currentTimeMillis();
//        
//        System.out.println("This is a flag1");
//        System.out.println("Time Taken to Index : "+ String.valueOf(endClustertime-startClusterTime));
//        System.out.println("This is a flag2");
//        System.out.flush();
        
        System.out.println("Creating DataBase");
        long startGDBTime = System.currentTimeMillis();
        neo4j graphdb = new neo4j(this.Instance.getgraph());
        GraphDatabaseService gdb=graphdb.getgdbservice();
        for (File file : rdffileiterator()) { 
          if (file.isFile()) /*extracting all the files in the specified folder*/ {
               graphdb.graphdbform(gdb, this.Instance.getrdf()+'/'+file.getName());
           }
        }
        long endGDBTime = System.currentTimeMillis();
        System.out.println("Time Taken to Create GraphDataBase : "+ String.valueOf(endGDBTime-startGDBTime));
        while(true){
        	System.out.println("Waiting for clients...");
        	client = serverSocket.accept();
        	System.out.println("The following client has connected:"+client.getInetAddress().getCanonicalHostName());
                Thread thread = new Thread(new SocketClientHandler(client,esnode,gdb));
                thread.start();
        }
        
    }
    
}
