package website.ylab.service;

import website.ylab.enums.Freq;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.util.List;

public class StatWonts {

    public void doWont(Read in, Write out, User user) {
        while (true) {
            out.writeLn("""
                    Ведите название привычки,
                    exit для выхода в предыдущее меню.""");
            String wontName = in.readLn();
            if (wontName.equals("exit")) return;

            List<Wont> list = user.getWonts();
            int j = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(wontName)) {
                    j = i;
                    break;
                }
            }
            if (j != -1) {
                Wont wont = list.get(j);
                if (wont.getFreq() == Freq.EVERYDAY) {

                }
            }
        }
    }
}
