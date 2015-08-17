
package org.dbpedia.keywordsearch.propagator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.dbpedia.keywordsearch.datastructures.BlacklistDataStruct;
import org.dbpedia.keywordsearch.datastructures.ListFunctions;
import org.dbpedia.keywordsearch.datastructures.ResultDataStruct;
import org.dbpedia.keywordsearch.propagator.interfaces.PropagatorInterface;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

/* This is the class for propagating over the activated nodes */
public final class propagator implements PropagatorInterface{
    
    /* Blacklist is used to keep track of the combination of nodes that already has been propagated */
    private List<BlacklistDataStruct> blacklist = new ArrayList<BlacklistDataStruct>();
    private List<String> blacklistnode = new ArrayList<String>();
    public void PropagateInit(GraphDatabaseService db, List<ResultDataStruct> results){
        boolean mainflag;
        boolean flag=false;
        while(true){
            mainflag=false;
            flag=false;
            
            /* Propagating over every pair of activated nodes */
            for(int i=0; i<results.size();i++){
                if(!this.blacklistnode.contains(results.get(i).getURI())){
                    flag=ActivateTypeNode(db,results.get(i),results);
                    this.blacklistnode.add(results.get(i).getURI());
                }
                  
                for(int j=0; j<results.size();j++){
                  if(i==j){
                    continue;
                  }
                  
                  if(!ListFunctions.isinBlackList(this.blacklist, results.get(i).getURI(), results.get(j).getURI()))
                  {
                      /* If the combination is not in blacklist, it activates the third node through fact node
                          and puts it in the list*/
                      flag=Activatefactnode(db,results.get(i),results.get(j),results);
                      
                      /* Insert the current pair in the blacklist */
                      BlacklistDataStruct combination = new BlacklistDataStruct(results.get(i).getURI(),results.get(j).getURI());
                      this.blacklist.add(combination);
                      
                  }
                  else continue;
                  if(flag==true){
                    mainflag=true;
                  }
                }
                
            }
            /* If all the combinations are in the blacklist and no new node is inserted, then break the loop */
            if(mainflag==false){
                break;
            }
        }
    }
    
