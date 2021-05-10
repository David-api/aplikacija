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
import com.example.alergenko.entities.User;
import com.example.alergenko.exceptions.PasswordsAreNotEqualException;
import com.example.alergenko.exceptions.WeakPasswordExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetPassword2 extends AppCompatActivity {

    // prikaz informacij o izjemah
    TextView txtException;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_passwprd2);

        // prikaz informacij o izjemah
        txtException = (TextView) findViewById(R.id.txtException);

        // ob kliku na gumb "PREKLIČI" se odpre okno za prijavo
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openLogInActivity();
            }
        });

        // ob kliku na gumb "SPREMENI" spremeni gleso v PB in odpre prijavni zaslov
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
                EditText editTextTextPassword2 = (EditText) findViewById(R.id.editTextTextPassword2);

                try {
                    // če geslo zadostuje kriterijem za varno geslo gre naprej
                    if (editTextTextPassword.getText().toString().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}")) {
                        try {
                            // ce se geslo ustreza kriterijem in je enako kot ponovno geslo spremeni geslo
                            if (editTextTextPassword.getText().toString().equals(editTextTextPassword2.getText().toString())) {
                                // spremeni geslo
                                changePassword(editTextTextPassword.getText().toString(), User.phoneNumber);
                                // odpre prijano okno
                                openLogInActivity();
                            } else { // ce ne pa obvesti uporabnika o izjemi
                                throw new PasswordsAreNotEqualException("Gesli se ne ujemata!");
                            }
                        } catch (PasswordsAreNotEqualException e) {
                            txtException.setText(e.getMessage());
                        } catch (Exception e) {
                            txtException.setText("Napaka: " + e.getMessage());
                            Log.e("ERROR", e.toString());
                        }
                    } else { // če ne pa vrže izjemo
                        throw new WeakPasswordExeption("Geslo mora vsebovati eno številko, eno veliko črko in biti dolgo 8 znakov!");
                    }
                } catch (WeakPasswordExeption e) {
                    txtException.setText(e.getMessage());
                }
            }

        });

    }

    // spremeni geslo uporabnika
    public void changePassword(String password, String phoneNumber){
        try {
            //povezava na bazo
            Connection con = DBConnection.getConnection();
            String query = "update apl_user\n" +
                    "set password = '" + password + "'\n" +
                    "where phonenumber = '" + phoneNumber + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();

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

    public void openLogInActivity(){
        startActivity(new Intent(ResetPassword2.this, Login.class));
    }

}
