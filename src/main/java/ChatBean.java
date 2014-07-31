import org.primefaces.context.RequestContext;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean(name = "chatBean")
@SessionScoped
public class ChatBean implements Serializable {

    private static final String PUBLIC_CHANNEL = "/room/";
    private static final String NOTIFICATION_CHANNEL = "/notifications/";

    private UsersOnline usersOnline;

    private String globalMsg;
    private String username;
    private String privateUser;

    private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();

    public void setGlobalMsg(String globalMsg) {
        this.globalMsg = globalMsg;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPrivateUser(String privateUser) {
        this.privateUser = privateUser;
    }
    public String getGlobalMsg() {
        return globalMsg;
    }
    public String getUsername() {
        return username;
    }
    public String getPrivateUser() {
        return privateUser;
    }

    public ChatBean() {
        this.username = FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().
                get("login").toString();
        pushContext.push(PUBLIC_CHANNEL + "*", "'" + username + "'" + " enter into the room.");
        usersOnline = UsersOnline.getInstance();
        updateUsers();
    }

    public void sendMsg() {
        pushContext.push(PUBLIC_CHANNEL + "*" , username + " : " + globalMsg);
        globalMsg = null;
    }

    private void updateUsers() {
        pushContext.push(NOTIFICATION_CHANNEL + "*", "update_ui");
    }

    public void connectSocket() {
        RequestContext.getCurrentInstance().execute("PF('public_socket').connect('/" + username + "')");
        RequestContext.getCurrentInstance().execute("PF('notfocation_socket').connect('/" + username + "')");
    }

    private void openInviteDialog() {
        RequestContext.getCurrentInstance().execute("PF('videoDialog').show()");
    }

    public void invitePrivate() {
        FacesMessage msg = null;
        if (usersOnline.getUsersPrivate().contains(privateUser)) {
            FacesContext.getCurrentInstance().
                    addMessage("buisy user",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "The " + privateUser + " is busy :(", ""));
        } else {
            if (username.equals(privateUser)) {
                FacesContext.getCurrentInstance().
                        addMessage("call yourself",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "You try to call yourself", ""));
            } else {
                FacesContext.getCurrentInstance().
                        addMessage("call yourself",
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Test", ""));
                openInviteDialog();
                pushContext.push(NOTIFICATION_CHANNEL + privateUser, "open_dialog");
            }
        }
    }

// have to be used when the user leaves the chat
//    public void disconnect() {
//        UsersOnline usersOnline = UsersOnline.getInstance();
//        usersOnline.removeUserOnline(username);
//        updateUsers();
//
//        FacesContext.getCurrentInstance().
//                getExternalContext().getSessionMap().
//                remove("login");
//
//        pushContext.push(PUBLIC_CHANNEL + "*", username + " left the channel.");
//    }
}

// добавить данных например id юзера(json)
// сохранять историю сообщений
// изучить bootStrap и jQuery