package com.example.alergenko.controllers;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.User;
import com.example.alergenko.exceptions.PhoneNumberDoesNotExistInDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ResetPassword extends AppCompatActivity {

    // prikaz informacij o izjemah
    TextView txtException;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // prikaz informacij o izjemah
        txtException = (TextView) findViewById(R.id.txtException);

        // ob kliku na gumb "NAZAJ" se odpre login screen
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        // ob kliku na gumb "POŠLJI" pošlje sms s kodo za reset na številko, če ta obstaja v PB
        Button btnSend = (Button) findViewById(R.id.btnConfirm1);
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText etxPhoneNumber = (EditText) findViewById(R.id.etxPhoneNumber);

                try {
                    // preveri če telefonska številka obstaja v bazi
                    if(checkIfPhoneNumberExistsInDb(etxPhoneNumber.getText().toString())){
                        // shrani telefosko številko v class User
                        User.phoneNumber = etxPhoneNumber.getText().toString();
                        // generira verifikacijsko kodo
                        int verificationCode = generateVerificationCode();
                        // vstavi verifikacijsko kodo v DB
                        insertVerificationCodeInDB(verificationCode);
                        // pošlje sms s potrditveno kodo in odpre naslednji korak za reset gesla
                        sendMessageWithCode(etxPhoneNumber.getText().toString(), verificationCode);
                    } else {
                        // throwa exception
                        throw new PhoneNumberDoesNotExistInDB("Napačna telefonska številka!");
                    }
                } catch (PhoneNumberDoesNotExistInDB e){
                    txtException.setText(e.getMessage());
                    Log.e("ERROR", e.toString());
                }

            }
        });

    }

    public boolean checkIfPhoneNumberExistsInDb(String phoneNumber){
        // pridobi telefonsko številko iz PB
        String resault = "";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje telefonske številke
            String query = "select phonenumber from apl_user\n" +
                    "where phonenumber = '" + phoneNumber + "'";
            ResultSet rs = stmt.executeQuery(query);

            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                resault = rs.getString("phonenumber");
            }
            resault = "0" + resault;

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

        //ce je rezultat ustrezen vrni true, v nasprotnem primeru pa false
        if(resault.equals(phoneNumber) && resault != null)
            return true;
        else
            return false;

    }

    public int generateVerificationCode(){
        return new Random().nextInt(900000) + 100000;
    }

    public void insertVerificationCodeInDB(int verificationCode){
        // v PB vstavi generirano verifikacijsko kodo, ki se bo v PB hranila 5 min
        try {
            Connection con = DBConnection.getConnection();
            // vstavljanje verifikacijske kode
            String query = "insert into apl_verification(code, inserted)\n" +
                    "values (" + verificationCode + ", sysdate)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.execute();

            con.commit();
            con.close();
            pstmt.close();
        } catch (SQLException e){
            txtException.setText("Napaka: " + e.getMessage());
            Log.e("ERROR", e.toString());
        }catch (Exception e){
            txtException.setText("Napaka: " + e.getMessage());
            Log.e("ERROR", e.toString());
        }
    }

    public void sendMessageWithCode(String phoneNumber, int verificationCode){
        // pošlje sms z verifikacijsko kodo
        String smsText = "Vaša verifikacijska koda je " + verificationCode + "\n\nLep pozdrav\naplikacija Alergenko";


        //Getting intent and PendingIntent instance
        Intent intent=new Intent(ResetPassword.this, ResetPassword1.class);
        PendingIntent pi=PendingIntent.getActivity(ResetPassword.this, 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, smsText, pi,null);
    }

    public void openLoginActivity(){
        startActivity(new Intent(ResetPassword.this, Login.class));
    }
}
