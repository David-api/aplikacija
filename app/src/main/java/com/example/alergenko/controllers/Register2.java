package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;

public class Register2 extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
    }

    public void openRegister1Activity(View v){
        startActivity(new Intent(Register2.this, Register1.class));
    }
}
