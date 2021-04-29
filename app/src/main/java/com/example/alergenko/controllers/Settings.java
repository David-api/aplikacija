package com.example.alergenko.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alergenko.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //vhodi
    protected TextView settingsData;
    protected TextView settingsAllergens;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (kreiranje pogleda)
        View v = inflater.inflate(R.layout.settings, container, false);

        // ob kliku na text se odpre okno s podatki o uporabniku
        TextView settingsData = v.findViewById(R.id.txtData);
        View.OnClickListener listenerData = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsDataActivity();
            }
        };
        settingsData.setOnClickListener(listenerData);

        // ob kliku na text se odpre okno s podatki o alergenih
        TextView settingsAllergens = v.findViewById(R.id.txtAllergenes);
        View.OnClickListener listenerAllergens = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsAllergensActivity();
            }
        };
        settingsAllergens.setOnClickListener(listenerAllergens);

        return v;
    }

    public void openSettingsAllergensActivity(){
        //odpre settingsAllergens aktivnost
        Intent intent = new Intent(getActivity(), SettingsAllergens.class);
        ((MainActivity) getActivity()).startActivity(intent);
    }

    public void openSettingsDataActivity(){
        //odpre settingsData aktivnost
        Intent intent = new Intent(getActivity(), SettingsData.class);
        ((MainActivity) getActivity()).startActivity(intent);
    }


}