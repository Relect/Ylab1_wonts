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
                            in.readLn();
                        }
                    }
                    break;
                default:
                    out.writeLn("команда неверна, повторите заново");
            }
        }

    }
}