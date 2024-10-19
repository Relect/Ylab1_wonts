/** package website.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
                .thenReturn(Constants.USER_NAME)
                .thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
    }
    @DisplayName("проверка количества пользователей")
    @Test
    public void addUserTest1() {
        dataUsers.addUser(in, out);
        assertThat(dataUsers.getUsers()).hasSize(2);
    }
    @DisplayName("проверка количества пользователей после добавления")
    @Test
    public void addUserTest2() {
        dataUsers.addUser(in, out);
        User actual = dataUsers.getUsers().get(Constants.USER_EMAIL);
        User expected = new User(Constants.USER_NAME, Constants.USER_EMAIL, Constants.USER_PASSWORD);
        assertThat(actual).isEqualTo(expected);
    }
    @DisplayName("проверка входа пользователя")
    @Test
    public void loginTest() {
        dataUsers.addUser(in, out);

        Mockito.when(in.readLn()).thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
        User actual = dataUsers.login(in, out);
        User expected = dataUsers.users.get(Constants.USER_EMAIL);
        assertThat(actual).isEqualTo(expected);
    }
    @DisplayName("проверка удаления пользователя")
    @Test
    public void deleteTest() {
        dataUsers.addUser(in, out);

        dataUsers.deleteUser(Constants.USER_EMAIL, out);
        assertThat(dataUsers.getUsers()).hasSize(1);
    }
    @DisplayName("проверка обновления пользователя")
    @Test
    public void editUserTest() {
        dataUsers.addUser(in, out);

        Mockito.when(in.readLn()).thenReturn("3")
                .thenReturn("321");
        dataUsers.editUser(Constants.USER_EMAIL, in, out);
        User actual = dataUsers.getUsers().get(Constants.USER_EMAIL);
        User expected = new User(Constants.USER_NAME, Constants.USER_EMAIL, "321");
        assertThat(actual).isEqualTo(expected);
    }
}
*/