import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by Денис on 16.07.2014.
 */
@ServerEndpoint("/notifications")
public class LiveNotifications {

    @OnMessage
    public String onMessage(String message) {
        return message;
    }

}
