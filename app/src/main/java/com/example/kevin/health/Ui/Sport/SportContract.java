package com.example.kevin.health.Ui.Sport;


import com.example.kevin.health.base.BaseContract;

/**
 * Created by hyx on 2017/2/9.
 */

public interface SportContract  {
    interface View extends BaseContract.BaseView<Presenter>{
        void showStep();

    }





    interface Presenter extends BaseContract.BasePresenter{



    }

}
