package website.ylab.service;

import org.testcontainers.containers.PostgreSQLContainer;
import website.ylab.db.DBManager;

import java.sql.Connection;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:10.4";
    private static final String DB_NAME = "testdb";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "testpw";
    private static final PostgresContainer POSTGRES_CONTAINER = new PostgresContainer()
            .withExposedPorts(5434,5432)
            .withDatabaseName(DBManager.properties.getProperty("db.name"))
            .withUsername(DBManager.properties.getProperty("user"))
            .withPassword(DBManager.properties.getProperty("password"));

    private PostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresContainer getInstance(){
        return POSTGRES_CONTAINER;
    }

    @Override
    public void start(){
        super.start();
        DBManager.DATABASE_URL = POSTGRES_CONTAINER.getJdbcUrl();

    }

    @Override
    public void stop(){
        //do nothing
    }

}
