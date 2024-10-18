package website.ylab.model;

import lombok.Getter;
import lombok.Setter;
import website.ylab.custom.Freq;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Setter
@Getter
public class Wont {

    private long id;
    private String name;
    private String info;
    private Freq freq2;
    private String freq;
    private Calendar createdAt;
    private boolean done;

    private List<Calendar> listDone = new ArrayList<>();

    public Wont() {}
    public Wont(String name, String info,
                Freq freq2, Calendar createdAt, boolean done) {
        this.name = name;
        this.info = info;
        this.freq2 = freq2;
        this.createdAt = createdAt;
        this.done = done;
    }

    public void addDoneWont(Calendar calendar) {
        listDone.add(calendar);
    }

    @Override
    public String toString() {
        return "Wont{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", freq=" + freq2 +
                ", createAt=" + createdAt.getTime() +
                ", status=" + done +
                '}';
    }
}
