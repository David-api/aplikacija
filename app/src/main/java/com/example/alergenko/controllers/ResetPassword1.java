package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.exceptions.VerificationCodeDoesNotExistInDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetPassword1 extends AppCompatActivity {

    // textView za prikaz podatkov o izjemah
    TextView txtException;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password1);

        // textView za prikaz podatkov o izjemah
        txtException = findViewById(R.id.txtException);

        // ob kliku na gum "NAZAJ" odpre ResetPassword screen
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openResetPassword1Activity();
            }
        });

        // ob kliku na gum "POTRDI" preveri če obstaja verifikacijska koda
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxCode = (EditText) findViewById(R.id.etxCode);

                try {
                    // preveri če verifikacijska koda obstaja v PB
                    if(checkIfVerificationCodeExists(etxCode.getText().toString())){
                        // če obstaja odpre okno ki omogoča reset gesla
                        openResetPassword2Activit();
                    } else{
                        // če ne obstaja se sproži izjema
                        throw new VerificationCodeDoesNotExistInDB("Napačna verifikacijska koda!");
                    }
                } catch (VerificationCodeDoesNotExistInDB e){
                    // prikaz podatkov o izjemi
                    txtException.setText(e.getMessage());
                }
            }
        });

    }

    public boolean checkIfVerificationCodeExists(String code){
        // pridobi verifikacijsko kodo iz PB
        String resault = "";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje verifikacijske kode
            String query = "select * from apl_verification\n" +
                    "where code = '" + code + "'";
            ResultSet rs = stmt.executeQuery(query);

            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                resault = rs.getString("code");
            }

            con.close();
            stmt.close();
            rs.close();
        }  catch (SQLException e){
            Log.e("ProductInfo SQLException", e.toString());
            txtException.setText("Napaka pri povezavi na podatkovno bazo!");
        } catch (Exception e){
            Log.e("ProductInfo Exception", e.toString());
            txtException.setText("Napaka pri povezavi na podatkovno bazo!");
        }

        //ce se verifikacijski kodi ujemata vrni true, v nasprotnem primeru pa false
        if(resault.equals(code) && resault != null)
            return true;
        else
            return false;
    }

    public void openResetPassword2Activit(){
        startActivity(new Intent(ResetPassword1.this, ResetPassword2.class));
    }

    public void openResetPassword1Activity(){
        startActivity(new Intent(ResetPassword1.this, ResetPassword.class));
    }
}
