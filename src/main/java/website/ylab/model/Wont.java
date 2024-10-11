package website.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import website.ylab.enums.Freq;
import website.ylab.enums.Status;

import java.util.Date;


@Setter
@Getter
public class Wont {

    private String name;
    private String info;

    private Freq freq;
    private Date createAt;
    private Status status;

    public Wont(String name, String info, Freq freq, Date createAt, Status status) {
        this.name = name;
        this.info = info;
        this.freq = freq;
        this.createAt = createAt;
        this.status = status;
    }
}
