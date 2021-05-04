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

public class ProductInfo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);

        // deklaracija in inicializacija pogledov
        TextView txtvProductName = (TextView)findViewById(R.id.txtvProductName);
        TextView txtvAllergenes = (TextView)findViewById(R.id.txtvAllergenes);
        TextView txtvIngredients = (TextView)findViewById(R.id.txtvIngredients);
        ImageView imgvProductImage = (ImageView)findViewById(R.id.imgvProductImage);

        // pridobi crno kodo iz intenta
        String barcode = getBarcodeFromIntent();
        // pridobipodatke o izdečku na podlagi crtne kode
        Product scanedProduct = getProduct(barcode, txtvAllergenes);
        // doda skeniran izdelek k zgodovini
        User.history.add(0, scanedProduct);
        addProductToHistory(scanedProduct);


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

    //doda skeniran izdelek v tabelo zgodovina
    public void addProductToHistory(Product product){
        try {
            Connection con = DBConnection.getConnection();

            //dodajanje izdelka v tabelo zgodovina
            String query = "insert into apl_history (id, product_id, user_id, scandate)\n" +
                    "values(APL_HISTORY_ID_SQ.NEXTVAL, " + product.getId() + ", " + User.id + ", sysdate)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();

            con.commit();
            con.close();
            pstmt.close();
        } catch (SQLException e){
            Log.e("ProductInfo SQLException", e.toString());
        } catch (Exception e){
            Log.e("ProductInfo Exception", e.toString());
        }
    }

    //prdobi podatke o skeniranem izdelku
    public Product getProduct(String barcode, TextView txtvAllergenes){
        int id = 0;
        String brandName = "";
        String name = "";
        String shortName = "";
        String allergen = "";
        ArrayList<Allergens> allergens = new ArrayList<Allergens>();
        byte [] picture = null;
        String ingredinets = "";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje podatkov o izdelku po crtni kodi
            String query = "select b.barcode, p.id, br.name as znamka, p.name as izdelek, p.shortName, p.picture, a.name as alergen, ai.name as sestavine\n" +
                    "from apl_allergen a\n" +
                    "    right join  apl_alr_prdct ap\n" +
                    "        on a.id = ap.allergen_id\n" +
                    "    right join  apl_product p\n" +
                    "        on p.id = ap.product_id\n" +
                    "    right join apl_barcode b\n" +
                    "        on p.id = b.product_id\n" +
                    "    right join apl_brand br\n" +
                    "        on p.brand_id = br.id\n" +
                    "    left join apl_ing_prdct aip\n" +
                    "        on aip.product_id = p.id\n" +
                    "    left join apl_ingredient ai\n" +
                    "        on aip.ingredient_id = ai.id\n" +
                    "where b.barcode = '" + barcode + "'";
            ResultSet rs = stmt.executeQuery(query);

            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                id = rs.getInt("id");
                brandName = (rs.getString("znamka") == null) ? "" : rs.getString("znamka");
                barcode = (rs.getString("barcode") == null) ? "" : rs.getString("barcode");
                name = (rs.getString("izdelek")  == null) ? "" : rs.getString("izdelek");
                shortName = (rs.getString("shortName")  == null) ? "" : rs.getString("shortName");
                picture = (rs.getBytes("picture") == null) ? new byte[]{} : rs.getBytes("picture");
                ingredinets = (rs.getString("sestavine")  == null) ? "Ni podatka o sestavinah" : rs.getString("sestavine");
                allergen = (rs.getString("alergen") == null) ? "null" : rs.getString("alergen");
                allergens.add(Allergens.valueOf(allergen.toUpperCase().replace(' ', '_').trim()));
            }

            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException e){
            txtvAllergenes.setText("Napaka pri pridobivanju podatkov iz podatkovne baze!");
            Log.e("ProductInfo SQLException", e.toString());
        } catch (Exception e){
            txtvAllergenes.setText(e.getMessage());
            Log.e("ProductInfo Exception", e.toString());
        }
        return new Product(id, barcode, brandName, name, shortName, allergens, picture, ingredinets);
    }

    //pridobi sliko iz baze
    public Bitmap getBitmapFromBLOB(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }

    public String getBarcodeFromIntent(){
        //pridobi črtno kodo iz intenta
        Intent i= getIntent();
        Bundle b = i.getExtras();
        String barcode = "";

        if(b!=null)
            barcode =(String) b.get("BARCODE");

        return barcode;
    }

    public void openMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
