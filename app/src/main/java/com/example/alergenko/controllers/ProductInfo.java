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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductInfo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);

        TextView txtvProductName = (TextView)findViewById(R.id.txtvProductName);
        TextView txtvAllergenes = (TextView)findViewById(R.id.txtvAllergenes);
        ImageView imgvProductImage = (ImageView)findViewById(R.id.imgvProductImage);

        //pridobi črtno kodo iz intenta
        Intent i= getIntent();
        Bundle b = i.getExtras();
        String barcode = "ni kode";
        if(b!=null){
           barcode =(String) b.get("BARCODE");
        }

        String productName = "";
        String allergens = "";
        String picture = "";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje izdelka, alergenov in slike po barkodah
            String query = "select b.barcode, br.name as znamka, p.name as izdelek, p.pictureUrl as url, a.name as alergen\n" +
                    "from apl_allergen a\n" +
                    "    right join  apl_alr_prdct ap\n" +
                    "        on a.id = ap.allergen_id\n" +
                    "    right join  apl_product p\n" +
                    "        on p.id = ap.product_id\n" +
                    "    right join apl_barcode b\n" +
                    "        on p.id = b.product_id\n" +
                    "    right join apl_brand br\n" +
                    "        on p.brand_id = br.id\n" +
                    "where b.barcode = '" + barcode + "'";
            ResultSet rs = stmt.executeQuery(query);

            //TODO: dodaj da pobere sliko iz baze in ne iz interneta
            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                productName = rs.getString("izdelek");
                picture = rs.getString("url");

                //ce izdelek ne vsebuje alergenov izpise "Izdelek ne vsebuje alergenov."
                if (rs.getString("alergen") == null)
                    allergens = "Izdelek ne vsebuje alergenov.";
                else
                    allergens += rs.getString("alergen").toUpperCase() + ", ";

             }

            con.close();
            stmt.close();
            rs.close();
        }  catch (SQLException e){
            txtvAllergenes.setText("Napaka pri pridobivanju podatkov iz podatkovne baze!");
            Log.e("ProductInfo SQLException", e.toString());
        } catch (Exception e){
            txtvAllergenes.setText(e.getMessage());
            Log.e("ProductInfo Exception", e.toString());
        }

        //prikaz podatkov v aplikaciji
        txtvAllergenes.setText(allergens.substring(0, allergens.length() - 2));
        txtvProductName.setText(productName);
        imgvProductImage.setImageBitmap(getBitmapFromURL(picture));

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

    //pridobi sliko iz interneta
    public Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.toString());
            return null;
        }
    }

    public void openMainActivity(){
        startActivity(new Intent(this, MainActivity.class
        ));
    }
}
