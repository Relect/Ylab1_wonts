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
                .thenReturn("Gennady")
                .thenReturn("relect@bk.ru")
                .thenReturn("123");
        dataUsers.addUser(in, out);
        assertThat(dataUsers.getUsers()).hasSize(1);
    }


}
