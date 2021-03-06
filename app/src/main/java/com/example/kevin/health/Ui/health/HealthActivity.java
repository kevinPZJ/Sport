package com.example.kevin.health.Ui.health;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.example.kevin.health.R;
import com.example.kevin.health.Ui.heart.HeartActivity;
import com.example.kevin.health.Ui.internal.ToolbarDelegate;
import com.example.kevin.health.Ui.sport.SportFragment;
import com.example.kevin.health.base.BaseActivity;
import com.ykrank.library.StatusBarUtil;

/**
 * Created by hyx on 2017/4/19.
 */

public class HealthActivity extends BaseActivity {

    public static void StartActivity(Fragment fragment){
        Intent intent =new Intent(fragment.getContext(),HealthActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setTitle("健康");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary),0);
            StatusBarUtil.StatusBarLightMode(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary),100);
        }
        setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        setToolbarBackIcon(ToolbarDelegate.CLOSE,ToolbarDelegate.COLOR_BLACK);

        HealFragment fragment = HealFragment.newInstance();
        if (fragment==null){
            setMainFragment(fragment,"health");
        }
    }
}
