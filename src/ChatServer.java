import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel sschannel = ServerSocketChannel.open();
            sschannel.configureBlocking(true);  // 만약 false로 동작시키면 클라이언트의 accept를 기다리지 않기 때문에 client가 없어 nullpointerexception을 던진다.
            sschannel.bind(new InetSocketAddress(5001));

            while (true) {
                System.out.println("Chatting server is ready.");
                SocketChannel client = sschannel.accept();
                sschannel.configureBlocking(true);  // default: true
                System.out.println("Connected client: " + client.getRemoteAddress());
                String msg;
                Scanner scn = new Scanner(System.in);

                while (true) {  // 송수신 반복 수행
                    System.out.print("> ");
                    msg = scn.nextLine();

                    HelperMethods.sendMessage(client, msg);

                    // 수신하기
                    msg = HelperMethods.receiveMessage(client);
                    System.out.println("Received: " + msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}