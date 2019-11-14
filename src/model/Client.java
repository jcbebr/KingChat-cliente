package model;

/**
 *
 * @author José Carlos
 */
public class Client {

    private Integer id;
    private String nick;
    private String email;
    private String oldemail;
    private String pass;
    private int birth;
    private boolean online;
    private String path;
    private int port;

    public Client() {
    }

    public Client(String nick, String email, String oldemail, String pass, int birth, boolean online, String path, int port) {
        this.nick = nick;
        this.email = email;
        this.oldemail = oldemail;
        this.pass = pass;
        this.birth = birth;
        this.online = online;
        this.path = path;
        this.port = port;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldemail() {
        return oldemail;
    }

    public void setOldemail(String oldemail) {
        this.oldemail = oldemail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        if (online) {
            return getNick() + " - online";
        } else {
            return getNick() + " - offline";
        }
    }

}
