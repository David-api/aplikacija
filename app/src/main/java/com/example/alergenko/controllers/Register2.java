package com.example.alergenko.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Register2 extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
    }

    public void openRegister1Activity(View v){
        startActivity(new Intent(Register2.this, Register1.class));
    }

    public void register2(View v){

        SwitchCompat swJajca = findViewById(R.id.swJajca);
        SwitchCompat swOrescki = findViewById(R.id.swOrescki);
        SwitchCompat swGluten = findViewById(R.id.swGluten);
        SwitchCompat swMleko = findViewById(R.id.swMleko);
        SwitchCompat swSoja = findViewById(R.id.swSoja);
        SwitchCompat swArasidi = findViewById(R.id.swArasidi);
        SwitchCompat swZelena = findViewById(R.id.swZelena);
        SwitchCompat swRibe = findViewById(R.id.swRibe);
        SwitchCompat swRaki = findViewById(R.id.swRaki);
        SwitchCompat swGorcicnoSeme = findViewById(R.id.swGorcicnoSeme);
        SwitchCompat swSezam = findViewById(R.id.swSezam);
        SwitchCompat swZveplo = findViewById(R.id.swZveplo);
        SwitchCompat swVolcjiBob = findViewById(R.id.swVolcjiBob);
        SwitchCompat swMehkuzci = findViewById(R.id.swMehkuzci);

        //zbiranje alergenov, ki ji je uporabnik izbral
        ArrayList<Allergens> allergensRegister = new ArrayList<Allergens>();
        if(swJajca.isChecked())
            allergensRegister.add(Allergens.JAJCA);
        if(swOrescki.isChecked())
            allergensRegister.add(Allergens.ORESCKI);
        if(swGluten.isChecked())
            allergensRegister.add(Allergens.GLUTEN);
        if(swMleko.isChecked())
            allergensRegister.add(Allergens.MLEKO);
        if(swSoja.isChecked())
            allergensRegister.add(Allergens.SOJA);
        if(swArasidi.isChecked())
            allergensRegister.add(Allergens.ARASIDI);
        if(swZelena.isChecked())
            allergensRegister.add(Allergens.ZELENE);
        if(swRibe.isChecked())
            allergensRegister.add(Allergens.RIBE);
        if(swRaki.isChecked())
            allergensRegister.add(Allergens.RAKI);
        if(swGorcicnoSeme.isChecked())
            allergensRegister.add(Allergens.GORCICNO_SEME);
        if(swSezam.isChecked())
            allergensRegister.add(Allergens.SEZAM);
        if(swZveplo.isChecked())
            allergensRegister.add(Allergens.ZVEPLO);
        if(swVolcjiBob.isChecked())
            allergensRegister.add(Allergens.VOLCJI_BOB);
        if(swMehkuzci.isChecked())
            allergensRegister.add(Allergens.MEHKUZCI);


        //dodajanje uporabnika v podatkovno bazo in povezava z alergeni na katere je alergicen
        try {
            Connection con = DBConnection.getConnection();
            //dodajanje uporabnika v podatkovno bazo
            String query = "insert into apl_user(id, name, surname, gmail, username, phonenumber, password)\n" +
                    "values (apl_user_id_seq.nextval, '" + User.name + "', '" + User.surname + "', '" + User.gmail + "', '" + User.username + "', '" + User.phoneNumber + "', '" + User.password + "')";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.execute();

            //povezava z alergeni na katere uporabnik je alergicen
            int userId = User.getUserId(User.username);
            for(int i = 0; i < allergensRegister.size(); i++){
                query = "insert into apl_user_allergen (user_id, allergen_id)\n" +
                        "values(" + userId + ", " + allergensRegister.get(i).getId() + ")";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
            }

            con.close();
            pstmt.close();

            successRegister();
        } catch (SQLException e){
            System.out.println("Napaka v Register2 razredu, v metodi register2");
            e.printStackTrace();
            unsuccessRegister(e.getMessage());
        }catch (Exception e){
            System.out.println("Napaka v Register2 razredu, v metodi register2");
            e.printStackTrace();
            unsuccessRegister(e.getMessage());
        }
    }

    public void successRegister(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Registracija uspešna, vrnite se na prijavno okno!");

        builder1.setPositiveButton(
                "Prijavno okno",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Register2.this, Login.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void unsuccessRegister(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Registracija neuspešna!\nRazlog: " + message);

        builder1.setPositiveButton(
                "Poizkusite ponovno",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Register2.this, Register1.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
