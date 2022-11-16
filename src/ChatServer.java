import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class ChatServer {
    public static Vector<ClientHandler> clients = new Vector<>();
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            ServerSocket ss = new ServerSocket(3005);

            while (true) {
                System.out.println("Server is waiting...");
                Socket s = ss.accept();
                System.out.println("Client is connected: " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                String name = dis.readUTF();
                System.out.println(name + ": Welcome to the server.");

                 // 클라이언트에게 메시지 전송
                /* System.out.print("Chat: ");
                String msg = scan.nextLine();
                dos.writeUTF(msg); */

                ClientHandler handler = new ClientHandler(s, dis, dos, name);
                Thread thread = new Thread(handler); // 스레드 생성
                System.out.println("Adding this client to client vector");
                clients.add(handler); // 백터로 그 클라이언트 저장
                thread.start(); // 스레드 실행
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}