package website.ylab.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String  name;
    private String email;
    private String password;

    private List<Wont> wonts = new ArrayList<>();

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWont(Wont wont) {
        wonts.add(wont);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Wont> getWonts() {
        return wonts;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", wonts=" + wonts +
                '}';
    }
}
