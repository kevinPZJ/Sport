package com.example.kevin.health.Ui.Fun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.health.R;

/**
 * Created by hyx on 2017/2/6.
 */

public class FunFragment extends Fragment {

    public static FunFragment newInstance(){
        FunFragment fragment =new FunFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);

        return view;
    }
}
