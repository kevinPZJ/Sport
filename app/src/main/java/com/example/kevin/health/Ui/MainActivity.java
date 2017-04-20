package com.example.kevin.health.Ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.kevin.health.R;
import com.example.kevin.health.Ui.health.HealFragment;
import com.example.kevin.health.Ui.internal.ToolbarDelegate;
import com.example.kevin.health.Ui.personal.PersonalFragment;

import com.example.kevin.health.Ui.sport.SportFragment;
import com.example.kevin.health.base.BaseActivity;
import com.ykrank.library.StatusBarUtil;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    private AHBottomNavigation bottom_navigation;

    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Health");
        if (Build.VERSION.SDK_INT>=23)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.LOCATION_HARDWARE,Manifest.permission.ACCESS_FINE_LOCATION}, 10);   }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 11);   }
        }
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
        bottom_navigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("运动", R.drawable.ic_sport);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("健康", R.drawable.ic_health);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("我的", R.drawable.ic_person_black_24dp);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottom_navigation.addItems(bottomNavigationItems);
        bottom_navigation.setTranslucentNavigationEnabled(true);
//        bottom_navigation.setColored(true);
        bottom_navigation.setSelectedBackgroundVisible(true);

        bottom_navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position){

                    case 0 :
                        showFragment(SportFragment.newInstance());
                        break;
                    case 1 :
                        showFragment(HealFragment.newInstance());
                        break;
                    case 2 :
                        showFragment(PersonalFragment.newInstance());
                        break;


                }


                return true;
            }
        });
}
    private void showFragment(Fragment fragemnt) {

        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame_content,fragemnt,"sport").commit();

    }





}

