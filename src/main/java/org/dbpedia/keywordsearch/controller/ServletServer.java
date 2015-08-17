/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbpedia.keywordsearch.controller;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dbpedia.keywordsearch.Initializer.initializer;
import org.dbpedia.keywordsearch.Initializer.interfaces.InitializerInterface;
import org.dbpedia.keywordsearch.datastructures.ListFunctions;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
import org.dbpedia.keywordsearch.importer.neo4j;
import org.dbpedia.keywordsearch.indexer.ESNode;
import org.dbpedia.keywordsearch.indexer.Interface.IndexerInterface;
import org.dbpedia.keywordsearch.ngramgenerator.NGramModel;
import org.dbpedia.keywordsearch.ngramgenerator.interfaces.NGramInterface;
import org.dbpedia.keywordsearch.propagator.interfaces.PropagatorInterface;
import org.dbpedia.keywordsearch.propagator.propagator;
import org.dbpedia.keywordsearch.serverproperties.pathvariables;
import org.dbpedia.keywordsearch.urimapper.Mapper;
import org.dbpedia.keywordsearch.urimapper.interfaces.MapperInterface;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author enigmatus
 */
public class ServletServer extends HttpServlet {

    IndexerInterface esnode;
    private pathvariables Instance;
    neo4j graphdb;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        NGramInterface ngram = new NGramModel();
        ngram.CreateNGramModel(request.getParameter("Query").replace("whose", "").replace("is", ""));
        MapperInterface mappings = new Mapper();
        mappings.BuildMappings(this.esnode,ngram.getNGramMod());
        InitializerInterface init = new initializer();
        init.initiate(mappings.getMappings(),ngram.getNGramMod());
        PropagatorInterface getFinalResults = new propagator();
        getFinalResults.PropagateInit(graphdb.getgdbservice(),init.getResultsList());
        ListFunctions.sortresults(init.getResultsList());
        PrintWriter pw = response.getWriter();//get the stream to write the data
        Map map = new HashMap();
        pw.write("[");
        int i;
        for(i=init.getResultsList().size()-1; i >=0; i--){
            ResultDataStruct rds=init.getResultsList().get(i);
            System.out.println(rds.getURI()+" : "+rds.getImage() + " : "+rds.getEnergyScore());
            map.put("URI", rds.getURI());
            map.put("ExpScore", rds.getExplainationScore());
            map.put("EngScore", rds.getEnergyScore());
            map.put("image", rds.getImage());
            pw.write(new Gson().toJson(map));
            map.clear();
            if(i>0)
                pw.write(",");
            
        }
        pw.write("]");
        System.out.print("Done");

    }

    private File[] rdffileiterator() {
        File folder = new File(this.Instance.getrdf());
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    private String graphpath() {

        return this.Instance.getgraph();
    }

    @Override
    public void init() throws ServletException {

        esnode = new ESNode();
        esnode.StartCluster("DBpedia cluster");
        long startClusterTime = System.currentTimeMillis();
        /*Indexing of classes*/
        try {
            esnode.lemoncluster("/home/enigmatus/NetBeansProjects/test/KeywordSearch/resources/dbpedia_3Eng_class.ttl", "classes");

            /*Indexing of Properties*/
            esnode.lemoncluster("/home/enigmatus/NetBeansProjects/test/KeywordSearch/resources/dbpedia_3Eng_property.ttl", "properties");

            /*Enriching them with surfaceforms*/
            esnode.rdfcluster("/home/enigmatus/NetBeansProjects/test/KeywordSearch/resources/en_surface_forms.ttl", "surfaceforms");

            /*Indexing DBpedia labels*/
            esnode.rdfcluster("/home/enigmatus/NetBeansProjects/test/KeywordSearch/resources/dbpedia_labels.ttl", "dbpedialabels");

            esnode.datatypeindex("/home/enigmatus/NetBeansProjects/test/KeywordSearch/resources/datatypes", "datatypes");
            long endClustertime = System.currentTimeMillis();
        } catch (IOException ex) {
            Logger.getLogger(ServletServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Creating DataBase");
        long startGDBTime = System.currentTimeMillis();
        this.Instance=new pathvariables();
        graphdb = new neo4j(this.Instance.getgraph());
        GraphDatabaseService gdb = graphdb.getgdbservice();
        for (File file : rdffileiterator()) {
            if (file.isFile()) /*extracting all the files in the specified folder*/ {
                graphdb.graphdbform(gdb, this.Instance.getrdf() + '/' + file.getName());
            }
        }
        System.out.print("finish");
        long endGDBTime = System.currentTimeMillis();
        System.out.println("Time Taken to Create GraphDataBase : " + String.valueOf(endGDBTime - startGDBTime));

    }
}
