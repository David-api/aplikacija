package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;

public class SettingsData extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_data);

        //dodajanje poslušalca ob kliku na gumb in kaj naj se zgodi ob kliku na gumb
        Button btnCancel = findViewById(R.id.btnCancel);
        View.OnClickListener listenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        };
        btnCancel.setOnClickListener(listenerCancel);
    }

    public void openSettingsActivity(){
        startActivity(new Intent(SettingsData.this, MainActivity.class));
    }
}
