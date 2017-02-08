package com.example.kevin.health.Ui.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.kevin.health.R;
import com.example.kevin.health.base.BaseFragment;

/**
 * Created by hyx on 2017/2/6.
 */

public class PersonalFragment extends BaseFragment implements PersonalContract.View {

    private PersonalContract.Presenter presenter;


    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new PersonalPresenter(this);

        presenter.start();

    }
}
