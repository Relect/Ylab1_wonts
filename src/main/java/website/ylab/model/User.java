package website.ylab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(wonts, user.wonts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, wonts);
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
