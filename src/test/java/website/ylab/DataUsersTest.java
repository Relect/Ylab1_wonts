package website.ylab;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import static org.mockito.ArgumentMatchers.any;

public class DataUsersTest {

    @Mock
    Read in;
    @Mock
    Write out;

    @Test
    public void addUserTest() {
        DataUsers dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn("Gennady")
                .thenReturn("relect@bk.ru")
                .thenReturn("123");
        Mockito.doNothing().when(out.writeLn(any(String.class)));


    }
}
