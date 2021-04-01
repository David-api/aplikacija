package com.example.alergenko.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alergenko.R;
import com.example.alergenko.entities.Allergens;
import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter<P> extends ArrayAdapter<Product> {

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

        for(int i = 0; i < allergens.size(); i++){
            //lepljenje nizov
            if(i != allergens.size() - 1){
                allergensString += allergens.get(i).getName() + ", ";
            } else {
                allergensString += allergens.get(i).getName();
            }
        }

        for(int i = 0; i < allergens.size(); i++){
            //ugotavljanje ali je izdelek primeren za uporabnika
            if(User.allergens.contains(allergens.get(i))){
                verdict = "Izdelek ni primeren za zaužitje.";
                break;
            } else {
                verdict = "Izdelek lahko zaužijete.";
            }
        }

        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.txtVProductName);
        TextView tvAllergens = (TextView) convertView.findViewById(R.id.txtVAllergens);
        TextView tvVerdict = (TextView) convertView.findViewById(R.id.txtVVerdict);
        ImageView imgVImage = (ImageView) convertView.findViewById(R.id.imgVImage);

        //prikaz podatkov
        tvName.setText(name);
        tvAllergens.setText(allergensString.toUpperCase());
        tvVerdict.setText(verdict);
        imgVImage.setImageBitmap(getBitmapFromByteArr(picture));

        return convertView;
    }

    public Bitmap getBitmapFromByteArr(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }

}
