package website.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DataUsersTest {

    @Mock
    Read in;
    @Mock
    Write out;

    DataUsers dataUsers;
    @BeforeEach
    public void beforeEach() {
        dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn("Gennady")
                .thenReturn("relect@bk.ru")
                .thenReturn("123");
    }
    @Test
    public void addUserTest1() {
        dataUsers.addUser(in, out);
        assertThat(dataUsers.getUsers()).hasSize(2);
    }
    @Test
    public void addUserTest2() {
        dataUsers.addUser(in, out);
        User actual = dataUsers.getUsers().get("relect@bk.ru");
        User expected = new User("Gennady", "relect@bk.ru", "123");
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void loginTest() {
        dataUsers.addUser(in, out);

        Mockito.when(in.readLn()).thenReturn("relect@bk.ru")
                .thenReturn("123");
        User actual = dataUsers.login(in, out);
        User expected = dataUsers.users.get("relect@bk.ru");
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void deleteTest() {
        dataUsers.addUser(in, out);

        dataUsers.deleteUser("relect@bk.ru");
        assertThat(dataUsers.getUsers()).hasSize(1);
    }
    @Test
    public void editUserTest() {
        dataUsers.addUser(in, out);

        Mockito.when(in.readLn()).thenReturn("3")
                .thenReturn("321");
        dataUsers.editUser("relect@bk.ru", in, out);
        User actual = dataUsers.getUsers().get("relect@bk.ru");
        User expected = new User("Gennady", "relect@bk.ru", "321");
        assertThat(actual).isEqualTo(expected);
    }

}
