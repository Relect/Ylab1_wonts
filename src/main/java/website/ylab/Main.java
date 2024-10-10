package website.ylab;


import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Read in = new Read();
        Write out = new Write();
        Data data = new Data();
        String command;

        while (true) {
            out.writeLn("""
                    Для регистрации нового пользователя введите 1,
                    для авторизации введите 2,
                    для выхода введите exit.""");
            command = in.readLn();

            switch (command) {
                case "exit":
                    out.writeLn("Thank you for choosing Ylab, goodbye");
                    System.exit(0);
                case "1":
                    data.addUser(in, out);
                    break;
                case "2":
                    User user = data.login(in, out);
                    if (user != null) {
                        while (true) {
                            boolean exit = false;
                            out.writeLn("""                    
                    Для редактирования пользователя введите 1,
                    для удаления введите 2,
                    для выхода в предыдущее меню введите exit.""");
                            String command2 = in.readLn();

                            switch (command2) {
                                case "1":
                                    data.editUser(user.getEmail());
                                    break;
                                case "2":
                                    data.deleteUser(user.getEmail());
                                    exit = true;
                                    break;
                                case "exit":
                                    exit = true;
                                    break;
                                default:
                                    out.writeLn("команда неверна, повторите заново");
                            }
                            if (exit) break;
                        }
                    }
                    break;
                default:
                    out.writeLn("команда неверна, повторите заново");
            }
        }

    }
}