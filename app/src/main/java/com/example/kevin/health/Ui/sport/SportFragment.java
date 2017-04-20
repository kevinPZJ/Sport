package com.example.kevin.health.Ui.sport;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;


import com.example.kevin.health.R;
import com.example.kevin.health.Ui.step.StepView;
import com.example.kevin.health.base.BaseFragment;
import com.example.kevin.health.databinding.FragmentSportBinding;

import static android.databinding.DataBindingUtil.inflate;
import static com.example.kevin.health.R.id.view;

/**
 * Created by hyx on 2017/2/6.
 */

public class SportFragment extends BaseFragment implements SportContract.View  {

    private SportContract.Presenter presenter;

    private  SeekBar mSeekBar;
    private  StepView stepView;
    private  EditText mEtMax;
    private Handler mHandler = new Handler();
    private Button btnDeviceStatus ;

    public static SportFragment newInstance(){
        SportFragment fragment =new SportFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSportBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sport,container,false);
        stepView = binding.svStep;
        mSeekBar = binding.seekBar;
        mEtMax = binding.etMax;
        btnDeviceStatus =binding.btnDeviceStatus;




        
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter=new SportPresenter();
        presenter.start();
        showStep();
        btnDeviceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    private void  setDeviceStatus(String status){
        btnDeviceStatus.setText(status);
    }
}

