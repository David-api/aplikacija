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
import com.example.alergenko.entities.Allergen;
import com.example.alergenko.entities.User;
import com.example.alergenko.exceptions.UsernameAlreadyExsistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        //deklaracija in inicializacija vseh možnih alergenov
        Allergen []allergens = {
                new Allergen(70, "jajca"),
                new Allergen(71, "orescki"),
                new Allergen(72, "gluten"),
                new Allergen(73, "mleko"),
                new Allergen(74, "soja"),
                new Allergen(75, "arasidi"),
                new Allergen(76, "zelena"),
                new Allergen(77, "ribe"),
                new Allergen(78, "raki"),
                new Allergen(79, "gorcicno seme"),
                new Allergen(80, "sezam"),
                new Allergen(81, "so2"),
                new Allergen(82, "volcji bob"),
                new Allergen(83, "mehkuzci"),
        };

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
        ArrayList<Allergen> allergensRegister = new ArrayList<Allergen>();
        if(swJajca.isChecked())
            allergensRegister.add(allergens[0]);
        if(swOrescki.isChecked())
            allergensRegister.add(allergens[1]);
        if(swGluten.isChecked())
            allergensRegister.add(allergens[2]);
        if(swMleko.isChecked())
            allergensRegister.add(allergens[3]);
        if(swSoja.isChecked())
            allergensRegister.add(allergens[4]);
        if(swArasidi.isChecked())
            allergensRegister.add(allergens[5]);
        if(swZelena.isChecked())
            allergensRegister.add(allergens[6]);
        if(swRibe.isChecked())
            allergensRegister.add(allergens[7]);
        if(swRaki.isChecked())
            allergensRegister.add(allergens[8]);
        if(swGorcicnoSeme.isChecked())
            allergensRegister.add(allergens[9]);
        if(swSezam.isChecked())
            allergensRegister.add(allergens[10]);
        if(swZveplo.isChecked())
            allergensRegister.add(allergens[11]);
        if(swVolcjiBob.isChecked())
            allergensRegister.add(allergens[12]);
        if(swMehkuzci.isChecked())
            allergensRegister.add(allergens[13]);


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
        builder1.setMessage("Registracija neuspešna!\n Razlog: " + message);

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
