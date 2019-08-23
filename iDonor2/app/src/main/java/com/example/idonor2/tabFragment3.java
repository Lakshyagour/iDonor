package com.example.idonor2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabFragment3 extends Fragment {


    public tabFragment3 newInstance() {
        tabFragment3 t3=new tabFragment3();
        return t3;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.tab_fragment3, container, false);
        return view;
    }

}
