package website.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import website.ylab.enums.Freq;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class Wont {

    private String name;
    private String info;

    private Freq freq;
    private Date createAt;
    private boolean status;
}
