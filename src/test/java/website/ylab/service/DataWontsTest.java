package website.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.custom.Freq;
import website.ylab.custom.Status;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.model.Wont;
import website.ylab.out.Write;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
public class DataWontsTest {

    @Mock
    Read in;
    @Mock
    Write out;

    DataWonts dataWonts;
    DataUsers dataUsers;
    User user;

    @BeforeEach
    public void beforeEach() {
        dataUsers = new DataUsers();
        Mockito.when(in.readLn())
                .thenReturn("Gennady")
                .thenReturn("relect@bk.ru")
                .thenReturn("123");
        dataUsers.addUser(in, out);
        dataWonts = new DataWonts(dataUsers);
        user = dataUsers.users.get("relect@bk.ru");
        Mockito.when(in.readLn()).thenReturn("чистка зубов")
                .thenReturn("предотвращает кариес")
                .thenReturn("1");


        dataWonts.addWont(in, out, user);
    }

    @Test
    public void addWontsTest() {

        assertThat(user.getWonts().size())
                .isEqualTo(1);
        Wont wont = user.getWonts().get(0);

        assertThat(wont.getName()).isEqualTo("чистка зубов");
        assertThat(wont.getInfo()).isEqualTo("предотвращает кариес");
        assertThat(wont.getFreq()).isEqualTo(Freq.EVERYDAY);
        assertThat(wont.getStatus()).isEqualTo(Status.DEFFERRED);
    }

    @Test
    public void editWontTest() {
        Mockito.when(in.readLn()).thenReturn("чистка зубов")
                .thenReturn("2").thenReturn("защищает зубы");
        dataWonts.editWont(in, out, user);
        assertThat(user.getWonts().size())
                .isEqualTo(1);
        Wont wont = user.getWonts().get(0);
        assertThat(wont.getInfo()).isEqualTo("защищает зубы");
    }

    @Test
    public void deleteWontTest() {
        Mockito.when(in.readLn()).thenReturn("чистка зубов");
        dataWonts.deleteWont(in, out, user);
        assertThat(user.getWonts().size())
                .isEqualTo(0);

    }
}
