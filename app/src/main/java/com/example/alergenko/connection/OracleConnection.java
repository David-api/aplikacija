package com.example.alergenko.connection;

import java.sql.Connection;
import oracle.jdbc.pool.OracleDataSource;

public class OracleConnection {
    public static OracleDataSource oracleDataSource;
    public static Connection getConnection() {
        if (oracleDataSource == null) {
            try {
                oracleDataSource = new OracleDataSource();
                oracleDataSource.setURL(DBStrings.URL);
                oracleDataSource.setUser(DBStrings.USERNAME);
                oracleDataSource.setPassword(DBStrings.PASSWORD);
            } catch (Exception e) {
            }
        }
        Connection connection = null;
        try {
            connection = oracleDataSource.getConnection();
        } catch (Exception e) {
        }
        return connection;
    }
}

