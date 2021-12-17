import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain{
    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner=new Scanner(System.in);//system.in means taking input from keyboard
        System.out.println("Enter username: ");
        String username=scanner.nextLine();//dont need to flush because not bufferedwriter
        Socket socket=new Socket("localhost", 8080);//idk how this work, i just choose one random port in my pc
        Client client=new Client(socket, username);//create client
        client.listenForMessage();//needs separate thread because either way it waits for "while" to get done
        client.sendMessage();
    }
}