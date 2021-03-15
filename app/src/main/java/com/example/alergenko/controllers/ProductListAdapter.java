package com.example.alergenko.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alergenko.R;
import com.example.alergenko.entities.Product;

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
        int id = getItem(position).getId();
        String name = getItem(position).getName();
        String brandName = getItem(position).getBrandName();

        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource, parent, false);

        TextView tvID = (TextView) convertView.findViewById(R.id.txtVProductName);
        TextView tvName = (TextView) convertView.findViewById(R.id.txtVAllergens);
        TextView tvBname = (TextView) convertView.findViewById(R.id.txtVVerdict);


        tvID.setText(Integer.toString(id));
        tvName.setText(name);
        tvBname.setText(brandName);

        return convertView;
    }
}
