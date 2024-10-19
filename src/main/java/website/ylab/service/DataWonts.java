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
import java.util.*;

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
                    watchWonts(in, out, user);
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
        String freq;
        while (true) {
            out.writeLn("""
                Введите частоту привычки:
                1 ежедневно,
                2 еженедельно.""");
            String str = in.readLn();
            if (str.equals("1")) {
                freq = Freq.EVERYDAY.name();
                break;
            } else if (str.equals("2")) {
                freq = Freq.EVERYWEEK.name();
                break;
            } else {
                out.writeLn("Неверный ввод");
            }
        }

        try (Connection conn = DBManager.getConn()) {
            String sql = "INSERT INTO new.wonts (user_id, name, info, freq) " +
                    " VALUES (?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ps.setString(2, wontName);
            ps.setString(3, wontInfo);
            ps.setString(4, freq);
            int count = ps.executeUpdate();
            if (count != 0) {
                out.writeLn("Привычка " + wontName + " добавлена.");
            } else {
                out.writeLn("Привычка " + wontName + " не добавлена.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editWont(Read in, Write out, User user) {
        while (true) {
            out.writeLn("""
                    Ведите название привычки,
                    exit для выхода в предыдущее меню.""");
            String wontName = in.readLn();
            if (wontName.equals("exit")) return;

            try (Connection conn = DBManager.getConn()) {
                String sql = "SELECT new.wonts WHERE user_id = ? AND name = ?;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1, user.getId());
                ps.setString(2, wontName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {

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

                                String sql2 = "UPDATE new.wonts SET name = ? WHERE name = ? AND user_id = ?;";
                                PreparedStatement ps2 = conn.prepareStatement(sql2);
                                ps2.setString(1, newName);
                                ps2.setString(2, wontName);
                                ps2.setLong(3, user.getId());
                                int count = ps2.executeUpdate();
                                if (count != 0) {
                                    out.writeLn("Привычка " + wontName + " обновлена на " + newName);
                                }
                                return;
                            case "2":
                                out.writeLn("введите новое описание");
                                String newInfo = in.readLn();

                                String sql3 = "UPDATE new.wonts SET info = ? WHERE name = ? AND user_id = ?;";
                                PreparedStatement ps3 = conn.prepareStatement(sql3);
                                ps3.setString(1, newInfo);
                                ps3.setString(2, wontName);
                                ps3.setLong(3, user.getId());
                                int count3 = ps3.executeUpdate();
                                if (count3 != 0) {
                                    out.writeLn("Привычка " + wontName + " info обновлено на " + newInfo);
                                }
                                return;
                            case "3":
                                String newFreq;
                                while (true) {
                                    out.writeLn("""
                                            Введите новую частоту:
                                            1 ежедневно,
                                            2 еженедельно""");
                                    newFreq = in.readLn();
                                    if (newFreq.equals("1")) {
                                        newFreq = Freq.EVERYDAY.name();
                                        break;
                                    } else if (newFreq.equals("2")) {
                                        newFreq = Freq.EVERYWEEK.name();
                                        break;
                                    } else {
                                        out.writeLn("неверный ввод");
                                    }
                                }
                                String sql4 = "UPDATE new.wonts SET freq = ? WHERE name = ? AND user_id = ?;";
                                PreparedStatement ps4 = conn.prepareStatement(sql4);
                                ps4.setString(1, newFreq);
                                ps4.setString(2, wontName);
                                ps4.setLong(3, user.getId());
                                int count4 = ps4.executeUpdate();
                                if (count4 != 0) {
                                    out.writeLn("Привычка " + wontName + " freq обновлена на " + newFreq);
                                }
                                return;
                            case "exit":
                                return;
                            default:
                                out.writeLn("Команда неверна, повторите заново.");
                        }
                    }
                } else {
                    out.writeLn("Неверный ввод");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteWont(Read in, Write out, User user) {
        out.writeLn("""
                    Ведите название привыки,
                    exit для выхода в предыдущее меню.""");
        String wontName = in.readLn();
        if (wontName.equals("exit")) return;

        try (Connection conn = DBManager.getConn()) {
            String sql = "DELETE from new.wonts WHERE name = ? AND user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wontName);
            ps.setLong(2, user.getId());
            int count = ps.executeUpdate();
            if (count != 0) {
                out.writeLn("Привычка " + wontName + " удалена");
            } else {
                out.writeLn("Привычка " + wontName + " не найдена");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void watchWonts(Read in, Write out, User user) {

        try (Connection conn = DBManager.getConn()) {
            String sql = "Select name, info, freq, created_at, done from new.wonts " +
                    "WHERE user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();
            List<Wont> list = new ArrayList<>();
            Wont wont;
            while (rs.next()) {
                wont = new Wont();
                wont.setName(rs.getString("name"));
                wont.setInfo(rs.getString("info"));
                wont.setFreq(rs.getString("freq"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getTime("created_at"));
                wont.setCreatedAt(calendar);
                wont.setDone(rs.getBoolean("done"));
                list.add(wont);
            }

            if (list.isEmpty()) {
                out.writeLn("список привычек пуст");
                return;
            } else {
                while (true) {
                    out.writeLn("""
                            Введите
                            1 для просмотра всех привычек,
                            2 отсортировать по дате,
                            3 отсортировать по статусу
                            exit для выхода в предыдущее меню""");
                    String command = in.readLn();
                    switch (command) {
                        case "1":
                            for (Wont wont1 : list) {
                                out.writeLn(wont1.toString());
                            }
                            return;
                        case "2":
                            list.sort(Comparator.comparing(o -> o.getCreatedAt().getTime()));
                            for (Wont wont1 : list) {
                                out.writeLn(wont1.toString());
                            }
                            return;
                        case "3":
                            list.sort(Comparator.comparing(Wont::isDone));
                        case "exit":
                            return;
                        default:
                            out.writeLn("неверный ввод");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
