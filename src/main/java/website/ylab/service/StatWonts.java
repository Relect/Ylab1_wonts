package website.ylab.service;

import website.ylab.custom.Freq;
import website.ylab.db.DBManager;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
                    long wont_id = rs.getLong("id");
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
                        return;

                    } else {
                        String sql3 = "SELECT exec FROM new.done WHERE wont_id = ? " +
                                " ORDER BY exec DESC;";
                        PreparedStatement ps3 = conn.prepareStatement(sql3);
                        ps3.setLong(1, wont_id);
                        ResultSet rs3 = ps3.executeQuery();
                        rs3.next();
                        Date date = rs3.getTimestamp("exec");
                        Calendar last = Calendar.getInstance();
                        last.setTime(date);
                        Calendar start = freq.equals(Freq.EVERYDAY.name()) ? getDay() : getWeek();
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


            try (Connection conn = DBManager.getConn()) {
                String sql = "SELECT id, done FROM new.wonts WHERE user_id = ? AND name = ?;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, user.getId());
                ps.setString(2, wontName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    long wont_id = rs.getLong("id");
                    boolean done = rs.getBoolean("done");
                    if (!done) {
                        out.writeLn("привычка " + wontName + "не выполнялась.");
                    } else {
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
                                    String sql2 = "SELECT count(*) FROM new.done WHERE wont_id = ? and exec >= ?";
                                    PreparedStatement ps2 = conn.prepareStatement(sql2);
                                    ps2.setLong(1, wont_id);
                                    Calendar day = getDay();
                                    ps2.setTimestamp(2, new Timestamp(day.getTimeInMillis()));
                                    ResultSet rs2 = ps2.executeQuery();
                                    rs2.next();
                                    long count = rs2.getLong("count");
                                    if (count == 0) {
                                        out.writeLn("за день выполнений не найдено");
                                    } else {
                                        rs2.close();
                                        ps2.close();
                                        sql2 = "SELECT exec FROM new.done WHERE wont_id = ? and exec >= ?";
                                        ps2 = conn.prepareStatement(sql2);
                                        ps2.setLong(1, wont_id);
                                        ps2.setTimestamp(2, new Timestamp(day.getTimeInMillis()));
                                        rs2 = ps2.executeQuery();
                                        while (rs2.next()) {
                                            out.writeLn(rs2.getDate("exec").toString());
                                        }
                                        out.writeLn("выполнена " + count + " раз");
                                    }
                                    out.writeLn("для продолжения нажмите enter");
                                    in.readLn();
                                    break;
                                case "2":
                                    String sql3 = "SELECT count(*) FROM new.done WHERE wont_id = ? and exec >= ?";
                                    PreparedStatement ps3 = conn.prepareStatement(sql3);
                                    ps3.setLong(1, wont_id);
                                    Calendar day3 = getWeek();
                                    ps3.setTimestamp(2, new Timestamp(day3.getTimeInMillis()));
                                    ResultSet rs3 = ps3.executeQuery();
                                    rs3.next();
                                    long count3 = rs3.getLong("count");
                                    if (count3 == 0) {
                                        out.writeLn("за неделю выполнений не найдено");
                                    } else {
                                        rs3.close();
                                        ps3.close();
                                        sql3 = "SELECT exec FROM new.done WHERE wont_id = ? and exec >= ?";
                                        ps3 = conn.prepareStatement(sql3);
                                        ps3.setLong(1, wont_id);
                                        ps3.setTimestamp(2, new Timestamp(day3.getTimeInMillis()));
                                        rs3 = ps3.executeQuery();
                                        while (rs3.next()) {
                                            out.writeLn(rs3.getDate("exec").toString());
                                        }
                                        out.writeLn("выполнена " + count3 + " раз");
                                    }
                                    out.writeLn("для продолжения нажмите enter");
                                    in.readLn();
                                    break;
                                case "3":
                                    String sql4 = "SELECT count(*) FROM new.done WHERE wont_id = ? and exec >= ?";
                                    PreparedStatement ps4 = conn.prepareStatement(sql4);
                                    ps4.setLong(1, wont_id);
                                    Calendar day4 = getMonth();
                                    ps4.setTimestamp(2, new Timestamp(day4.getTimeInMillis()));
                                    ResultSet rs4 = ps4.executeQuery();
                                    rs4.next();
                                    long count4 = rs4.getLong("count");
                                    if (count4 == 0) {
                                        out.writeLn("за месяц выполнений не найдено");
                                    } else {
                                        rs4.close();
                                        ps4.close();
                                        sql4 = "SELECT exec FROM new.done WHERE wont_id = ? and exec >= ?";
                                        ps4 = conn.prepareStatement(sql4);
                                        ps4.setLong(1, wont_id);
                                        ps4.setTimestamp(2, new Timestamp(day4.getTimeInMillis()));
                                        rs4 = ps4.executeQuery();
                                        while (rs4.next()) {
                                            out.writeLn(rs4.getDate("exec").toString());
                                        }
                                        out.writeLn("выполнена " + count4 + " раз");
                                    }
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
                    }
                } else {
                    out.writeLn("неверное название привычки");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void streak(Read in, Write out, User user) {

        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT id, name, freq, done FROM new.wonts WHERE user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                out.writeLn("У пользователя нет привычек");
            } else {
                do {
                    long wont_id = rs.getLong("id");
                    String wontName = rs.getString("name");
                    boolean done = rs.getBoolean("done");

                    if (!done) {
                        String str = String.format("Привычка %s не выполнена ни разу", wontName);
                        out.writeLn(str);
                    } else {
                        List<Calendar> list = new ArrayList<>();
                        String sql2 = "SELECT exec FROM new.done WHERE wont_id = ? ORDER BY exec;";
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ps2.setLong(1, wont_id);
                        ResultSet rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(rs2.getTimestamp("exec"));
                            list.add(cal);
                        }

                        if (rs.getString("freq") == Freq.EVERYDAY.name()) {
                            Calendar current, end;
                            end = getDayPlusTwo(list.get(0));
                            int part = 1;
                            for (int j = 0; j < list.size(); j++) {
                                if (j == 0) {
                                    out.writeLn("Привычка" + wontName + ", " + part + " Серия.");
                                    part++;
                                    current = list.get(0);
                                    out.writeLn(current.getTime().toString());
                                } else {
                                    current = list.get(j);
                                    if (current.after(end)) {
                                        out.writeLn("Привычка" + wontName + ", " + part + " Серия.");
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
                                    out.writeLn("Привычка" + wontName + ", " + part + " Серия.");
                                    part++;
                                    current = list.get(0);
                                    out.writeLn(current.getTime().toString());
                                } else {
                                    current = list.get(j);
                                    if (current.after(end)) {
                                        out.writeLn("Привычка" + wontName + ", " + part + " Серия.");
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
                } while (rs.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.writeLn("для продолжения нажмите enter");
        in.readLn();
    }

    public void getRateOfTime(Read in, Write out, User user) {

        try (Connection conn = DBManager.getConn()){
            String sql = "SELECT id, name, freq, done FROM new.wonts WHERE user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                out.writeLn("У пользователя нет привычек");
                out.writeLn("для продолжения нажмите enter");
                in.readLn();
            } else {
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

                        do {
                            List<Calendar> list = new ArrayList<>();
                            String sql2 = "SELECT exec FROM new.done WHERE wont_id = ?;";
                            PreparedStatement ps2 = conn.prepareStatement(sql2);
                            long wont_id = rs.getLong("id");
                            ps2.setLong(1, wont_id);
                            ResultSet rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(rs2.getTimestamp("exec"));
                                list.add(cal);
                            }

                            String freq = rs.getString("freq");
                            String wontName = rs.getString("name");
                            if (freq.equals(Freq.EVERYDAY.name())) {

                                long days = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
                                if (days == 0) {
                                    days = 1;
                                }
                                double doIt = list.stream()
                                        .filter(done -> done.after(cal1))
                                        .filter(done -> done.before(cal2))
                                        .count();
                                double result = doIt / days * 100;
                                String str = String.format("привычка %s выполненна на %.2f процентов",
                                        wontName, result);
                                out.writeLn(str);
                            } else {
                                long weeks = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24 * 7);
                                if (weeks == 0) {
                                    weeks = 1;
                                }
                                double doIt = list.stream()
                                        .filter(done -> done.after(cal1))
                                        .filter(done -> done.before(cal2))
                                        .count();
                                double result = doIt / weeks * 100;
                                String str = String.format("привычка %s выполненна на %.2f процентов",
                                        wontName, result);
                                out.writeLn(str);
                            }

                        } while (rs.next());
                        out.writeLn("для продолжения нажмите enter");
                        in.readLn();
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getReport(Write out, User user) {

        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT id, name, done FROM new.wonts WHERE user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();


            while (rs.next()){
                String wontName = rs.getString("name");
                if (!rs.getBoolean("done")) {
                    out.writeLn("привычка " + wontName + " не выполнялась 24 дня подряд");
                    continue;
                }
                long wont_id = rs.getLong("id");
                List<Calendar> list = new ArrayList<>();
                String sql2 = "SELECT exec FROM new.done WHERE wont_id = ? ORDER BY exec;";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setLong(1, wont_id);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(rs2.getTimestamp("exec"));
                    list.add(cal);
                }
                Calendar current = list.get(0);
                int count = 1;
                Calendar endDay = getDayPlusTwo(current);

                for (int i = 1; i < list.size() && count != 24; i++) {
                    current = list.get(i);
                    if (current.after(endDay)) {
                        count = 0;
                    } else {
                        count++;
                    }
                    endDay = getDayPlusTwo(current);
                }
                if (count == 24) {
                    out.writeLn("привычка " + wontName + " выполнялась 24 дня подряд");
                } else {
                    out.writeLn("привычка " + wontName + " не выполнялась 24 дня подряд");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
