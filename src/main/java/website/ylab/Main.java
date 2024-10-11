package website.ylab;


import website.ylab.enums.Freq;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;
import website.ylab.service.DataUsers;
import website.ylab.service.DataWonts;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Read in = new Read();
        Write out = new Write();
        DataUsers dataUsers = new DataUsers();
        DataWonts dataWonts = new DataWonts(dataUsers);
        String command;

        while (true) {
            out.writeLn("""
                    1 Для регистрации нового пользователя введите,
                    2 для авторизации введите,
                    exit для выхода введите.""");
            command = in.readLn();

            switch (command) {
                case "exit":
                    out.writeLn("Thank you for choosing Ylab");
                    System.exit(0);
                case "1":
                    dataUsers.addUser(in, out);
                    break;
                case "2":
                    User user = dataUsers.login(in, out);
                    if (user != null) {

                        while (true) {
                            boolean exit = false;
                            out.writeLn("""
                    1 введите для редактирования пользователя,
                    2 введите для удаления пользователя,
                    3 введите для управления привычками,
                    exit введите для выхода в предыдущее меню.""");
                            String command2 = in.readLn();

                            switch (command2) {
                                case "1":
                                    dataUsers.editUser(user.getEmail(), in, out);
                                    break;
                                case "2":
                                    dataUsers.deleteUser(user.getEmail());
                                    exit = true;
                                    break;
                                case "3":
                                    dataWonts.controlWonts(in, out, user);
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
                    break;
                default:
                    out.writeLn("Команда неверна, повторите заново.");
            }
        }

    }
}