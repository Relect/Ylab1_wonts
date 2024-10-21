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
import website.ylab.model.Wont;
import website.ylab.out.Write;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    static PostgresContainer container;

    @BeforeAll
    public static void beforeAll() {

        container = PostgresContainer.getInstance();
        container.start();

        try (Connection conn = DBManager.getConn()) {
            conn.createStatement().execute("CREATE SCHEMA if not exists new;");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            database.setDefaultSchemaName("new");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @BeforeEach
    public void beforeEach() {
        Mockito.when(in.readLn())
                .thenReturn(Constants.USER_NAME)
                .thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
        dataUsers = new DataUsers();
        dataUsers.addUser(in, out);
        dataWonts = new DataWonts(dataUsers);

        Mockito.when(in.readLn())
                .thenReturn(Constants.USER_EMAIL)
                .thenReturn(Constants.USER_PASSWORD);
        user = dataUsers.login(in, out);
        Mockito.when(in.readLn()).thenReturn(Constants.WONT_NAME)
                .thenReturn(Constants.WONT_INFO)
                .thenReturn("1");
        dataWonts.addWont(in, out, user);
    }
    @AfterEach
    public void afterEach() {
        dataUsers.deleteUser(Constants.USER_EMAIL, out);
    }
    @DisplayName("проверка выполнения привычки, после добавления")
    @Test
    public void doWontTest() {
        Mockito.when(in.readLn()).thenReturn(Constants.WONT_NAME);
        statWonts.doWont(in, out, user);

        try (Connection conn = DBManager.getConn()) {
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT count(*) from new.done;");
            rs.next();
            long count = rs.getLong("count");
            assertThat(count).isEqualTo(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}