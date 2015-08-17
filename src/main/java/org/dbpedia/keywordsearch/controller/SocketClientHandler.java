
package org.dbpedia.keywordsearch.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import org.dbpedia.keywordsearch.Initializer.interfaces.InitializerInterface;
import org.dbpedia.keywordsearch.Initializer.initializer;
import org.dbpedia.keywordsearch.datastructures.ListFunctions;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
import org.dbpedia.keywordsearch.indexer.ESNode;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.dbpedia.keywordsearch.ngramgenerator.NGramModel;
import org.dbpedia.keywordsearch.ngramgenerator.interfaces.NGramInterface;
import org.dbpedia.keywordsearch.propagator.interfaces.PropagatorInterface;
import org.dbpedia.keywordsearch.propagator.propagator;
import org.dbpedia.keywordsearch.urimapper.Mapper;
import org.dbpedia.keywordsearch.urimapper.interfaces.MapperInterface;
import org.neo4j.graphdb.GraphDatabaseService;

public class SocketClientHandler implements Runnable {

  private Socket client;
  private  IndexerInterface esnode;
  GraphDatabaseService gdb;
  List<ResultDataStruct> actual;
  long ngramtime;
  long mappertime;
  long initializertime;
  long propagatortime;
  long totaltime;
  
  public SocketClientHandler(Socket client,IndexerInterface esnode, GraphDatabaseService gdb) {
	this.client = client;
        this.esnode=esnode;
        this.gdb=gdb;
  }

  @Override
  public void run() {
     try {
	System.out.println("Thread started with name:"+Thread.currentThread().getName());
	readResponse();
       } catch (IOException e) {
	 e.printStackTrace();
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
   }

   private void readResponse() throws IOException, InterruptedException {
	String userInput;
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
	userInput = stdIn.readLine();
        System.out.println(userInput);
        long startTotalTime = System.currentTimeMillis();
        NGramInterface fetchedngrams=new NGramModel();
        fetchedngrams.CreateNGramModel(userInput);
        long endTime = System.currentTimeMillis();
        this.ngramtime=endTime-startTotalTime;
        long startTime = System.currentTimeMillis();
        MapperInterface mappings = new Mapper();
        mappings.BuildMappings(this.esnode,fetchedngrams.getNGramMod());
        endTime = System.currentTimeMillis();
        this.mappertime=endTime-startTime;
        startTime = System.currentTimeMillis();
        InitializerInterface init = new initializer();
        init.initiate(mappings.getMappings(),fetchedngrams.getNGramMod());
        endTime = System.currentTimeMillis();
        this.initializertime=endTime-startTime;
        startTime = System.currentTimeMillis();
        PropagatorInterface getFinalResults = new propagator();
        getFinalResults.PropagateInit(gdb,init.getResultsList());
        endTime = System.currentTimeMillis();
        this.propagatortime=endTime-startTime;
        this.totaltime=endTime-startTotalTime;
        this.actual= init.getResultsList();
        
        /* Sorting the final nodes according to explanation score and energy score  */
        ListFunctions.sortresults(this.actual);
        System.out.println("Now send");
        sendResponse();
   }
	
    private void sendResponse() throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.write(" Retrieving results in decreasing order of relevancy.... ");
        writer.newLine();
        for(int i=actual.size()-1;i>=0;i--){
            writer.write(" URI : "+actual.get(i).getURI()+" {Explanation score : "+actual.get(i).getExplainationScore()+", Energy Score : " + actual.get(i).getEnergyScore()+", Colors : "+actual.get(i).getColors()+"}");
            writer.newLine();
        }
        writer.write("Total Time : "+String.valueOf(this.totaltime));
        writer.write("NGram Generation Time : "+String.valueOf(this.ngramtime));
        writer.write("Mapping Time : "+String.valueOf(this.mappertime));
        writer.write("Initializer Time : "+String.valueOf(this.initializertime));
        writer.write("Propagator Time : "+String.valueOf(this.propagatortime));
        
        System.out.println("Send successful");
	writer.flush();
        writer.close();
        client.close();
    }

}