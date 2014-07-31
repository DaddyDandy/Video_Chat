import org.primefaces.event.FileUploadEvent;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable {

    private UsersOnline usersOnline;

    private List<User> users;
    private User currentUser = new User();

    private EntityManager em;

    public UsersOnline getUsersOnline() {
        return usersOnline;
    }
    public void setUsersOnline(UsersOnline usersOnline) {
        this.usersOnline = usersOnline;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public List<User> getUsers() {
        return users;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public UserBean() {
        users = new ArrayList<User>();
        usersOnline = UsersOnline.getInstance();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("users");
        em = factory.createEntityManager();

        users.addAll(loadUsersFromDb());
    }

    private List<User> loadUsersFromDb() {
        return em.createQuery("from User").getResultList();
    }

    public String newUser() {
        System.out.println("About to insert new user");
        currentUser = new User();
        return "goRegistr";
    }

    public boolean isNewUser(User currentUser) {
        User tmp = null;

        if (!users.isEmpty()) {
            for (User usr : users) {
                if (currentUser.getLogin().equals(usr.getLogin())) {
                    tmp = currentUser;
                    break;
                }
            }
        }
        if (tmp == null)
            return true;
        else
            return false;
    }

    public String saveUser() {
        System.out.println("About to save user");
        FacesMessage msg = null;
        em.getTransaction().begin();

        if (isNewUser(currentUser)) {
            users.add(currentUser);
            em.persist(currentUser);
            em.getTransaction().commit();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User (" + currentUser.getLogin() + ") registered", "");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            currentUser = null; //
            return "goLogin";
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Such user is present!", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        String avatarName = event.getFile().getFileName();
        byte[] data = event.getFile().getContents(); //this line could be wrong!!!

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File("path" + avatarName)); //incorrect
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }

    public String backToLog() {
        return "goLogin";
    }
}
