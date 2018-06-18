package com.example.ender.dadapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import api.ConfigAPI;

public class FragmentAbout extends Fragment {


    private EditText edtIp;
    private EditText edtVersao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment,container,false);

        edtIp = view.findViewById(R.id.edtIp);
        edtIp.setText(ConfigAPI.getIp());
        edtIp.setEnabled(false);

        edtVersao = view.findViewById(R.id.edtVersao);
        edtVersao.setEnabled(false);


        return view;
    }


}
