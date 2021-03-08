package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.alergenko.R;
import com.example.alergenko.entities.Allergen;

public class Register2 extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
    }

    public void openRegister1Activity(View v){
        startActivity(new Intent(Register2.this, Register1.class));
    }

    public void register2(View v){

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


    }
}
