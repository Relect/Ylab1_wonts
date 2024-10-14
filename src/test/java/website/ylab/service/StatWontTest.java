package website.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StatWontTest {


    @Mock
    Read in;
    @Mock
    Write out;

    DataWonts dataWonts;
    DataUsers dataUsers;
    User user;
    StatWonts statWonts = new StatWonts();

    @BeforeEach
    public void beforeEach() {
        dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn(Constants.USER_NAME)
                .thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
        dataUsers.addUser(in, out);
        dataWonts = new DataWonts(dataUsers);
        user = dataUsers.users.get(Constants.USER_EMAIL);
        Mockito.when(in.readLn()).thenReturn(Constants.WONT_NAME)
                .thenReturn(Constants.WONT_INFO)
                .thenReturn("1");


        dataWonts.addWont(in, out, user);
    }

    @Test
    public void doWontTest() {
        Mockito.when(in.readLn()).thenReturn(Constants.WONT_NAME);
        statWonts.doWont(in, out, user);
        Wont wont = user.getWonts().get(0);
        assertThat(wont.getListDone().size()).isEqualTo(1);
    }
}
