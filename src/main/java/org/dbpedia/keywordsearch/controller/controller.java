
package org.dbpedia.keywordsearch.controller;

import java.io.IOException;

public class controller {
    public static void main(String args[]) throws IOException{
        
        if(args[0].equals("Start"))
        {
            server socketserver = new server(60001);
            socketserver.start();
        }
        else if(args[0].equals("Run")){
            String Query="";
            for(int i=1;i<args.length;i++){
                Query=Query+" "+args[i];
            }
            SocketClient client = new SocketClient("localhost",9991,Query);
            client.connect();
            client.runQuery();
            client.readResponse();
        }
        
    }
}
