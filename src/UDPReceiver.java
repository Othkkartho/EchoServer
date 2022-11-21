import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9005);
            System.out.println("UDP Receiver is ready.");

            while (true) {
                byte[] buffer = new byte[1024]; // 한 번에 받을 수 있는 데이터의 크기
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // 소캣으로 들어온 데이터를 받아서 패킷의 버퍼에 넣어줌
                String msg = new String(packet.getData()); // byte 배열을 String 으로 바꿈
                System.out.println("From: " + packet.getAddress() + ", port: " + packet.getPort());
                System.out.println("   Message: " + msg.trim() + ", Length: " + packet.getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}