import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
private ServerSocket serverSocket;
public Server(ServerSocket serverSocket){
this.serverSocket=serverSocket;
}
public void startServer(){ //to make connection to client via socket
try{
    while(!serverSocket.isClosed()){
        Socket socket= serverSocket.accept();
        System.out.println("a client is connected");
        ClientHandler clientHandler= new ClientHandler(socket); //handling client
        
        Thread thread=new Thread(clientHandler); //running client on multiple thread
        thread.start();//starting the thread
    }
}catch (IOException e){
e.printStackTrace();
}
}
public void closeServerSocket(){
    try{
        if(serverSocket!=null){
            serverSocket.close();//to close the connection
        }
    }
    catch(IOException e){
        e.printStackTrace();
    }
}
public static void main(String[] args) throws IOException {//using console
    ServerSocket serverSocket=new ServerSocket(8080);
    Server server=new Server(serverSocket);
    server.startServer();//to start the server when compiled
}
}