import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

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

    private void informLeave(ClientHandler handler) throws IOException {
        for (ClientHandler mc : ChatServer.clients) {
            if (!mc.name.equals(handler.name)) {    // 로그아웃하는 클라이언트가 아니면
                mc.dos.writeUTF(handler.name + " is just leaved.");
            }
        }
    }

    @Override
    public void run() {
        // 클라이언트와의 송수신 처리
        while (true) {
            try {
                String msg = dis.readUTF(); // 블록 메서드임
                System.out.println("Received message" + msg);
                if (msg.equals("logout")) {
                    this.s.close(); // 접속을 종료하는 클라이언트의 서버 쪽 소켓을 닫음
                    informLeave(this);
                    break;
                }
                if (!msg.contains("#")) {
                    dos.writeUTF("Your message format is wrong.");
                    continue;
                }
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
