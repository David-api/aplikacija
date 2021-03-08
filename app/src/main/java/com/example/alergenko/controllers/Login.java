package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;

import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergen;
import com.example.alergenko.exceptions.EmptyInputException;
import com.example.alergenko.exceptions.WrongUsernameOrPasswordException;
import com.example.alergenko.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void login(View v){
        //inicializacija in deklaracija vsega potrebnega
        TextView exceptionInfo = (TextView)findViewById(R.id.txtInfo);
        EditText txtInUsername= (EditText)findViewById(R.id.txtInUsername);
        EditText psswdInPsswd = (EditText)findViewById(R.id.psswdInPassword1);
        String usernameIn = "";
        String passwordIn = "";

        try {
            //pridovivanje podatkov iz vnosov
            usernameIn = (txtInUsername.getText().toString() != null) ? txtInUsername.getText().toString() : "";
            passwordIn = (psswdInPsswd.getText().toString() != null) ? psswdInPsswd.getText().toString() : "";

            //preverjanje da niso vnosna polja prazna
            if(usernameIn.equals(""))
                throw new EmptyInputException("Uporabniško ime manjka!");
            if(passwordIn.equals(""))
                throw new EmptyInputException("Geslo manjka!");

            //metoda za pridobivanje podatkov uporabnika, ki se zeli prijaviti
            //podatki se zapisejo v class User kot staticne spremenljivke
            getDataFromDB(usernameIn, passwordIn, exceptionInfo);

            //metoda za preverjane vnesenih podatkov
            checkLogin(usernameIn, User.username, passwordIn, User.password, exceptionInfo);


        } catch (EmptyInputException e){  //ravnanje z izjemami pri vnosu
            exceptionInfo.setText(e.getMessage());
        } catch (Exception e){
            exceptionInfo.setText("Napaka!");
            e.printStackTrace();
        }
    }

    public void getDataFromDB(String usernameIn, String passwordIn, TextView exceptionInfo){
        //pridobivanje podatkov uporabnika, ki se zeli prijaviti, iz baze
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //pridobivanje osnovnih podatkov
            String query = "select * \n" +
                    "from apl_user \n" +
                    "where username like '" + usernameIn + "' and password = '" + passwordIn + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User.id = rs.getInt("id");
                User.name = rs.getString("name");
                User.surname = rs.getString("surname");
                User.gmail = rs.getString("gmail");
                User.username = rs.getString("username");
                User.phoneNumber = rs.getString("phoneNumber");
                User.password = rs.getString("password");
            }

            //pridobivanje podatkov o allergenih
            query = "select ua.allergen_id, a.name\n" +
                    "from apl_user_allergen ua\n" +
                    "    inner join apl_user u\n" +
                    "        on u.id = ua.user_id\n" +
                    "    inner join apl_allergen a\n" +
                    "        on ua.allergen_id = a.id\n" +
                    "where u.id = " + User.id;
            rs = stmt.executeQuery(query);

            while (rs.next())
                User.allergens.add(new Allergen(rs.getInt("allergen_id"), rs.getString("name")));

            con.close();
            stmt.close();
            rs.close();
        }  catch (SQLException e){
            exceptionInfo.setText("Napaka pri pridobivanju podatkov iz podatkovne baze!");
        } catch (Exception e){
            exceptionInfo.setText(e.getMessage());
        }

    }

    public void checkLogin(String usernameIn, String usernameDB, String passwordIn, String passwordDB, TextView exceptionInfo){
        //preverjanje ali so vneseni podatki pravilni
        try {
            if (usernameIn.equals(usernameDB) && passwordIn.equals(passwordDB))
                startActivity(new Intent(Login.this, MainActivity.class));
            else
                throw new WrongUsernameOrPasswordException("Napačno uporabniško ime ali geslo!");
        } catch (WrongUsernameOrPasswordException e) { //ravnanje z izjemami pri napacnem vnosu
            exceptionInfo.setText(e.getMessage());
        }
    }


    //odpre 1. okno za registracijo
    public void openRegister1Activity(View v){
        startActivity(new Intent(Login.this, Register1.class));
    }
}
