import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class ClientHandler implements Runnable {
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;
    String name;    // 클라이언트 이름

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String name) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.name = name;
    }

    @Override
    public void run() {
        // 클라이언트와의 송수신 처리
        while (true) {
            try {
                String msg = dis.readUTF();
                System.out.println("Received message" + msg);
                StringTokenizer tokenizer = new StringTokenizer(msg, "#");
                String who = tokenizer.nextToken(); // 받는 이름
                String data = tokenizer.nextToken(); // 보내는 데이터
                data = name + " >> " + data;

                for (ClientHandler handler : ChatServer.clients) {
                    if (handler.name.equals(who)) {
                        handler.dos.writeUTF(data);
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
