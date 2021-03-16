package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.entities.User;

public class SettingsData extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_data);

        TextView txtvName = findViewById(R.id.txtvName);
        TextView txtvSurname = findViewById(R.id.txtvSurname);
        TextView txtvGmail = findViewById(R.id.txtvGmail);
        TextView txtvUsername = findViewById(R.id.txtvUsername);
        TextView txtvPhoneNumber = findViewById(R.id.txtvPhoneNumber);

        //prikaz podatkov o uporabniku
        txtvName.setText(Html.fromHtml("<b>Ime:</b> " + User.name));
        txtvSurname.setText(Html.fromHtml("<b>Priimek:</b> " + User.surname));
        txtvGmail.setText(Html.fromHtml("<b>Epošta:</b> " + User.gmail));
        txtvUsername.setText(Html.fromHtml("<b>Uporabniško ime:</b> " + User.username));
        txtvPhoneNumber.setText(Html.fromHtml("<b>Telefonska številka:</b> " + User.phoneNumber));

        //ob kliku na gumb odpre glavno aktivnost (MainActivity)
        Button btnCancel = findViewById(R.id.btnCancel);
        View.OnClickListener listenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        };
        btnCancel.setOnClickListener(listenerCancel);

        //ob kliku na gumb odpre aktivnost za spreminjanje podatkov (SettingsChangeData)
        Button btnChange = findViewById(R.id.btnConfirm);
        View.OnClickListener listenerChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsChangeDataActivity();
            }
        };
        btnChange.setOnClickListener(listenerChange);
    }

    public void openMainActivity(){
        startActivity(new Intent(SettingsData.this, MainActivity.class));
    }

    public void openSettingsChangeDataActivity(){
        startActivity(new Intent(SettingsData.this, SettingsChangeData.class));
    }
}
