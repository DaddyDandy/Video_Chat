import javax.websocket.Session;

public class UserOnline {

    private String username;
    private Session session;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    public String getUsername() {

        return username;
    }
    public Session getSession() {
        return session;
    }

    public UserOnline(String username) {
        this.username = username;
    }

    public UserOnline(Session session) {
        this.session = session;
    }

    public UserOnline(String username, Session session) {
        this.username = username;
        this.session = session;
    }
}
