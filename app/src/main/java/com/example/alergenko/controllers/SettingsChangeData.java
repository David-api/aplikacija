package com.example.alergenko.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.User;
import com.example.alergenko.exceptions.InputTooShortException;
import com.example.alergenko.exceptions.UsernameAlreadyExsistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingsChangeData extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_change_data);

        TextView txtInName = findViewById(R.id.txtInName);
        TextView txtInSurname = findViewById(R.id.txtInSurname);
        TextView txtInEmail = findViewById(R.id.txtInEmail);
        TextView txtInUsername = findViewById(R.id.txtInUsername);
        TextView txtInPhoneNum = findViewById(R.id.txtInPhoneNum);
        TextView exceptionInfo = findViewById(R.id.txtVException);

        //vnosna polja napolni s trenutnimi podatki
        txtInName.setText(User.name);
        txtInSurname.setText(User.surname);
        txtInEmail.setText(User.gmail);
        txtInUsername.setText(User.username);
        txtInPhoneNum.setText(User.phoneNumber);


        //ob kliku na gumb se izvede sprememba podatkov v podatkovni bazi
        Button btnChange = findViewById(R.id.btnConfirm);
        View.OnClickListener listenerChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData();
            }
        };
        btnChange.setOnClickListener(listenerChange);

        //ob kliku na gumb odpre aktivnost, ki prikaze osnovne podatke o uporabniku (SettingsData)
        Button btnCancel = findViewById(R.id.btnCancel);
        View.OnClickListener listenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsDataActivity();
            }
        };
        btnCancel.setOnClickListener(listenerCancel);

    }

    public void changeData(){
        //posodobi podatke v PB
        TextView txtInName = findViewById(R.id.txtInName);
        TextView txtInSurname = findViewById(R.id.txtInSurname);
        TextView txtInEmail = findViewById(R.id.txtInEmail);
        TextView txtInUsername = findViewById(R.id.txtInUsername);
        TextView txtInPhoneNum = findViewById(R.id.txtInPhoneNum);
        TextView exceptionInfo = findViewById(R.id.txtVException);

        //pridobivanje podatkov iz vnosnih polj
        String name = (txtInName.getText().toString() != null) ? txtInName.getText().toString() : "";
        String surname = (txtInSurname.getText().toString() != null) ? txtInSurname.getText().toString() : "";
        String email = (txtInEmail.getText().toString() != null) ? txtInEmail.getText().toString() : "";
        String username = (txtInUsername.getText().toString() != null) ? txtInUsername.getText().toString() : "";
        String phoneNum = (txtInPhoneNum.getText().toString() != null) ? txtInPhoneNum.getText().toString() : "";

        //preveri da so vnosi pravilni (da niso prekratki)
        boolean nameLength = true;
        boolean surnameLength = true;
        boolean emailLength = true;
        boolean usernameLength = true;
        boolean phoneNumLength = true;
        try{
            //preveri da je dolzina imena vecja ali enaka 2
            if(name.length() < 2){
                nameLength = false;
                throw new InputTooShortException("Ime je prekratko!");
            }

            //preveri da je dolzina priimka vecja ali enaka 3
            if(surname.length() < 3){
                surnameLength = false;
                throw new InputTooShortException("Priimek je prekratek!");
            }

            //preveri da je dolzina emaila vecja ali enaka 8
            if(email.length() < 8){
                emailLength = false;
                throw new InputTooShortException("Email je prekratek!");
            }

            //preveri da je dolzina uporabniskega imena vecja ali enaka 3
            if(username.length() < 3){
                usernameLength = false;
                throw new InputTooShortException("Uporabniško ime je prekratko!");
            }

            //preveri da je dolzina imena vecja ali enaka 9
            if(phoneNum.length() < 9){
                phoneNumLength = false;
                throw new InputTooShortException("Telefonska številka ni prava!");
            }

            //posodobi podatke v bazi ce so vsi vnosi ustrezni
            if(nameLength && surnameLength && emailLength && usernameLength && phoneNumLength)
                submitData(name, surname, email, username, phoneNum, exceptionInfo);

        } catch (InputTooShortException e){
            exceptionInfo.setText(e.getMessage());
        }
    }

    public void submitData(String name, String surname, String email, String username, String phoneNum, TextView exceptionInfo){
        //posodabljanje osnovnih podatkov o uporabniku
        try {
            //vzpostavitev povezave na bazo
            Connection con = DBConnection.getConnection();
            String query = "";
            PreparedStatement pstmt;

            //posodobi ime, če je vnešena druga vrednost
            if(!User.name.equals(name)){
                query = "update apl_user \n" +
                        "set name = '" + name + "'\n" +
                        "where id = " + User.id;
                pstmt = con.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
                User.name = name;
            }

            //posodobi priimek, če je vnešena druga vrednost
            if(!User.surname.equals(surname)){
                query = "update apl_user \n" +
                        "set surname = '" + surname + "'\n" +
                        "where id = " + User.id;
                pstmt = con.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
                User.surname = surname;
            }

            //posodobi gmail, če je vnešena druga vrednost
            if(!User.gmail.equals(email)){
                query = "update apl_user \n" +
                        "set gmail = '" + email + "'\n" +
                        "where id = " + User.id;
                pstmt = con.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
                User.gmail = email;
            }

            //posodobi uporabniško ime, če je vnešena druga vrednost
            if(!User.username.equals(username)){
                if(checkUsername(username, exceptionInfo)){
                    query = "update apl_user \n" +
                            "set username = '" + username + "'\n" +
                            "where id =" + User.id;
                    pstmt = con.prepareStatement(query);
                    pstmt.executeUpdate();
                    pstmt.close();
                    User.username = username;
                }
            }

            //posodobi telefonsko številko, če je vnešena druga vrednost
            if(!User.phoneNumber.equals(phoneNum)){
                query = "update apl_user \n" +
                        "set phoneNumber = '" + phoneNum + "'\n" +
                        "where id =" + User.id;
                pstmt = con.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
                User.phoneNumber = phoneNum;
            }

            con.commit();
            con.close();
            successUpdate();
        } catch (SQLException e){
            Log.e("error","Napaka v SettingsChangeData razredu, v metodi changeData");
            Log.e("error", e.toString());
            unsuccessUpdate(e.getMessage());
        }catch (Exception e){
            System.out.println("Napaka v SettingsChangeData razredu, v metodi changeData");
            Log.e("error", e.toString());
            unsuccessUpdate(e.getMessage());
        }
    }

    public boolean checkUsername(String username, TextView exceptionInfo){
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

    public void successUpdate(){
        //sporocilo da je bila posodobitev podatkov uspešna
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Posodobitev podatkov uspešna!");

        builder1.setPositiveButton(
                "Vredu",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SettingsChangeData.this, SettingsData.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void unsuccessUpdate(String message){
        //sporocilo da je bila posodobitev podatkov neuspešna
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Posodobitev podatkov neuspešna!\nRazlog: " + message);

        builder1.setPositiveButton(
                "Poizkusite ponovno",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SettingsChangeData.this, SettingsChangeData.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void openSettingsDataActivity(){
        startActivity(new Intent(SettingsChangeData.this, SettingsData.class));
    }
}
