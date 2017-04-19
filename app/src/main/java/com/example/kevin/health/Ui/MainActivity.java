package com.example.kevin.health.Ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kevin.health.R;
import com.example.kevin.health.Ui.health.HealFragment;
import com.example.kevin.health.Ui.heart.HeartFragment;
import com.example.kevin.health.Ui.internal.ToolbarDelegate;
import com.example.kevin.health.Ui.personal.PersonalFragment;

import com.example.kevin.health.Ui.sport.SportFragment;
import com.example.kevin.health.base.BaseActivity;
import com.ykrank.library.StatusBarUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Health");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
            StatusBarUtil.StatusBarLightMode(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 100);
        }
        setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        setToolbarBackIcon(ToolbarDelegate.CLOSE, ToolbarDelegate.COLOR_BLACK);
        initView();
        showFragment(SportFragment.newInstance());
    }

    private void initView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nv_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.navigation_sport:
//                        showFragment(SportFragment.newInstance());
//                     return true;
//
//
//                    case R.id.navigation_health:
//
//                        showFragment(HealFragment.newInstance());
//
//                        break;
//                    case R.id.navigation_personal:
//                        showFragment(PersonalFragment.newInstance());
//
//                        break;
//                }
//                return true;
//            }
//        });



    }

    private void showFragment(Fragment fragemnt) {

        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame_content,fragemnt,"sport").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sport:

                  showFragment(SportFragment.newInstance());

                    return true;
                case R.id.navigation_health:
                    showFragment(HealFragment.newInstance());

                    return true;
                case R.id.navigation_personal:
                    showFragment(PersonalFragment.newInstance());


                    return true;
            }
            return false;
        }

    };



}