    public boolean ActivateTypeNode(GraphDatabaseService db, ResultDataStruct URI1, List<ResultDataStruct> results){
      Node activatednode1=null;
      Node activatednode2=null;
      Node activatednewnode=null;
      boolean flag = false;
      boolean run = true;
      ResourceIterator<Node> nodeindex1;
      ResourceIterator<Node> nodeindex2;
      
      /* Beginning the transaction for finding third node */
      try ( Transaction ignored = db.beginTx();
              /* Executing the cypher query to find node attached to given two nodes */
            Result result = db.execute( "match (a:`"+URI1.getURI()+"`)--c--(b:`"+"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"+"`) return c" ) )
            {
                Label activatedlabel1 = DynamicLabel.label(URI1.getURI());
                Label activatedlabel2 = DynamicLabel.label("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                
                nodeindex1 = db.findNodes(activatedlabel1);
                if(nodeindex1.hasNext())
                    activatednode1=nodeindex1.next();
                else
                    run = false;
                nodeindex2 = db.findNodes(activatedlabel2);
                if(nodeindex2.hasNext())
                    activatednode2=nodeindex2.next();
                else
                    run = false;
                /* Iterating over all the nodes in the results from the cypher query */
                if(run==true){
                    Iterator<Node> n_column = result.columnAs( "c" );
                    for ( Node node : IteratorUtil.asIterable( n_column ) )
                    {
                         /* finding the third node connected to two nodes through fact node */
                         activatednewnode=thirdnode(node,activatednode1,activatednode2);

                         flag = true;

                         /* Finding the union of the color set of the two nodes */
                         Set<Integer> union=URI1.getColors();
                         ResultDataStruct newnode = null;
                         ResultDataStruct temp =null;

                         /* Checking if the new node is already activated before, if yes then the scores are incremented 
                            or else a new node is created and added to the list */
                         if(!((temp=ListFunctions.ContainsResultDataStruct(activatednewnode.getProperty("URI").toString(),results))==null))
                         {
                                union=ResultDataStruct.union(union, temp.getColors());
                                temp.setExplainationScore(Double.valueOf(union.size())+5.00);
                                temp.setColors(union);
                                temp.setEnergyScore(temp.getEnergyScore()+URI1.getEnergyScore());
                         }
                         else{
                                newnode = new ResultDataStruct( activatednewnode.getProperty("URI").toString(),
                                                                (double) union.size()+5.00,
                                                                URI1.getEnergyScore(),
                                                                union); 
                                newnode.setActivation("Type");
                                results.add(newnode);
                         }
                    }/* Commiting the transaction */
                }
                ignored.success();
            }
            return flag;
    }
    
    public boolean Activatefactnode(GraphDatabaseService db, ResultDataStruct URI1, ResultDataStruct URI2, List<ResultDataStruct> results){
      Node activatednode1=null;
      Node activatednode2=null;
      Node activatednewnode=null;
      boolean flag = false;
      boolean run = true;
      ResourceIterator<Node> nodeindex1;
      ResourceIterator<Node> nodeindex2;
      
      /* Beginning the transaction for finding third node */
      try ( Transaction ignored = db.beginTx();
              /* Executing the cypher query to find node attached to given two nodes */
            Result result = db.execute( "match (a:`"+URI1.getURI()+"`)--c--(b:`"+URI2.getURI()+"`) return c" ) )
            {
                Label activatedlabel1 = DynamicLabel.label(URI1.getURI());
                Label activatedlabel2 = DynamicLabel.label(URI2.getURI());
                
                nodeindex1 = db.findNodes(activatedlabel1);
                if(nodeindex1.hasNext())
                    activatednode1=nodeindex1.next();
                else
                    run = false;
                nodeindex2 = db.findNodes(activatedlabel2);
                if(nodeindex2.hasNext())
                    activatednode2=nodeindex2.next();
                else
                    run = false;
                
                /* Iterating over all the nodes in the results from the cypher query */
                if(run==true){
                    Iterator<Node> n_column = result.columnAs( "c" );
                    for ( Node node : IteratorUtil.asIterable( n_column ) )
                    {
                         if(URI1.isSubClassActivated()||URI2.isSubClassActivated()){
                             if(URI1.isSubClassActivated()){
                                 Set<Integer> union=ResultDataStruct.union(URI1.getColors(), URI2.getColors());
                                 URI1.setColors(union);
                                 URI1.setEnergyScore(URI1.getEnergyScore()+URI2.getEnergyScore());
                                 URI1.setEnergyScore((double)union.size());
                             }
                             else{
                                 Set<Integer> union=ResultDataStruct.union(URI1.getColors(), URI2.getColors());
                                 URI2.setColors(union);
                                 URI2.setEnergyScore(URI1.getEnergyScore()+URI2.getEnergyScore());
                                 URI2.setEnergyScore((double)union.size());
                             }
                             break;
                         }

                         /* finding the third node connected to two nodes through fact node */
                         activatednewnode=thirdnode(node,activatednode1,activatednode2);

                         /* This is to prevent recursion of activating the same triple again and again.
                            This checks if the new activated node is part of previously activated tiple */
                         if(ListFunctions.isinBlackList(this.blacklist, activatednewnode.getProperty("URI").toString(), activatednode1.getProperty("URI").toString())
                           || ListFunctions.isinBlackList(this.blacklist, activatednewnode.getProperty("URI").toString(), activatednode2.getProperty("URI").toString())){
                             continue;
                         }


                         flag = true;

                         /* Finding the union of the color set of the two nodes */
                         Set<Integer> union=ResultDataStruct.union(URI1.getColors(), URI2.getColors());
                         ResultDataStruct newnode = null;
                         ResultDataStruct temp =null;

                         /* Checking if the new node is already activated before, if yes then the scores are incremented 
                            or else a new node is created and added to the list */
                         if(!((temp=ListFunctions.ContainsResultDataStruct(activatednewnode.getProperty("URI").toString(),results))==null))
                         {
                                union=ResultDataStruct.union(union, temp.getColors());
                                temp.setExplainationScore(Double.valueOf(union.size()));
                                temp.setColors(union);
                                temp.setEnergyScore(temp.getEnergyScore()+URI1.getEnergyScore()+URI2.getEnergyScore());
                         }
                         else{
                                newnode = new ResultDataStruct( activatednewnode.getProperty("URI").toString(),
                                                                (double) union.size(),
                                                                URI1.getEnergyScore()+URI2.getEnergyScore(),
                                                                union);
                                ImageLink(db, newnode);
                                newnode.setActivation("Factual");
                                results.add(newnode);
                         }
                    }/* Commiting the transaction */
                }    
                ignored.success();
            }
            return flag;
    }
    
    private void ImageLink(GraphDatabaseService db, ResultDataStruct URI1){
      Node activatednode1=null;
      Node activatednode2=null;
      Node activatednewnode=null;
      boolean flag = false;
      boolean run = true;
      ResourceIterator<Node> nodeindex1;
      ResourceIterator<Node> nodeindex2;
      
      /* Beginning the transaction for finding third node */
      try ( Transaction ignored = db.beginTx();
              /* Executing the cypher query to find node attached to given two nodes */
            Result result = db.execute( "match (a:`"+URI1.getURI()+"`)--c--(b:`"+"http://xmlns.com/foaf/spec/#term_depiction"+"`) return c" ) )
            {
                Label activatedlabel1 = DynamicLabel.label(URI1.getURI());
                Label activatedlabel2 = DynamicLabel.label("http://xmlns.com/foaf/spec/#term_depiction");
                
                nodeindex1 = db.findNodes(activatedlabel1);
                if(nodeindex1.hasNext())
                    activatednode1=nodeindex1.next();
                else
                    run = false;
                nodeindex2 = db.findNodes(activatedlabel2);
                if(nodeindex2.hasNext())
                    activatednode2=nodeindex2.next();
                else
                    run = false;
                if(run==false){
                    URI1.setImage("No image");
                }
                /* Iterating over all the nodes in the results from the cypher query */
                if(run==true){
                    Iterator<Node> n_column = result.columnAs( "c" );
                    for ( Node node : IteratorUtil.asIterable( n_column ) )
                    {
                         /* finding the third node connected to two nodes through fact node */
                         activatednewnode=thirdnode(node,activatednode1,activatednode2);

                         flag = true;
                         System.out.print("skdajkl");
                         URI1.setImage(activatednewnode.getProperty("URI").toString());
                         
                         
                    }/* Commiting the transaction */
                }
                if(flag == false){
                    URI1.setImage("No image");
                }
                ignored.success();
            }
    }
    
    private enum Reltypes implements RelationshipType{
	Predicate_of,Subject_of,Object_of;
    }
    
    /* This method finds the third node that is connected with the two given nodes through fact node */
    private static Node thirdnode(Node fact, Node node1, Node node2) {
    for(Relationship r :fact.getRelationships(Reltypes.Object_of)) {
       if (!(r.getOtherNode(fact).equals(node1) || r.getOtherNode(fact).equals(node2)))
       return r.getOtherNode(fact);
    }
    for(Relationship r :fact.getRelationships(Reltypes.Subject_of)) {
       if (!(r.getOtherNode(fact).equals(node1) || r.getOtherNode(fact).equals(node2)))
       return r.getOtherNode(fact);
    }
    for(Relationship r :fact.getRelationships(Reltypes.Predicate_of)) {
       if (!(r.getOtherNode(fact).equals(node1) || r.getOtherNode(fact).equals(node2)))
       return r.getOtherNode(fact);
    }
    return null;
    }
    
}
