package repositorySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String DATABASE_URL = "jdbc:sqlite:/Users/alexandruspac/Downloads/demo1/smecherie.sqlite";
    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}