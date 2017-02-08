package com.example.kevin.health.Ui.Health;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.health.R;

/**
 * Created by hyx on 2017/2/6.
 */

public class HealFragment extends Fragment {

    public static HealFragment newInstance(){
        HealFragment fragment =new HealFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        return view;
    }
}
