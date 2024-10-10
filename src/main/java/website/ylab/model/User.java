package website.ylab.model;

import java.util.List;

public class User {

    private String  name;
    private String email;
    private String password;

    private List<String> worts;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getWorts() {
        return worts;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", worts=" + worts +
                '}';
    }
}
