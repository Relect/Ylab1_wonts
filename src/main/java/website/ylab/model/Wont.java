package website.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import website.ylab.enums.Freq;
import website.ylab.enums.Status;

import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
public class Wont {

    private String name;
    private String info;

    private Freq freq;
    private Date createAt;
    private Status status;

    @Override
    public String toString() {
        return "Wont{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", freq=" + freq +
                ", createAt=" + createAt +
                ", status=" + status +
                '}';
    }
}
