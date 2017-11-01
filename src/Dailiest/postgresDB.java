package Dailiest;

import java.sql.*;

public class postgresDB {
    private Statement state;
    private Connection connection;
    postgresDB() {
        try {
            init();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
    private void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        final String url = "jdbc:postgresql://localhost:5432/dailiest";
        final String user = "postgres";
        final String pass = "4668";
        connection = DriverManager.getConnection(url, user, pass);
        state = connection.createStatement();
    }
    ResultSet execQuery(String query) throws SQLException{
        return state.executeQuery(query);
    }
    void testDB() throws SQLException {
        String query = "select event_name,event_start from public_table.event";
        ResultSet result = state.executeQuery(query);
        while (result.next()) {
            String eventID = result.getString("event_name");
            String eventName = result.getString("event_start");
            System.out.println("event name : "+ eventID+" event start : "+ eventName);
        }
    }

}
