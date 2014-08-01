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
            System.out.println("send audio in Bean");            
        } catch (Throwable ioe) {
            System.out.println("Error sending audio " + ioe.getMessage());
        }        
    }

    @OnMessage
    public void onMessage(String str, Session session) {
        try {
            System.out.println("send audio in Bean");
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
