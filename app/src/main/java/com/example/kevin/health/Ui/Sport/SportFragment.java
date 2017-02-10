package com.example.kevin.health.Ui.Sport;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;


import com.example.kevin.health.R;
import com.example.kevin.health.Ui.Step.StepView;
import com.example.kevin.health.base.BaseFragment;

/**
 * Created by hyx on 2017/2/6.
 */

public class SportFragment extends BaseFragment implements SportContract.View  {

    private SportContract.Presenter presenter;

    private  SeekBar mSeekBar;
    private  StepView stepView;
    private  EditText mEtMax;
    private Handler mHandler = new Handler();

    public static SportFragment newInstance(){
        SportFragment fragment =new SportFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport, container, false);
        stepView= (StepView) view.findViewById(R.id.sv_step);
        mSeekBar = (SeekBar)view.findViewById(R.id.seekBar);
        mEtMax = (EditText)view.findViewById(R.id.et_max);
        showStep();
        
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter=new SportPresenter();
        presenter.start();
       
    }







    @Override
    public void onResume() {
        super.onResume();
         showStep();
    }

    @Override
    public void showStep() {
        stepView.setMaxProgress(8000);//设置每日目标步数
        stepView.setProgress(8888);//每日步数目标
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stepView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mEtMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String max = s.toString();

                int maxProgress = 0;
                try {
                    maxProgress = Integer.parseInt(max);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                stepView.setMaxProgress(maxProgress);

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stepView.stopAnimator(6666); //手环传过来的步数

                    }
                });
            }
        }).start();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }
}

