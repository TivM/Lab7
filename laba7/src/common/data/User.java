package common.data;

import java.io.Serializable;
import java.util.Objects;

public class User implements Validateable, Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String s = "";
        s += "{";
        s += "\"username\" : " + "\"" + username + "\"" + "}";
        return s;
    }

    @Override
    public boolean validate() {
        return (password != null && !password.equals("")) && (username != null && !username.equals(""));
    }
}
