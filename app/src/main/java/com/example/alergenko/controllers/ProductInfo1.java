package com.example.alergenko.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;
import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductInfo1 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info1);

        // deklaracija in inicializacija pogledov
        TextView txtvProductName = (TextView)findViewById(R.id.txtvProductName);
        TextView txtvAllergenes = (TextView)findViewById(R.id.txtvAllergenes);
        TextView txtvIngredients = (TextView)findViewById(R.id.txtvIngredients);
        ImageView imgvProductImage = (ImageView)findViewById(R.id.imgvProductImage);


        // pridobi pozicijo izdelka iz intenta
        String productPos = getProductPosFromIntent();
        // vazame izdelek iz array lista kjer so vsi izdelki ki jih je uporabnik ze poskeniral
        Product scanedProduct = User.history.get(Integer.parseInt(productPos));

        //prikaz podatkov o izdelku
        txtvAllergenes.setText(formatAllergens(scanedProduct));
        txtvProductName.setText(scanedProduct.getName());
        txtvIngredients.setText(scanedProduct.getIngredients());
        imgvProductImage.setImageBitmap(getBitmapFromBLOB(scanedProduct.getPicture()));

        //odpre glavno aktivnost ob kliku an gumb
        Button btnClose = findViewById(R.id.btnClose);
        View.OnClickListener listenerClose = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        };
        btnClose.setOnClickListener(listenerClose);
    }

    //fornatira alergene
    public String formatAllergens(Product product) {
        String formattedAllergens = "";

        for (int i = 0; i < product.getAllergens().size(); i++) {
            if (i != product.getAllergens().size() - 1) {
                formattedAllergens += product.getAllergens().get(i).getName() + ", ";
            } else {
                formattedAllergens += product.getAllergens().get(i).getName();
            }
        }
        return formattedAllergens.toUpperCase();
    }

    //pridobi sliko iz baze
    public Bitmap getBitmapFromBLOB(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }

    public String getProductPosFromIntent(){
        //pridobi črtno kodo iz intenta
        Intent i= getIntent();
        Bundle b = i.getExtras();
        String position = "";

        if(b!=null)
            position = "" + b.get("PRODUCT_POS");

        return position;
    }

    public void openMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
