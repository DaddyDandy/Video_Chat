import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@ServerEndpoint(value = "/liveaudio")
public class LiveAudio {

    private static final Map<Session, String> sessionsWithUser = new HashMap<Session, String>();

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {        
        sessionsWithUser.put(session, "audio");
        System.out.println("Open audio");
    }

    @OnMessage
    public void onMessage(byte[] audioData, Session session) {
        try {
            // Wrap a byte array into a buffer
            ByteBuffer buf = ByteBuffer.wrap(audioData);
            for (Map.Entry<Session, String> entry : sessionsWithUser.entrySet()) {
                entry.getKey().getBasicRemote().sendBinary(buf);
            }
            System.out.println("send audio .....................................................");
        } catch (Throwable ioe) {
            System.out.println("Error sending audio " + ioe.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionsWithUser.remove(session);
        System.out.println("Close audio");
    }

}
