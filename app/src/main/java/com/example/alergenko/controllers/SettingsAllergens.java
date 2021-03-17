package com.example.alergenko.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SettingsAllergens extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_allergens);

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

        //oznaci vsa tista stikala, ki predstavljajo alergene na ktere je uporanik alergicen
        if(User.allergens.contains(Allergens.JAJCA))
            swJajca.setChecked(true);
        if(User.allergens.contains(Allergens.ORESCKI))
            swOrescki.setChecked(true);
        if(User.allergens.contains(Allergens.GLUTEN))
            swGluten.setChecked(true);
        if(User.allergens.contains(Allergens.MLEKO))
            swMleko.setChecked(true);
        if(User.allergens.contains(Allergens.SOJA))
            swSoja.setChecked(true);
        if(User.allergens.contains(Allergens.ARASIDI))
            swArasidi.setChecked(true);
        if(User.allergens.contains(Allergens.ZELENE))
            swZelena.setChecked(true);
        if(User.allergens.contains(Allergens.RIBE))
            swRibe.setChecked(true);
        if(User.allergens.contains(Allergens.RAKI))
            swRaki.setChecked(true);
        if(User.allergens.contains(Allergens.GORCICNO_SEME))
            swGorcicnoSeme.setChecked(true);
        if(User.allergens.contains(Allergens.SEZAM))
            swSezam.setChecked(true);
        if(User.allergens.contains(Allergens.ZVEPLO))
            swZveplo.setChecked(true);
        if(User.allergens.contains(Allergens.VOLCJI_BOB))
            swVolcjiBob.setChecked(true);
        if(User.allergens.contains(Allergens.MEHKUZCI))
            swMehkuzci.setChecked(true);


        //ob sliku na gumb se posodobijo podatki o alergenik na katere je uporabnik alergičen
        Button btnSumbit = findViewById(R.id.btnSubmit);
        View.OnClickListener listenerSumbit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sumbitData();
            }
        };
        btnSumbit.setOnClickListener(listenerSumbit);


        //ob kliku na gumb se odpre glavna aktivnost
        Button btnCancel = findViewById(R.id.btnCancel);
        View.OnClickListener listenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        };
        btnCancel.setOnClickListener(listenerCancel);
    }

    public void sumbitData(){
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
        ArrayList<Allergens> allergensUpdate = new ArrayList<Allergens>();
        if(swJajca.isChecked())
            allergensUpdate.add(Allergens.JAJCA);
        if(swOrescki.isChecked())
            allergensUpdate.add(Allergens.ORESCKI);
        if(swGluten.isChecked())
            allergensUpdate.add(Allergens.GLUTEN);
        if(swMleko.isChecked())
            allergensUpdate.add(Allergens.MLEKO);
        if(swSoja.isChecked())
            allergensUpdate.add(Allergens.SOJA);
        if(swArasidi.isChecked())
            allergensUpdate.add(Allergens.ARASIDI);
        if(swZelena.isChecked())
            allergensUpdate.add(Allergens.ZELENE);
        if(swRibe.isChecked())
            allergensUpdate.add(Allergens.RIBE);
        if(swRaki.isChecked())
            allergensUpdate.add(Allergens.RAKI);
        if(swGorcicnoSeme.isChecked())
            allergensUpdate.add(Allergens.GORCICNO_SEME);
        if(swSezam.isChecked())
            allergensUpdate.add(Allergens.SEZAM);
        if(swZveplo.isChecked())
            allergensUpdate.add(Allergens.ZVEPLO);
        if(swVolcjiBob.isChecked())
            allergensUpdate.add(Allergens.VOLCJI_BOB);
        if(swMehkuzci.isChecked())
            allergensUpdate.add(Allergens.MEHKUZCI);

        //posodobitev alergenov na katere je uporabnik alergičen
        try {
            //povezava na bazo
            Connection con = DBConnection.getConnection();
            //pobrise alergene na katere je uporabnik alergicen
            String query = "delete from apl_user_allergen\n" +
                    "where user_id = " + User.id;
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.execute();

            //na novo poveze uporabnika z alergeni na katere je alergicen
            for(int i = 0; i < allergensUpdate.size(); i++){
                query = "insert into apl_user_allergen (user_id, allergen_id)\n" +
                        "values(" + User.id + ", " + allergensUpdate.get(i).getId() + ")";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
                pstmt.close();
            }

            User.allergens = allergensUpdate;

            con.commit();
            con.close();
            successUpdate();
        } catch (SQLException e){
            System.out.println("Napaka v Register2 razredu, v metodi register2");
            e.printStackTrace();
            unsuccessUpdate(e.getMessage());
        }catch (Exception e){
            System.out.println("Napaka v Register2 razredu, v metodi register2");
            e.printStackTrace();
            unsuccessUpdate(e.getMessage());
        }
    }

    public void successUpdate(){
        //uporabnika obvesti o uspešni posodobitvi alergenov
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Posodobitev alergenov uspešna!");

        builder1.setPositiveButton(
                "Vredu",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SettingsAllergens.this, MainActivity.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void unsuccessUpdate(String message){
        //uporabnika obvesti o neuspešni posodobitvi alergenov
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Posodobitev alergenov ni bila uspešna!\nRazlog: " + message);

        builder1.setPositiveButton(
                "Poizkusite ponovno",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SettingsAllergens.this, SettingsAllergens.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void openSettingsActivity(){
        startActivity(new Intent(SettingsAllergens.this, MainActivity.class));
    }
}
