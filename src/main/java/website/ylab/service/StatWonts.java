package website.ylab.service;

import website.ylab.enums.Freq;
import website.ylab.enums.Status;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.util.Calendar;
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
                    Calendar startDay = getDay();
                    boolean getDone = wont.getListDone().stream()
                            .anyMatch(date -> date.after(startDay));
                    if (getDone) {
                        out.writeLn("привычка уже выполнена в течение предыдущих суток");
                        return;
                    }
                    wont.addDoneWont(Calendar.getInstance());
                    wont.setStatus(Status.DONE);
                } else {
                    Calendar startWeek = getWeek();
                    boolean getDone = wont.getListDone().stream()
                            .anyMatch(date -> date.after(startWeek));
                    if (getDone) {
                        out.writeLn("привычка уже выполнена в течение предыдущей недели");
                        return;
                    }
                    wont.addDoneWont(Calendar.getInstance());
                    wont.setStatus(Status.DONE);
                }
                return;
            }
        }
    }

    public void generateStat(Read in, Write out, User user) {
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
                while (true) {
                    boolean exit = false;
                    out.writeLn("""
                            1 для генерации статистики за день,
                            2 для генерации статистики за неделю,
                            3 для генерации статистики за месяц,
                            exit для выхода в предыдущее меню""");
                    String command = in.readLn();

                    switch (command) {
                        case "1":
                            out.writeLn(wont.toString());
                            Calendar day = getDay();
                            long done = wont.getListDone().stream()
                                    .filter(calendar -> calendar.after(day))
                                    .peek(calendar -> out.writeLn(calendar.getTime().toString()))
                                    .count();
                            out.writeLn("выполнена " + done + " раз");
                            break;
                        case "2":
                            out.writeLn(wont.toString());
                            Calendar week = getWeek();
                            long done2 = wont.getListDone().stream()
                                    .filter(calendar -> calendar.after(week))
                                    .peek(calendar -> out.writeLn(calendar.getTime().toString()))
                                    .count();
                            out.writeLn("выполнена " + done2 + " раз");
                            break;
                        case "3":
                            out.writeLn(wont.toString());
                            Calendar month = getMonth();
                            long done3 = wont.getListDone().stream()
                                    .filter(calendar -> calendar.after(month))
                                    .peek(calendar -> out.writeLn(calendar.getTime().toString()))
                                    .count();
                            out.writeLn("выполнена " + done3 + " раз");
                            break;
                        case "exit":
                            exit = true;
                            break;
                        default:
                            out.writeLn("Команда неверна, повторите заново.");
                    }



                    if (exit) break;
                }
            } else {
                out.writeLn("неверное название привычки");
            }
        }
    }

    public Calendar getDay() {
        Calendar startDay = Calendar.getInstance();
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        startDay.set(Calendar.MILLISECOND, 0);
        return startDay;
    }

    public Calendar getWeek() {
        Calendar startWeek = Calendar.getInstance();
        startWeek.add(Calendar.DAY_OF_MONTH, -6);
        startWeek.set(Calendar.HOUR_OF_DAY, 0);
        startWeek.set(Calendar.MINUTE, 0);
        startWeek.set(Calendar.SECOND, 0);
        startWeek.set(Calendar.MILLISECOND, 0);
        return startWeek;
    }

    public Calendar getMonth() {
        Calendar startMonth = Calendar.getInstance();
        startMonth.add(Calendar.MONTH, -1);
        startMonth.add(Calendar.DAY_OF_MONTH, 1);
        startMonth.set(Calendar.HOUR_OF_DAY, 0);
        startMonth.set(Calendar.MINUTE, 0);
        startMonth.set(Calendar.SECOND, 0);
        startMonth.set(Calendar.MILLISECOND, 0);
        return startMonth;
    }
}
