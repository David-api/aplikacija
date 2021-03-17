package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.User;
import com.example.alergenko.exceptions.EmptyInputException;
import com.example.alergenko.exceptions.UsernameAlreadyExsistsException;
import com.example.alergenko.exceptions.WeakPasswordExeption;
import com.example.alergenko.exceptions.WrongUsernameOrPasswordException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register1 extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_1);

        //TODO: dodaj on click listenerje
    }

    public void openLoginActivity(View v){
        startActivity(new Intent(Register1.this, Login.class));
    }

    public void register1(View v){
        //TODO: dodaj da preveri dolžino podatkov

        //pridobivanje podatkov iz vnosnih polj
        TextView txtInName = findViewById(R.id.txtInName);
        TextView txtInSurname = findViewById(R.id.txtInSurname);
        TextView txtInEmail = findViewById(R.id.txtInEmail);
        TextView txtInUsername = findViewById(R.id.txtInUsername);
        TextView txtInPhoneNum = findViewById(R.id.txtInPhoneNum);
        TextView psswdInPsswd = findViewById(R.id.psswdInPsswd);
        TextView psswdInPsswd2 = findViewById(R.id.psswdInPsswd2);
        TextView exceptionInfo = findViewById(R.id.txtVException);

        String name = (txtInName.getText().toString() != null) ? txtInName.getText().toString() : "";
        String surname = (txtInSurname.getText().toString() != null) ? txtInSurname.getText().toString() : "";
        String email = (txtInEmail.getText().toString() != null) ? txtInEmail.getText().toString() : "";
        String username = (txtInUsername.getText().toString() != null) ? txtInUsername.getText().toString() : "";
        String phoneNum = (txtInPhoneNum.getText().toString() != null) ? txtInPhoneNum.getText().toString() : "";
        String psswd = (psswdInPsswd.getText().toString() != null) ? psswdInPsswd.getText().toString() : "";
        String psswd2 = (psswdInPsswd2.getText().toString() != null) ? psswdInPsswd2.getText().toString() : "";

        //preverjanje podatkov
        if(checkInputData(name, surname, email, username, phoneNum, psswd, psswd2, exceptionInfo)){
            User.name = name;
            User.surname = surname;
            User.gmail = email;
            User.username = username;
            User.phoneNumber = phoneNum;
            User.password = psswd;
            startActivity(new Intent(Register1.this, Register2.class));
        }
    }

    public boolean checkInputData(String name, String surname, String email, String username,  String phoneNum, String psswd, String psswd2, TextView exceptionInfo){
        //preveri da so vsa polja izpolnjena
        try{
            if(name.equals(""))
                throw new EmptyInputException("Ime manjka!");
            if(surname.equals(""))
                throw new EmptyInputException("Priimek manjka!");
            if(email.equals(""))
                throw new EmptyInputException("Email manjka!");
            if(username.equals(""))
                throw new EmptyInputException("Uporabniško ime manjka!");
            if(phoneNum.equals(""))
                throw new EmptyInputException("Telefonska št. manjka!");
            if(psswd.equals(""))
                throw new EmptyInputException("Geslo manjka!");
            if(psswd2.equals(""))
                throw new EmptyInputException("Ponovno geslo manjka!");
        } catch (EmptyInputException e) {
            exceptionInfo.setText(e.getMessage());
            return  false;
        }

        //preveri da se geslo in ponovno geslo ujemata
        try {
            if (!psswd.equals(psswd2))
                throw new WrongUsernameOrPasswordException("Gesli se ne ujemata!");
        } catch (WrongUsernameOrPasswordException e){
            exceptionInfo.setText(e.getMessage());
            return false;
        }

        //preveri geslo da je dovolj močno (vsaj 8 črk, ena velika začetnica in en poseben znak)
        try {
            if(!psswd.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}"))
                throw new WeakPasswordExeption("Geslo mora vsebovati eno številko, eno veliko črko in biti dolgo 8 znakov!");
        } catch (WeakPasswordExeption e){
            exceptionInfo.setText(e.getMessage());
            return  false;
        }

        //preveri da uporabniško ime ne obstaja v bazi
        try{
            String usernameDb = "";

            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = "select u.username\n" +
                        "from apl_user u\n" +
                        "where u.username like '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next())
                usernameDb = rs.getString("username");

            if(username.equals(usernameDb))
                throw new UsernameAlreadyExsistsException("Uporabniško ime že obstaja!");

        } catch (UsernameAlreadyExsistsException e){
            exceptionInfo.setText(e.getMessage());
            return false;
        } catch (SQLException e){
            exceptionInfo.setText(e.getMessage());
            return false;
        }

        return true;
    }

}
