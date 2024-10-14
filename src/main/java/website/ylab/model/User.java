package website.ylab.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String  name;
    @Getter
    private String email;
    @Getter
    private String password;
    @Getter
    private List<Wont> wonts = new ArrayList<>();
    @Getter
    private boolean admin = false;
    @Getter
    private boolean block = false;

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
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return admin == user.admin && block == user.block
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(wonts, user.wonts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
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
