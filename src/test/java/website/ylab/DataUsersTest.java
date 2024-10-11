package website.ylab;

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

    @Test
    public void addUserTest1() {
        DataUsers dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("3");
        dataUsers.addUser(in, out);
        assertThat(dataUsers.getUsers()).hasSize(1);
    }

    @Test
    public void addUserTest2() {
        DataUsers dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn("Gennady")
                .thenReturn("relect@bk.ru")
                .thenReturn("123");
        dataUsers.addUser(in, out);
        User actual = dataUsers.getUsers().get("relect@bk.ru");
        User expected = new User("Gennady", "relect@bk.ru", "123");
        assertThat(actual).isEqualTo(expected);
    }

}
