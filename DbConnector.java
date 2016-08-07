package bank;

import java.sql.*;

public class DbConnector {
    static Connection conn = null;
    static String url = "jdbc:mysql://localhost:3306/bank";
    static String user = "root";
    static String pass = "";
    static Connection establishConnection(){
        try {
            conn = DriverManager.getConnection(url, user, pass);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
