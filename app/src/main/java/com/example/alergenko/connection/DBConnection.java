package com.example.alergenko.connection;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    public static Connection getConnection(){
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            connection = createConnection();
            return connection;

        } catch (SQLException e) {
            System.out.println("Napaka v OracleConnection classu (SQLException)!");
            e.printStackTrace();
            return null;
        } catch (Exception e){
            System.out.println("Napaka v OracleConnection classu (Exception)!");
            e.printStackTrace();
            return null;
        }
    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DBStrings.DRIVER, DBStrings.URL, DBStrings.USERNAME, DBStrings.PASSWORD);
    }

}
