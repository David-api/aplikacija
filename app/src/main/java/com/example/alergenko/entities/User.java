package com.example.alergenko.entities;

import com.example.alergenko.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    public static int id;
    public static String name;
    public static String surname;
    public static String gmail;
    public static String username;
    public static String phoneNumber;
    public static String password;
    public static ArrayList<Allergens> allergens = new ArrayList<Allergens>();

    public static int getUserId(String username){
        int id = 0;
        try {
            Connection con = DBConnection.getConnection();
            String query = "select id \n" +
                    "from apl_user\n" +
                    "where username like '" + username + "'";
            Statement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next())
                id = rs.getInt("id");

            con.close();
            stmt.close();
            rs.close();

            return  id;
        } catch (SQLException e){
            System.out.println("Napaka v razredu User, metoda getUserId(String username)");
            e.printStackTrace();
            return  0;
        }
    }
}
