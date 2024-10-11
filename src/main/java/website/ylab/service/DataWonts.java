package website.ylab.service;

import website.ylab.enums.Freq;
import website.ylab.enums.Status;
import website.ylab.in.Read;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.util.Date;

public class DataWonts {

    DataUsers dataUsers;
    public DataWonts(DataUsers dataUsers) {
        this.dataUsers = dataUsers;
    }

    public void controlWonts(Read in, Write out) {
        while (true) {
            out.writeLn("""
                    1 для создания привычки,
                    2 для редактирования привычки,
                    3 для удаления привычки,
                    4 для просмотра привычек
                    exit для выхода в предыдущее меню.""");
            String command = in.readLn();

            switch (command) {
                case "1":
                    addWont(in, out);
                    break;
                case "2":
                    editWont(in, out);
                    break;
                case "3":
                    deleteWont(in, out);
                    break;
                case "4":
                    watchWonts(out);
                    break;
                case "exit":
                    return;
                default:
                    out.writeLn("Команда неверна, повторите заново.");
            }
        }
    }

    public void addWont(Read in, Write out) {
        out.writeLn("Введите название привычки");
        String wontName = in.readLn();
        out.writeLn("Введите описание привычки");
        String wontInfo = in.readLn();
        Freq freq;
        while (true) {
            out.writeLn("""
                Введите частоту привычки:
                1 ежедневно,
                2 еженедельно.""");
            String str = in.readLn();
            if (str.equals("1")) {
                freq = Freq.EVERYDAY;
                break;
            } else if (str.equals("2")) {
                freq = Freq.EVERYWEEK;
                break;
            } else {
                out.writeLn("Неверный ввод");
            }
        }
        Wont wont = new Wont(wontName, wontInfo,
                freq, new Date(), Status.ACTIVE);

    }

    public void editWont(Read in, Write out) {

    }

    public void deleteWont(Read in, Write out) {

    }

    public void watchWonts(Write out) {

    }
}
