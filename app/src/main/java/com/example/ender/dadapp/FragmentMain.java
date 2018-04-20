package com.example.ender.dadapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FragmentMain extends Fragment {

    RelativeLayout frameLayout;
    int colorBack = 0xFFFFFFFF;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        frameLayout = view.findViewById(R.id.layoutFragMain);
        frameLayout.setBackgroundColor(colorBack);

        return view;
    }

    public void setColorBack(int color){
        colorBack = color;
    }
}
