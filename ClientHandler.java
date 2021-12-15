import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{//using runnable to be able to use 
                                                //Thread which then makes it run multiple thread
public static ArrayList<ClientHandler> clientHandlers=new ArrayList<>(); //arraylist of client to iterate
private Socket socket;                                                     //through it later on
private BufferedReader bufferedReader; //read msg from client
private BufferedWriter bufferedWriter; //write msg to client
private String clientUsername; //for client

//using buffer so its more efficient 
public ClientHandler(Socket socket){//accepting socket connection
    try{
        this.socket=socket;
        this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
        this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientUsername=bufferedReader.readLine();//waiting from client
        clientHandlers.add(this);
        broadcastMessage("Server: "+ clientUsername+" joined session");
    } catch(IOException e){
        closeEverything(socket, bufferedReader, bufferedWriter);
    }
}


@Override //a must because implemented
public void run(){//run on separate thread so other process wont wait(async)
    String messageFromClient;
    while(socket.isConnected()){//as long as connected, listen from msg
        try{
            messageFromClient=bufferedReader.readLine();
            broadcastMessage(messageFromClient);
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
            break; //need break because it loops indefinitely when connected
        }
    }
}

public void broadcastMessage(String messageToSend){
    for(ClientHandler clientHandler:clientHandlers){
        try{
            if(!clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();//enter key
                clientHandler.bufferedWriter.flush();//forcing the buffer to send when not full
            }
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } //for each clienthandler in list. clienhandler represent for each iteration
}

public void removeClientHandler(){//disconnect handling
    clientHandlers.remove(this);
    broadcastMessage("Server: "+clientUsername+" has left session");
}


public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
    removeClientHandler();
    try{
        if(bufferedReader!=null){
            bufferedReader.close();
        }
        if(bufferedWriter!=null){
            bufferedWriter.close();
        }
        if(socket!=null){
            socket.close();
        }
    }
    catch (IOException e){
        e.printStackTrace();
    }
}//to disconnect and reset state


}
    
