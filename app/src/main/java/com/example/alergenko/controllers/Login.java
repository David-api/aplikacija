package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.R;

import com.example.alergenko.connection.DBConnection;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.Product;
import com.example.alergenko.exceptions.EmptyInputException;
import com.example.alergenko.exceptions.WrongUsernameOrPasswordException;
import com.example.alergenko.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class Login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //ob kliku na gumb prijava se izvede prijava
        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // ob kliku na text "ste pozabili geslo" se odpre postopek za spreminjanje gesla
        TextView txtForgottPsswd = (TextView) findViewById(R.id.txtForgottPsswd);
        txtForgottPsswd.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                openResetPsswdActivity();
            }
        });
    }

    public void login(){
        //ponastavi vse podatke o uporabniku
        reserUser();

        //inicializacija in deklaracija vsega potrebnega
        TextView exceptionInfo = (TextView)findViewById(R.id.txtInfo);
        EditText txtInUsername= (EditText)findViewById(R.id.txtInUsername);
        EditText psswdInPsswd = (EditText)findViewById(R.id.psswdInPassword1);
        String usernameIn = "";
        String passwordIn = "";

        try {
            //pridovivanje podatkov iz vnosov
            usernameIn = (txtInUsername.getText().toString() != null) ? txtInUsername.getText().toString() : "";
            passwordIn = (psswdInPsswd.getText().toString() != null) ? psswdInPsswd.getText().toString() : "";

            //preverjanje da niso vnosna polja prazna
            if(usernameIn.equals(""))
                throw new EmptyInputException("Uporabniško ime manjka!");
            if(passwordIn.equals(""))
                throw new EmptyInputException("Geslo manjka!");

            //metoda za pridobivanje podatkov uporabnika, ki se zeli prijaviti
            //podatki se zapisejo v class User kot staticne spremenljivke
            getDataFromDB(usernameIn, passwordIn, exceptionInfo);

            //metoda za preverjane vnesenih podatkov
            checkLogin(usernameIn, User.username, passwordIn, User.password, exceptionInfo);

        } catch (EmptyInputException e){  //ravnanje z izjemami pri vnosu
            exceptionInfo.setText(e.getMessage());
        } catch (Exception e){
            exceptionInfo.setText("Napaka!");
            e.printStackTrace();
        }
    }

    public void getDataFromDB(String usernameIn, String passwordIn, TextView exceptionInfo){
        //pridobivanje podatkov uporabnika, ki se zeli prijaviti, iz baze
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //pridobivanje osnovnih podatkov
            String query = "select * \n" +
                    "from apl_user \n" +
                    "where username like '" + usernameIn + "' and password = '" + passwordIn + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User.id = rs.getInt("id");
                User.name = rs.getString("name");
                User.surname = rs.getString("surname");
                User.gmail = rs.getString("gmail");
                User.username = rs.getString("username");
                User.phoneNumber = rs.getString("phoneNumber");
                User.password = rs.getString("password");
            }

            //pridobivanje podatkov o allergenih
            query = "select ua.allergen_id, a.name\n" +
                    "from apl_user_allergen ua\n" +
                    "    inner join apl_user u\n" +
                    "        on u.id = ua.user_id\n" +
                    "    inner join apl_allergen a\n" +
                    "        on ua.allergen_id = a.id\n" +
                    "where u.id = " + User.id;
            rs = stmt.executeQuery(query);

            while (rs.next())
                User.allergens.add(Allergens.valueOf(rs.getString("name").toUpperCase().replace(' ', '_').trim()));

            con.close();
            stmt.close();
            rs.close();

        }  catch (SQLException e){
            Log.e("error", e.toString());
            exceptionInfo.setText("Napaka pri pridobivanju podatkov iz podatkovne baze!");
        } catch (Exception e){
            Log.e("error", e.toString());
            exceptionInfo.setText(e.getMessage());
        }

        //pridobivanje podatkov o zgodovini skeniranih izdelkov
        User.history.addAll(getUserHistory(User.id));
    }

    public void checkLogin(String usernameIn, String usernameDB, String passwordIn, String passwordDB, TextView exceptionInfo){
        //preverjanje ali so vneseni podatki pravilni
        try {
            if (usernameIn.equals(usernameDB) && passwordIn.equals(passwordDB))
                startActivity(new Intent(Login.this, MainActivity.class));
            else
                throw new WrongUsernameOrPasswordException("Napačno uporabniško ime ali geslo!");
        } catch (WrongUsernameOrPasswordException e) { //ravnanje z izjemami pri napacnem vnosu
            exceptionInfo.setText(e.getMessage());
        }
    }

    //pridobi podatke o skeniranih izdelkih uporabnika, ki se želi prijaviti
    public ArrayList<Product> getUserHistory(int id){
        ArrayList<Product> prdct = new ArrayList<Product>();
        ArrayList<Integer> ids = new ArrayList<Integer>();

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje izdelkov, ki jih je uporabnik skeniral
            String query = "select * from apl_history\n" +
                    "where user_id = " + id + "\n" +
                    "order by scandate desc";
            ResultSet rs = stmt.executeQuery(query);

            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                ids.add(rs.getInt("product_id"));
            }

            con.close();
            stmt.close();
            rs.close();
        }  catch (SQLException e){
            Log.e("ProductInfo SQLException", e.toString());
            return prdct;
        } catch (Exception e){
            Log.e("ProductInfo Exception", e.toString());
            return prdct;
        }

        for(int i = 0; i < ids.size(); i++){
            prdct.add(getProduct(ids.get(i)));
        }

        return prdct;
    }

    public Product getProduct(int id){
        String brandName = "";
        String name = "";
        String shortName = "";
        String barcode = "";
        String allergen = "";
        ArrayList<Allergens> allergensTemp = new ArrayList<Allergens>();
        byte [] picture = null;
        String ingredinets = "";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            //iskanje potrebnih podatkov o izdelku po id izdelka
            String query = "select br.name as znamka, b.barcode, p.name as izdelek, p.shortName, p.picture, a.name as alergen, ai.name as sestavine\n" +
                    "    from apl_allergen a\n" +
                    "        right join  apl_alr_prdct ap\n" +
                    "            on a.id = ap.allergen_id\n" +
                    "        right join  apl_product p\n" +
                    "            on p.id = ap.product_id\n" +
                    "        right join apl_barcode b\n" +
                    "            on p.id = b.product_id\n" +
                    "        right join apl_brand br\n" +
                    "            on p.brand_id = br.id\n" +
                    "        left join apl_ing_prdct aip\n" +
                    "            on aip.product_id = p.id\n" +
                    "        left join apl_ingredient ai\n" +
                    "            on ai.id = aip.ingredient_id\n" +
                    "        where p.id = " + id;
            ResultSet rs = stmt.executeQuery(query);

            //shranjevanje podatkov iz baze v spremenljivke
            while (rs.next()) {
                brandName = (rs.getString("znamka") == null) ? "" : rs.getString("znamka");
                barcode = (rs.getString("barcode") == null) ? "" : rs.getString("barcode");
                name = (rs.getString("izdelek")  == null) ? "" : rs.getString("izdelek");
                shortName = (rs.getString("shortName")  == null) ? "" : rs.getString("shortName");
                picture = (rs.getBytes("picture") == null) ? new byte[]{} : rs.getBytes("picture");
                ingredinets = (rs.getString("sestavine")  == null) ? "Ni podatka o sestavinah" : rs.getString("sestavine");
                allergen = (rs.getString("alergen") == null) ? "null" : rs.getString("alergen");
                allergensTemp.add(Allergens.valueOf(allergen.toUpperCase().replace(' ', '_').trim()));
            }

            con.close();
            stmt.close();
            rs.close();
        }  catch (SQLException e){
            Log.e("ProductInfo SQLException", e.toString());
            return null;
        } catch (Exception e){
            Log.e("ProductInfo Exception", e.toString());
            return null;
        }

        //odstrani podvojene alergene
        ArrayList<Allergens> allergens = new ArrayList<Allergens>(new TreeSet<Allergens>(allergensTemp));

        return new Product(id, barcode, brandName, name, shortName, allergens, picture, ingredinets);
    }

    //spremeni vse podatke o uporabniku na default
    public void reserUser(){
        User.id = 0;
        User.name = "";
        User.surname = "";
        User.gmail = "";
        User.username = "";
        User.username = "";
        User.password = "";
        User.allergens.clear();
        User.history.clear();
    }


    //odpre 1. okno za registracijo
    public void openRegister1Activity(View v){
        startActivity(new Intent(Login.this, Register1.class));
    }

    //odpre okno za resrtiranje gesla
    public void openResetPsswdActivity(){
        startActivity(new Intent(Login.this, ResetPassword.class));
    }
}
