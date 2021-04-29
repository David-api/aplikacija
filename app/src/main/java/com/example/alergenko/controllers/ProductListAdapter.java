package com.example.alergenko.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alergenko.R;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter<P> extends ArrayAdapter<Product> implements Filterable {

    private static final String TAG = "ProductListAdapter";

    private Context context;
    private int resource;

    public ProductListAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
     }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.txtVProductName);
        TextView tvAllergens = (TextView) convertView.findViewById(R.id.txtVAllergens);
        TextView tvVerdict = (TextView) convertView.findViewById(R.id.txtVVerdict);
        ImageView imgVImage = (ImageView) convertView.findViewById(R.id.imgVImage);

        //ime izdelka
        String name = (getItem(position).getName() == null) ? "" : getItem(position).getName();
        //alergeni v izdelku
        ArrayList<Allergens> allergens = getItem(position).getAllergens();
        //izpis alergenov
        String allergensString = "";
        //ugotovitev ali je izdelek primeren za uporabnika
        String verdict = "";
        //slika izdelka
        byte [] picture = getItem(position).getPicture();

        //lepljenje nizov
        for(int i = 0; i < allergens.size(); i++){
            if(i != allergens.size() - 1){
                allergensString += allergens.get(i).getName() + ", ";
            } else {
                allergensString += allergens.get(i).getName();
            }
        }

        //ce izdelek ne vsebuje alergenov obarva napis zeleno
        if(allergensString.contains(Allergens.NULL.getName()))
            tvAllergens.setTextColor(convertView.getResources().getColor(R.color.primaryGreen, null));
        else
            tvVerdict.setTextColor(convertView.getResources().getColor(R.color.red, null));

        //ugotavljanje ali je izdelek primeren za uporabnika
        for(int i = 0; i < allergens.size(); i++){
            if(User.allergens.contains(allergens.get(i))){
                verdict = "Izdelek ni primeren za zaužitje.";
                tvVerdict.setTextColor(convertView.getResources().getColor(R.color.red, null));
                break;
            } else {
                verdict = "Izdelek lahko zaužijete.";
                tvVerdict.setTextColor(convertView.getResources().getColor(R.color.primaryGreen, null));
            }
        }



        //prikaz podatkov
        tvName.setText(name);
        tvAllergens.setText(allergensString.toUpperCase());
        tvVerdict.setText(verdict);
        imgVImage.setImageBitmap(getBitmapFromByteArr(picture));

        return convertView;
    }

    //za vstavljanje slike
    public Bitmap getBitmapFromByteArr(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }
}
