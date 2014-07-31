import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.Serializable;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private String login;
    private String password;

    @ManagedProperty(value = "#{userBean}")
    private UserBean userBean;

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public String login() {
        FacesMessage msg = null;
        User loginUser = isUserPresent(getLogin());
        String returnString = null;

        Object userInSession = FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().
                get("login");

        // if the user exists
        if (loginUser != null) {
            // if password of user is correct
            if (getPassword().equals(loginUser.getPassword())) {
                // if user is not already logged in. It can only be logged in one time (with both options),
                // or only in ine browser(only 1st option)
                // 1) not in session
                // 2) not in login list
                if (userInSession == null && !userBean.getUsersOnline().getUsersOnline().contains(login)) {
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome " + login, "");

                    // add user to List<String> of online users
                    userBean.getUsersOnline().addUserOnline(login);

                    // put username to session
                    FacesContext.getCurrentInstance().
                            getExternalContext().getSessionMap().
                            put("login", login);

                    // redirect user to "chat" page
                    returnString = "goEnter";
                } else {
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "You are already logged in", "go to /chat");
                }
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
            }
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "No such user!");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        return returnString;
    }

    private User isUserPresent(String login) {
        User returnUser = null;
        for (User usr : userBean.getUsers()) {
            if (usr.getLogin().equals(login)) {
                returnUser = usr;
                break;
            }
        }
        if (returnUser == null)
            return null;
        else
            return returnUser;
    }
}