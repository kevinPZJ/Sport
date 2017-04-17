package com.example.kevin.health.Ui;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;


import com.example.kevin.health.R;
import com.example.kevin.health.Ui.Smart.SmartFragment;
import com.example.kevin.health.Ui.Health.HealFragment;
import com.example.kevin.health.Ui.Personal.PersonalFragment;
import com.example.kevin.health.Ui.Sport.SportFragment;
import com.example.kevin.health.Ui.internal.ToolbarDelegate;

import com.example.kevin.health.base.BaseActivity;
import com.ykrank.library.StatusBarUtil;


public class MainActivity extends BaseActivity {
    private FrameLayout frameLayout;
    private SportFragment sportFragment;
    private SmartFragment smartFragment;
    private PersonalFragment personalFragment;
    private HealFragment healFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary),0);
            StatusBarUtil.StatusBarLightMode(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary),100);
        }
        setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        setToolbarBackIcon(ToolbarDelegate.CLOSE,ToolbarDelegate.COLOR_BLACK);

        initView();
        initFragment();




    }

    private void initFragment() {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FrameLayout, HealFragment.newInstance()).commit();

    }

    private void initView() {
        frameLayout= (FrameLayout) findViewById(R.id.FrameLayout);
        setTitle("健康", ToolbarDelegate.COLOR_WHITE);
        setToolbarBackground(getResources().getColor(R.color.colorPrimary));


        final TabLayout tabLayout= (TabLayout) findViewById(R.id.main_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher).setText("运动"),false);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher).setText("健康"),true);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher).setText("智玩"),false);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher).setText("我的"),false);



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment selectfragment = null;

                switch (tab.getPosition()){
                    case 0:
                        if (sportFragment==null){
                            sportFragment=SportFragment.newInstance();
                        }
                        showToolbar();
                        setTitle("运动");
                        selectfragment=sportFragment;
                        break;
                    case 1:
                        if (healFragment==null){
                            healFragment=HealFragment.newInstance();
                        }
                        selectfragment=healFragment;
                        showToolbar();
                        setTitle("健康");
                        break;
                    case 2:
                        if (smartFragment ==null){
                            smartFragment = SmartFragment.newInstance();
                        }
                        selectfragment= smartFragment;
                        showToolbar();
                        setTitle("智玩");
                        break;
                    case 3:
                        if (personalFragment==null){
                            personalFragment=PersonalFragment.newInstance();
                        }
                        selectfragment=personalFragment;
                        setTitle("我的");
                        showToolbar();

                        break;

                }
                showChooseFragment(selectfragment);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showChooseFragment(Fragment fragment) {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FrameLayout,fragment).commit();
    }
}
