import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 연결 처리
            // 포트 번호 할당
            ServerSocket ss = new ServerSocket(8200);
            System.out.println("EchoServer is started and waiting for client.");
            while (true) {
                // 클라이언트의 연결 요청을 무한히 기다림, 연결이 되면 클라이언트의 정보를 return함.
                Socket client = ss.accept();
                System.out.println("Client is connected: " + client);

                // 환영메시지 보내기
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                out.println("Welcome. this is a EchoServer."); // 송신 버퍼에 보관되어 있음
                out.flush(); // 원래 보낼 데이터를 모았다가 버퍼에서 보내지만, flush를 통해 송신 버퍼를 강제로 비움(전송 시작)
                System.out.println("message is sent.");
                while (true) {
                    String msg = in.readLine(); // 클라이언트로 부터 메시지 수신
                    System.out.println("From client: " + msg);
                    if (msg == null)    // 클라이언트 소켓 종료했을 때
                        break;

                    System.out.print("Your Message: ");
                    msg = scanner.nextLine();
                    out.println(msg);
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}