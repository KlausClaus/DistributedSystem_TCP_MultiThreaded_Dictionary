/**
 * Name: Renfei Yu (Klaus)
 * SID: 1394392
 * Login Username: reyu
 * Date: 2023/8/25
 */

/**
 * this is the thread control class for the purpose of multi-threaded implementation
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import javax.net.ServerSocketFactory;

/**
 * this class is used for handle the multi-threaded problem
 */
public class ClientHandler extends Thread{

    private Socket clientSocket;
    private ConcurrentHashMap<String, String> tempDictionary;
    private int clientCounter;

    public ClientHandler (Socket clientSocket, ConcurrentHashMap<String, String> serverDictionary, int counter){
        this.clientSocket=clientSocket;
        this.tempDictionary=serverDictionary;
        this.clientCounter=counter;
    }

    public void setDictionary( ConcurrentHashMap<String, String> serverDictionary){
        tempDictionary=serverDictionary;
    }

    @Override
    public void run(){
        try{
            // Input stream
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Output Stream
            PrintWriter output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
           
        
            //get the client message and apply for the dictionary.
            String clientMsg = null;
            while((clientMsg = input.readLine()) != null){

                Server.editActionsPanel("CLIENT: " + clientCounter +" " +clientMsg +"\n");

                String[] message = clientMsg.split(":");
                String action = message[0];
                String text = message[1];
                String meaning = null;

                /**
                 * only the update and add will edit the meaning field 
                 */
                if(message.length==3){
                    meaning=message[2];
                }

                switch (action) {
                    case "QUE":
                        tempDictionary=Server.getDictionary();

                        String mMeaning = tempDictionary.get(text);
                        if(mMeaning==null){
                            output.println("Error: This word does not exists");
                        }else{
                            output.println("The Word: "+text + " has the meaning of: " +mMeaning);
                        }
                        break;

                    case "ADD":
                        tempDictionary=Server.getDictionary();
               
                        if(tempDictionary.get(text) != null){
                     
                            output.println("Error: Already exists this word");
           
                        }else{
       
                            Server.updateWord(text,meaning);
                            output.println("Add successfully!");


                        }
                        break;

                    case "DEL":
                        tempDictionary=Server.getDictionary();
               
                        if(tempDictionary.get(text) == null){
                            output.println("Error: This word does not exists");
                        }else{
                            Server.deleteWord(text);
                            output.println("Delete successfully!");
                        }
                        break;

                    case "UPD":
                        tempDictionary=Server.getDictionary();
                        if(tempDictionary.get(text) == null){
                            output.println("Error: This word does not exists");
           
                        }else{
       
                            Server.updateWord(text,meaning);
                            output.println("Update successfully!");

                        }
                        break;

                }

            }
            // close the client connection
            clientSocket.close();
            input.close();
            output.close();
        }catch(SocketException e){
            Server.editConnectionPanel("- Client "+clientCounter+" disconencted \n");

        }catch (IOException e) {
            Server.editConnectionPanel("- Client "+clientCounter+" disconencted \n");

        }
            
    }

}

