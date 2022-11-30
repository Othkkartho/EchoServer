import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    public static void main(String[] args) {
        try {
            // Selector 객체 생성
            Selector selector = Selector.open();
            ServerSocketChannel sschannel = ServerSocketChannel.open();

            sschannel.bind(new InetSocketAddress(5454));
            sschannel.configureBlocking(false);  // Non-blocking 모드로 설정해야 Selector가 동작 가능함.

            //  서버 소캣 채널을 Selector에 등록
            sschannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(64);
            System.out.println("Echo Server is ready.");

            while (true) {
                selector.select();  // Selection 키의 활성화된 채널들을 선택. Blocking 모드로 동작함.
                Set<SelectionKey> keys = selector.selectedKeys();   // 동작할 키(채널)
                Iterator<SelectionKey> iterator = keys.iterator();  // 순환자. 원소들을 탐색하는 포인터와 같은 역할

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {   // 클라이언트가 접속 요청을 하는 상황
                        SocketChannel client = sschannel.accept();
                        System.out.println("Connect client: " + client.getRemoteAddress());
                        client.configureBlocking(false);    // 등록전 반드시 실행되야 함
                        client.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) { // 데이터 수신
                        SocketChannel client = (SocketChannel) key.channel();   // 데이터를 수신해야 하는 채널이 됨
                        int bytes = client.read(buffer);    // 얼마만큼의 크기로 수신했는지 확인
                        String received = "";

                        for (int i = 0; i < bytes; i++) {
                            received += (char) buffer.get(i);
                        }

                        System.out.println("Received: " + received);

                        if (received.equals("quit")) {
                            client.close();
                            buffer.clear(); // 버퍼를 초기화 시켜야 다시 수신할 수 있음.
                            System.out.println("Closed Client");
                        }
                        else {
                            buffer.flip();
                            client.write(buffer);    // 데이터를 그대로 송신
                            buffer.clear();
                        }
                    }
                    iterator.remove();  // 이미 처리한 키는 삭제하지 않으면 다시 처리하면서 nullpointerexception 에러가 발생함
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}