import javax.websocket.Session;
import java.util.*;

public class UsersOnline {

    private static final List<String> usersOnline = new ArrayList<String>();

    private static final List<String> usersPrivate = new ArrayList<String>();

    public void addUserPrivate(String user) {
        getUsersPrivate().add(user);
    }
    public void removeUserPrivate(String user) {
        getUsersPrivate().remove(user);
    }
    public List<String> getUsersPrivate() {
        return usersPrivate;
    }
    public void addUserOnline(String user) {
            getUsersOnline().add(user);
    }
    public void removeUserOnline(String user) {
        getUsersOnline().remove(user);
    }
    public List<String> getUsersOnline() {
        return usersOnline;
    }

    public static UsersOnline getInstance() {
        return UsersInstanceHolder.INSTANCE;
    }

    private UsersOnline() {
    }

    private static class UsersInstanceHolder {
        private static UsersOnline INSTANCE = new UsersOnline();
        private UsersInstanceHolder(){}
    }

}
