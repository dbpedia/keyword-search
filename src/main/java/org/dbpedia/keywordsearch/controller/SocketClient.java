
package org.dbpedia.keywordsearch.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    private String hostname;
    private int port;
    Socket socketClient;
    private String Query;

    public SocketClient(String hostname, int port, String Query){
        this.hostname = hostname;
        this.port = port;
        this.Query=Query;
    }

    public void connect() throws UnknownHostException, IOException{
        System.out.println("Attempting to connect to "+hostname+":"+port);
        socketClient = new Socket(hostname,port);
        System.out.println("Connection Established");
    }

    public void readResponse() throws IOException{
       String userInput;
       BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

       System.out.print("RESPONSE FROM SERVER:");
       while ((userInput = stdIn.readLine()) != null) {
           System.out.println(userInput);
       }
    }
    
    public void runQuery() throws IOException{
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
       writer.write(this.Query);
       writer.newLine();
       writer.flush();
    }
    
}
