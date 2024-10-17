package website.ylab.service;

import website.ylab.custom.Freq;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.util.Calendar;
import java.util.List;

public class DataWonts {

    DataUsers dataUsers;
    public DataWonts(DataUsers dataUsers) {
        this.dataUsers = dataUsers;
    }

    public void controlWonts(Read in, Write out, User user) {
        StatWonts statWonts = new StatWonts();
        while (true) {
            out.writeLn("""
                    1 для создания привычки,
                    2 для редактирования привычки,
                    3 для удаления привычки,
                    4 для просмотра привычек,
                    5 для выполнения привычки,
                    6 для генерации статистики привычек,
                    7 для вывода серии выполнения всех привычек,
                    8 процент выполнения привычек за период времени,
                    9 отчёт о прогрессе выполнения привычек.
                    exit для выхода в предыдущее меню.""");
            String command = in.readLn();

            switch (command) {
                case "1":
                    addWont(in, out, user);
                    break;
                case "2":
                    editWont(in, out, user);
                    break;
                case "3":
                    deleteWont(in, out, user);
                    break;
                case "4":
                    watchWonts(out, user);
                    break;
                case "5":
                    statWonts.doWont(in, out, user);
                    break;
                case "6":
                    statWonts.generateStat(in, out, user);
                    break;
                case "7":
                    statWonts.streak(in, out, user);
                    break;
                case "8":
                    statWonts.getRateOfTime(in, out, user);
                case "9":
                    statWonts.getReport(out, user);
                    break;
                case "exit":
                    return;
                default:
                    out.writeLn("Команда неверна, повторите заново.");
            }
        }
    }

    public void addWont(Read in, Write out, User user) {
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
                freq, Calendar.getInstance(), false);
        user.setWont(wont);
    }

    public void editWont(Read in, Write out, User user) {
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
                Wont wont = user.getWonts().get(j);
                while (true) {
                    out.writeLn("""
                            1 новое название привычки,
                            2 новое описание привычки,
                            3 новая частота привычки
                            exit для выхода в предыдущее меню""");
                    String command = in.readLn();
                    switch (command) {
                        case "1":
                            out.writeLn("введите новое название");
                            String newName = in.readLn();
                            wont.setName(newName);
                            return;
                        case "2":
                            out.writeLn("введите новое описание");
                            String newInfo = in.readLn();
                            wont.setInfo(newInfo);
                            return;
                        case "3" :
                            while (true) {
                                out.writeLn("""
                                        Введите новую частоту:
                                        1 ежедневно,
                                        2 еженедельно""");
                                String newFreq = in.readLn();
                                if (newFreq.equals("1")) {
                                    wont.setFreq(Freq.EVERYDAY);
                                    return;
                                } else if (newFreq.equals("2")) {
                                    wont.setFreq(Freq.EVERYWEEK);
                                    return;
                                } else {
                                    out.writeLn("неверный ввод");
                                }
                            }
                        case "exit":
                            return;
                        default:
                            out.writeLn("Команда неверна, повторите заново.");
                    }
                }
            } else {
                out.writeLn("Неверный ввод");
            }
        }
    }

    public void deleteWont(Read in, Write out, User user) {
        out.writeLn("""
                    Ведите название привыки,
                    exit для выхода в предыдущее меню.""");
        String wontName = in.readLn();
        if (wontName.equals("exit")) return;

        List<Wont> list = user.getWonts();
        int j = -1;
        for (int i = 0; i < list.size() ; i++) {
            if (list.get(i).getName().equals(wontName)) {
                j = i;
                break;
            }
        }
        if (j != -1) {
            user.getWonts().remove(j);
        }
    }

    public void watchWonts(Write out, User user) {
        for (Wont wont: user.getWonts()) {
            out.writeLn(wont.toString());
        }
    }
}
