import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=username;
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
 
    public void sendMessage(){//send message to client handler
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend=scanner.nextLine();
                bufferedWriter.write(username+": "+messageToSend);
                bufferedWriter.newLine();//need to do newline and flush everytime
                bufferedWriter.flush();//when needing to send msg
            }

        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void listenForMessage(){//need new thread which using runnable
        new Thread(new Runnable() {
            @Override
            public void run(){//this does the running in the other thread thingy
                String msgFromGroupChat;

                while(socket.isConnected()){//as long as connected, read from the server
                    try{
                        msgFromGroupChat=bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();//object created and then start called
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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
    }//basically reset stuff, turning things(client) off

    public static void main(String[] args) throws IOException{
        Scanner scanner=new Scanner(System.in);//system.in means taking input from keyboard
        System.out.println("Enter username: ");
        String username=scanner.nextLine();//dont need to flush because not bufferedwriter
        Socket socket=new Socket("localhost", 8080);//idk how this work, i just choose one random port in my pc
        Client client=new Client(socket, username);//create client
        client.listenForMessage();//needs separate thread because either way it waits for "while" to get done
        client.sendMessage();
    }
}