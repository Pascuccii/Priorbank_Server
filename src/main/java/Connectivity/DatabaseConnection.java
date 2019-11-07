package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public DatabaseConnection() { }

    public DatabaseConnection(String URL, String USER, String PASS) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            if(!connection.isClosed()) {
                connected = true;
                System.out.println("Connection succeed!");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

    public int setConnection (String URL, String USER, String PASS) {
        try {
            Connection newCon = DriverManager.getConnection(URL, USER, PASS);
            if(!newCon.isClosed()) {
                connected = true;
                connection = newCon;
                System.out.println("Connection succeed!");
                return 1;
            }
        } catch (SQLException e) {
            return e.getErrorCode();
        }
        return 555;
    }
    public Connection getConnection() {
        return connection;
    }
}
