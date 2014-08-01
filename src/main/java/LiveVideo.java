import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@ServerEndpoint(value = "/livevideo/{username}")
public class LiveVideo {

    private static final Map<Session, String> sessionsWithUser = new HashMap<Session, String>();

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        System.out.println("Open video");
        session.setMaxBinaryMessageBufferSize(1024 * 512);        
    }

    @OnMessage
    public void onMessage(byte[] imageData, @PathParam("username") String userName, Session session) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(imageData);

            if (!sessionsWithUser.containsKey(session)) {
                sessionsWithUser.put(session, userName);
            }

            String sessionId = session.getId();

            for (Map.Entry<Session, String> entry : sessionsWithUser.entrySet()) {
                Session otherSession = entry.getKey();
                String otherUserName = entry.getValue();
                if (otherUserName.equals(userName) && !otherSession.getId().equals(sessionId)) {
                    otherSession.getBasicRemote().sendBinary(buf);
                    break;
                }
            }
        } catch (Throwable ioe) {
            System.out.println("Error sending message " + ioe.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close video");
        sessionsWithUser.remove(session);
    }

}
