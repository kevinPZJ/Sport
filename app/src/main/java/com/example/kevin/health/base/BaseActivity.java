package com.example.kevin.health.base;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
;import com.example.kevin.health.App;
import com.example.kevin.health.R;
import com.example.kevin.health.Ui.internal.SnackBarDelegate;
import com.example.kevin.health.Ui.internal.ToolbarDelegate;

/**
 * Created by hyx on 2017/2/6.
 */

public class BaseActivity extends AppCompatActivity{

    protected ToolbarDelegate mToolbarDelegate;
    protected SnackBarDelegate mSnackBarDelegate;
    protected View frameLayout;

    protected View  toolBarView;
    protected AppBarLayout AppBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
        setupSnackBar();
    }

    @Override
    @CallSuper
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setupToolbar();
        setupSnackBar();
    }


    private void setupToolbar() {
        AppBarLayout= (android.support.design.widget.AppBarLayout) findViewById(R.id.AppBarLayout );

         toolBarView = findViewById(R.id.toolbar);
        if (toolBarView != null) {
            Toolbar toolbar = (Toolbar) toolBarView;
            mToolbarDelegate = new ToolbarDelegate(this, toolbar);
        }
    }

    private void setupSnackBar() {
        frameLayout = findViewById(R.id.FrameLayout);
        if (frameLayout != null)
            mSnackBarDelegate = new SnackBarDelegate(frameLayout);
    }
    @Override
    public void setTitle(CharSequence title) {
        if (mToolbarDelegate == null)
            super.setTitle(title);
        else {
            super.setTitle(null);
            mToolbarDelegate.setTitle(title);
        }
    }

    public void setTitle(CharSequence title, @ToolbarDelegate.BAR_COLOR int color) {
        if (mToolbarDelegate == null)
            super.setTitle(title);
        else {
            super.setTitle(null);
            mToolbarDelegate.setTitle(title, color);
        }
    }

    public void setToolbarBackground(@ColorInt int color) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setToolBarBackground(color);
        }
    }

    public void setToolbarBackIcon(@ToolbarDelegate.NAV_ICON int icon, @ToolbarDelegate.BAR_COLOR int color) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setupNavCrossIcon(icon, color);
        }

    }
    public SnackBarDelegate getSnackBarDelegate() {
        return mSnackBarDelegate;
    }

    public void removeToolbar(){

     AppBarLayout.setVisibility(View.GONE);
    }
    public void showToolbar(){
        AppBarLayout.setVisibility(View.VISIBLE);
    }

}
