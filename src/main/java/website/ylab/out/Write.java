package website.ylab.out;

import java.io.PrintStream;

public class Write {

    private final PrintStream out = System.out;

    public void writeLn(String str) {
        out.println(str);
    }
}
