package website.ylab.service;

import website.ylab.custom.Freq;
import website.ylab.db.DBManager;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatWonts {

    public void doWont(Read in, Write out, User user) {
        while (true) {
            out.writeLn("""
                    Ведите название привычки,
                    exit для выхода в предыдущее меню.""");
            String wontName = in.readLn();
            if (wontName.equals("exit")) return;

            try (Connection conn = DBManager.getConn()) {
                String sql = "SELECT id, freq, done FROM new.wonts WHERE name = ? AND user_id = ?;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, wontName);
                ps.setLong(2, user.getId());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    long wont_id = rs.getLong("wont_id");
                    String freq = rs.getString("freq");
                    if (!rs.getBoolean("done")) {
                        String sql2 = "UPDATE new.wonts SET done = true WHERE id = ?;";
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ps2.setLong(1, wont_id);
                        ps2.executeUpdate();
                        ps2.close();
                        sql2 = "INSERT INTO new.done (wont_id) VALUES (?);";
                        ps2 = conn.prepareStatement(sql2);
                        ps2.setLong(1, wont_id);
                        ps2.executeUpdate();
                        out.writeLn("Привычка " + wontName + " выполнена");

                    } else {
                        String sql3 = "SELECT exec FROM new.done WHERE wont_id = ? " +
                                " ORDER BY exec DESC;";
                        PreparedStatement ps3 = conn.prepareStatement(sql3);
                        ps3.setLong(1, wont_id);
                        ResultSet rs3 = ps3.executeQuery();
                        rs3.next();
                        Date date = rs3.getTime("exec");
                        Calendar last = Calendar.getInstance();
                        last.setTime(date);
                        Calendar start = freq == Freq.EVERYDAY.name() ? getDay() : getWeek();
                        if (last.after(start)) {
                            out.writeLn("привычка уже выполнялась");
                        } else {
                            sql = "INSERT INTO new.done (wont_id) VALUES (?);";
                            ps.close();
                            ps = conn.prepareStatement(sql);
                            ps.setLong(1, wont_id);
                            ps.executeUpdate();
                            out.writeLn("Привычка " + wontName + " выполнена");
                        }
                        return;
                    }
                } else {
                    out.writeLn("Привычка " + wontName + " не найдена");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
                            out.writeLn("для продолжения нажмите enter");
                            in.readLn();
                            break;
                        case "2":
                            out.writeLn(wont.toString());
                            Calendar week = getWeek();
                            long done2 = wont.getListDone().stream()
                                    .filter(calendar -> calendar.after(week))
                                    .peek(calendar -> out.writeLn(calendar.getTime().toString()))
                                    .count();
                            out.writeLn("выполнена " + done2 + " раз");
                            out.writeLn("для продолжения нажмите enter");
                            in.readLn();
                            break;
                        case "3":
                            out.writeLn(wont.toString());
                            Calendar month = getMonth();
                            long done3 = wont.getListDone().stream()
                                    .filter(calendar -> calendar.after(month))
                                    .peek(calendar -> out.writeLn(calendar.getTime().toString()))
                                    .count();
                            out.writeLn("выполнена " + done3 + " раз");
                            out.writeLn("для продолжения нажмите enter");
                            in.readLn();
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

    public void streak(Read in, Write out, User user) {
        int size = user.getWonts().size();
        if (size == 0) {
            out.writeLn("У пользователя нет привычек");
            out.writeLn("для продолжения нажмите enter");
            in.readLn();
            return;
        }
        for (int i = 0; i < size; i++) {
            Wont wont = user.getWonts().get(i);
            if (!wont.isDone()) {
                String str = String.format("Привычка %s не выполнена ни разу", wont.getName());
                out.writeLn(str);
                continue;
            }
            List<Calendar> list = wont.getListDone();
            if (wont.getFreq() == Freq.EVERYDAY.name()) {
                Calendar current, end;
                end = getDayPlusTwo(list.get(0));
                int part = 1;
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        out.writeLn("Привычка" + wont.getName() + ", " + part + " Серия.");
                        part++;
                        current = list.get(0);
                        out.writeLn(current.getTime().toString());
                    } else {
                        current = list.get(j);
                        if (current.after(end)) {
                            out.writeLn("Привычка" + wont.getName() + ", " + part + " Серия.");
                            part++;
                            out.writeLn(current.getTime().toString());
                            end = getDayPlusTwo(current);
                        } else {
                            out.writeLn(current.getTime().toString());
                            end = getDayPlusTwo(current);
                        }
                    }
                }
            } else { // everyweek
                Calendar current, end;
                end = getDayPlusWeek(list.get(0));
                int part = 1;
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        out.writeLn("Привычка" + wont.getName() + ", " + part + " Серия.");
                        part++;
                        current = list.get(0);
                        out.writeLn(current.getTime().toString());
                    } else {
                        current = list.get(j);
                        if (current.after(end)) {
                            out.writeLn("Привычка" + wont.getName() + ", " + part + " Серия.");
                            part++;
                            out.writeLn(current.getTime().toString());
                            end = getDayPlusWeek(current);
                        } else {
                            out.writeLn(current.getTime().toString());
                            end = getDayPlusWeek(current);
                        }
                    }
                }
            }
        }

        out.writeLn("для продолжения нажмите enter");
        in.readLn();
    }

    public void getRateOfTime(Read in, Write out, User user) {
        int size = user.getWonts().size();
        if (size == 0) {
            out.writeLn("У пользователя нет привычек");
            out.writeLn("для продолжения нажмите enter");
            in.readLn();
            return;
        }
        while (true) {
            out.writeLn("""
                    введите дату начала периода в формате dd.mm.yyyy
                    например 20.10.2024
                    exit для выхода в предыдущее меню""");
            String str1 = in.readLn();
            if (str1.equals("exit")) return;

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date date1;
            Date date2;
            try {
                date1 = format.parse(str1);
            } catch (ParseException e) {
                out.writeLn("неверный ввод");
                continue;
            }
            while (true) {
                out.writeLn("""
                    введите дату конца периода в формате dd.mm.yyyy
                    например 30.10.2024
                    exit для выхода в меню""");
                String str2 = in.readLn();
                if (str2.equals("exit")) return;
                try {
                    date2 = format.parse(str2);
                } catch (ParseException e) {
                    out.writeLn("неверный ввод");
                    continue;
                }

                if (date2.getTime() < date1.getTime()) {
                    out.writeLn("вторая дата не может быть меньше первой");
                    continue;
                }


                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                for (int i = 0; i < size; i++) {
                    Wont wont = user.getWonts().get(i);
                    if (wont.getFreq() == Freq.EVERYDAY.name()) {

                        long days = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
                        if (days == 0) {
                            days = 1;
                        }
                        double doIt = wont.getListDone().stream()
                                .filter(done -> done.after(cal1))
                                .filter(done -> done.before(cal2))
                                .count();
                        double result = doIt / days * 100;
                        String str = String.format("привычка %s выполненна на %.2f процентов",
                                wont.getName(), result);
                        out.writeLn(str);
                    } else {
                        long weeks = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24 * 7);
                        if (weeks == 0) {
                            weeks = 1;
                        }
                        double doIt = wont.getListDone().stream()
                                .filter(done -> done.after(cal1))
                                .filter(done -> done.before(cal2))
                                .count();
                        double result = doIt / weeks * 100;
                        String str = String.format("привычка %s выполненна на %.2f процентов",
                                wont.getName(), result);
                        out.writeLn(str);
                    }

                }
                out.writeLn("для продолжения нажмите enter");
                in.readLn();
                return;
            }
        }
    }

    public void getReport(Write out, User user) {

        for (Wont wont: user.getWonts()) {

            List<Calendar> list = wont.getListDone();
            if (list.isEmpty()) continue;

            Calendar current = list.get(0);
            int count = 1;
            Calendar endDay = getDayPlusTwo(current);

            for (int i = 1; i < list.size() || count == 24; i++) {
                current = list.get(i);
                if (current.after(endDay)) {
                    count = 0;
                } else {
                    count++;
                }
                endDay = getDayPlusTwo(current);
            }
            if (count == 24) {
                out.writeLn("привычка " + wont.getName() + " выполнялась 24 дня подряд");
            } else {
                out.writeLn("привычка " + wont.getName() + " не выполнялась 24 дня подряд");
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

    public Calendar getDayPlusTwo(Calendar day) {
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(day.getTime());
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        startDay.set(Calendar.MILLISECOND, 0);
        startDay.add(Calendar.DAY_OF_MONTH, 2);
        return startDay;
    }

    public Calendar getDayPlusWeek(Calendar day) {
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(day.getTime());
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        startDay.set(Calendar.MILLISECOND, 0);
        startDay.add(Calendar.DAY_OF_MONTH, 8);
        return startDay;
    }
}
