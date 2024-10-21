package website.ylab.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.db.DBManager;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DataUsersTest {

    @Mock
    Read in;
    @Mock
    Write out;

    DataUsers dataUsers;

    @BeforeAll
    public static void beforeAll() {

        PostgresContainer postgresContainer = PostgresContainer.getInstance();
        postgresContainer.start();
        System.out.println("старт контейнера");

        try (Connection conn = DBManager.getConn()) {
            conn.createStatement().execute("CREATE SCHEMA new;");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            database.setDefaultSchemaName("new");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("migrate successful");
        } catch (LiquibaseException e) {
            System.out.println("ошибка liquibase");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

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
        try (Connection conn = DBManager.getConn()){
            ResultSet rs = conn.createStatement().executeQuery("SELECT count(*) from new.users;");
            rs.next();
            long count = rs.getLong("count");
            assertThat(count).isEqualTo(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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