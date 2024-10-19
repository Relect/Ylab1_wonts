package website.ylab.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class User {

    private long id;
    private String  name;
    private String email;
    private String password;
    private List<Wont> wonts = new ArrayList<>();
    private boolean admin = false;
    private boolean block = false;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setWont(Wont wont) {
        wonts.add(wont);
    }
}
