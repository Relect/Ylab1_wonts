package website.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import website.ylab.enums.Freq;
import website.ylab.enums.Status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Setter
@Getter
public class Wont {

    private String name;
    private String info;

    private Freq freq;
    private Calendar createAt;
    private Status status;
    private List<Calendar> listDone = new ArrayList<>();

    public Wont(String name, String info,
                Freq freq, Calendar createAt, Status status) {
        this.name = name;
        this.info = info;
        this.freq = freq;
        this.createAt = createAt;
        this.status = status;
    }

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
