import com.mysql.jdbc.Blob;

import javax.persistence.*;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String login;
    private String password;
    private Boolean online;
    private String avatarPath;

    public User() {
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
    public String getAvatarPath() {
        return avatarPath;
    }
    public void setOnline(Boolean online) {
        this.online = online;
    }
    public Boolean getOnline() {
        return online;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
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
}
