import org.primefaces.context.RequestContext;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@ManagedBean(name = "videoBean")
@SessionScoped
public class VideoBean {

    private static final String PRIVATE_CHANNEL = "/private/";
    private static final String NOTIFICATION_CHANNEL = "/notifications/";

    private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();

    private UsersOnline usersOnline;

    private String username;
    private String privateUser;
    private String privateMsg;

    public void setPrivateMsg(String privateMsg) {
        this.privateMsg = privateMsg;
    }
    public String getPrivateMsg() {
        return privateMsg;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPrivateUser(String privateUser) {
        this.privateUser = privateUser;
    }
    public String getUsername() {
        return username;
    }
    public String getPrivateUser() {
        return privateUser;
    }

    public VideoBean() {
        this.username = FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().
                get("login").toString();
        usersOnline = UsersOnline.getInstance();
    }

    public void sendPrivate() {
        pushContext.push(PRIVATE_CHANNEL + privateUser, "[PM] " + username + " : " + privateMsg);
        privateMsg = null;
    }

    public String openVideo() {
        String returnStr = "";
        if(usersOnline.getUsersPrivate().contains(username)) {
            FacesContext.getCurrentInstance().
                    addMessage("user in private",
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "You already use video ! \n /video.xhtml", null));
        } else {
            usersOnline.getUsersPrivate().add(username);
            returnStr = "goVideo";
        }
        return returnStr;
    }

    // delete user params "privateUser" and "video" from session
    // delete user from list of private users
    public String disconnect() {
        FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().
                remove("privateUser");
        pushContext.push(PRIVATE_CHANNEL + privateUser, username + " has left the chat :(");
        usersOnline.getUsersPrivate().remove(username);
        privateUser = null;
        return "disconnectVideo";
    }

    // connect to private socket
    public void connectSocket() {
        if(privateUser == null || privateUser == "") {
            privateUser = username;
        }
        FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().
                put("privateUser", privateUser);

        RequestContext.getCurrentInstance().execute("PF('private_socket').connect('/" + privateUser + "')");
    }

    // not used
    public void confirmNotification() {
        FacesContext.getCurrentInstance().
                addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "User confirm your invitation", null));
    }

    // not used
    public void cancelNotification() {
        FacesContext.getCurrentInstance().
                addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "User reject your invitation", null));
    }

}
