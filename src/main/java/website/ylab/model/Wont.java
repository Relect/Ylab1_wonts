package website.ylab.model;

import lombok.Getter;
import lombok.Setter;
import website.ylab.custom.Freq;
import website.ylab.custom.Status;

import java.util.ArrayList;
import java.util.Calendar;
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

    public void addDoneWont(Calendar calendar) {
        listDone.add(calendar);
    }

    @Override
    public String toString() {
        return "Wont{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", freq=" + freq +
                ", createAt=" + createAt.getTime() +
                ", status=" + status +
                '}';
    }
}
