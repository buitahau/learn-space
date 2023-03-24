package hau.kute.dojo.dto.auth;

import java.io.Serializable;

public class AuthDto implements Serializable {

    private static final long serialVersionUID = 8014422384153981908L;

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
