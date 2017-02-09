package com.example.kevin.health.Ui.Smart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.health.R;

/**
 * Created by hyx on 2017/2/6.
 */

public class SmartFragment extends Fragment {

    public static SmartFragment newInstance(){
        SmartFragment fragment =new SmartFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_Smart, container, false);

        return view;
    }
}
