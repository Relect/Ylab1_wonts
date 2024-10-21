package website.ylab.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
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
    static PostgresContainer container;

    @BeforeAll
    public static void beforeAll() {

        container = PostgresContainer.getInstance();
        container.start();

        try (Connection conn = DBManager.getConn()) {
            conn.createStatement().execute("CREATE SCHEMA new;");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            database.setDefaultSchemaName("new");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach() {
        dataUsers = new DataUsers();
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @DisplayName("проверка добавления пользователя")
    @Test
    public void addUserTest1() {
        Mockito.when(in.readLn())
                .thenReturn(Constants.USER_NAME)
                .thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
        dataUsers.addUser(in, out);
        try (Connection conn = DBManager.getConn()){
            ResultSet rs = conn.createStatement().executeQuery("SELECT count(*) from new.users;");
            rs.next();
            long count = rs.getLong("count");
            assertThat(count).isEqualTo(2);

            rs.close();
            rs = conn.createStatement().executeQuery("SELECT name, password from new.users WHERE email = " +
                    "'" + Constants.USER_EMAIL + "'" + " ;");
            rs.next();
            assertThat(rs.getString("name")).isEqualTo(Constants.USER_NAME);
            assertThat(rs.getString("password")).isEqualTo(Constants.USER_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("проверка удаления пользователя")
    @Test
    public void deleteTest() {

        dataUsers.deleteUser(Constants.USER_EMAIL, out);
        try (Connection conn = DBManager.getConn()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT count(*) from new.users;");
            rs.next();
            long count = rs.getLong("count");
            assertThat(count).isEqualTo(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}