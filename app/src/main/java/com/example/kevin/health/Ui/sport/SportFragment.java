package com.example.kevin.health.Ui.sport;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kevin.health.R;
import com.example.kevin.health.Ui.MainActivity;
import com.example.kevin.health.Ui.step.StepView;
import com.example.kevin.health.base.BaseFragment;
import com.example.kevin.health.ble.BleNotifyParse;
import com.example.kevin.health.ble.BleUtils;
import com.example.kevin.health.ble.Config;
import com.example.kevin.health.ble.DeviceListActivity;
import com.example.kevin.health.ble.UartService;
import com.example.kevin.health.ble.cmd.cmd_getNowData;
import com.example.kevin.health.databinding.FragmentSportBinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.databinding.DataBindingUtil.inflate;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.kevin.health.R.id.view;
import static com.example.kevin.health.ble.BleUtils.REQUEST_ENABLE_BT;
import static com.example.kevin.health.ble.BleUtils.REQUEST_SELECT_DEVICE;
import static com.example.kevin.health.ble.BleUtils.UART_PROFILE_CONNECTED;
import static com.example.kevin.health.ble.BleUtils.UART_PROFILE_DISCONNECTED;

/**
 * Created by hyx on 2017/2/6.
 */

public class SportFragment extends BaseFragment implements SportContract.View {

    private SportContract.Presenter presenter;

    private SeekBar mSeekBar;
    private StepView stepView;
    private EditText mEtMax;

    private TextView tvDeviceStatus;

    private  int Step = 0;

    private Button btnDeviceStatus;
    private Config config;
    private BluetoothAdapter mBluetoothAdapter;
    private UartService mUartService;  // 服务

    //蓝牙配置相关
    private final int intf_none = 0;
    private final int intf_ble_uart = 1;
    private int intf = intf_ble_uart;
    private int mState = UART_PROFILE_DISCONNECTED;

    // 手环反馈数据包
    private byte[] tx_data = new byte[512];
    private int tx_data_len = 0;
    private int tx_data_front = 0;
    private int tx_data_rear = 0;

    private String Ble_Cmd_Response;   // 手环回应


    int time_flag = 0;
    private Handler handler = new Handler();

    private BluetoothDevice mDevice = null;
    private BleNotifyParse bleNotify = new BleNotifyParse();




    private Runnable runnable = new Runnable() {
        public void run() {
            if (tx_data_len > 0) {
                int len;
                if (tx_data_len > 20) {
                    len = 20;
                } else {
                    len = tx_data_len;
                }

                byte[] send_buf = new byte[len];
                for (int i = 0; i < len; i++) {
                    send_buf[i] = tx_data[tx_data_rear];
                    tx_data_rear = (tx_data_rear + 1) % 512;

                }

                if (mUartService != null) {
                    mUartService.writeRXCharacteristic(send_buf);
                }
                tx_data_len = tx_data_len - len;
            }

            if (tx_data_len > 0) {
                handler.postDelayed(this, 200);
            } else {
                time_flag = 0;
            }
        }
    };
    private boolean bStartHRTest ;


    public static SportFragment newInstance() {
        SportFragment fragment = new SportFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSportBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sport, container, false);
        stepView = binding.svStep;
        mSeekBar = binding.seekBar;
        mEtMax = binding.etMax;
        btnDeviceStatus = binding.btnDeviceStatus;
        tvDeviceStatus = binding.tvDeviceStatus;
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new SportPresenter();
        presenter.start();
        showStep();
        initBle();
        initService();

        btnDeviceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getContext(), DeviceListActivity.class);
                startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
            }
        });

        getNowData();
    }

    private void getNowData() {
        cmd_getNowData cmd =new cmd_getNowData();
        cmd.getData();
        
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                     ;
                        stepView.stopAnimator(Step); //手环传过来的步数

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

    private void initBle() {

        config = new Config(getContext());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            return;
        }

        if (config.isValid() == true) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        if (config.isValid() == true) {
            bStartHRTest = true;
            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(config.getAddress());
//            tvDeviceName.setText(config.getName());
            setTvDeviceStatus("Connecting");
        }



    }

    private void initService() {
        Intent bindIntent = new Intent(getContext(), UartService.class);
        getContext().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                UARTStatusChangeReceiver, makeGattUpdateIntentFilter());

    }

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT);
        return intentFilter;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder rawBinder) {
            if (intf == intf_ble_uart) {
                mUartService = ((UartService.LocalBinder) rawBinder).getService();
                Log.d(TAG, "onServiceConnected mService= " + mUartService);
                if (!mUartService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    finish();
                } else {
                    if (config.isValid() == true) {
                        if (mBluetoothAdapter.isEnabled()) {
                            setTvDeviceStatus("Connecting");
                            mUartService.connect(config.getAddress());

                        }
                    }
                }
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            // // mService.disconnect(mDevice);
            if (intf == intf_ble_uart) {
                mUartService = null;
            }
        }
    };


    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final Intent mIntent = intent;
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {

                tvDeviceStatus.setText("已经连接");
                mState = UART_PROFILE_CONNECTED;
                if (config.isValid() == false) {
                    config.save_config(mDevice.getName(), mDevice.getAddress());
                }


                if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                    Log.d(TAG, "UART_DISCONNECT_MSG");
                    setTvDeviceStatus("Disconnect");
                    mState = UART_PROFILE_DISCONNECTED;
                    mUartService.close();
                    if (config.isValid() == true) {
                        setTvDeviceStatus("Connecting");
                        mUartService.connect(mDevice.getAddress());
                    }
                }

                if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                    mUartService.enableTXNotification();
                    try {
                        Thread.currentThread().sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                // *********************//
                if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

                    byte[] dataPackage = intent.getByteArrayExtra(UartService.EXTRA_DATA);

                    bleNotify.doParse(getContext(), dataPackage);

                    Ble_Cmd_Response = bleNotify.getResponse();

//                    Log.e("66666666666666666", Ble_Cmd_Response);


                    if (Ble_Cmd_Response != null) {
                        switch (Ble_Cmd_Response.substring(2, 4)) {
                            case "83":
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String power = Ble_Cmd_Response.substring(8, 10);
//                                        BigInteger srch = new BigInteger(power, 16);
//                                        tvPower.setText(srch.toString());
//
//                                    }
//                                });
                                break;

                            case "86":
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

//                                        String heart = Ble_Cmd_Response.substring(10, 12);
//                                        BigInteger heartforHex = new BigInteger(heart, 16);
//                                        tvHeartRate.setText(heartforHex.toString());              //心率


                                        String steps_01 = Ble_Cmd_Response.substring(10, 14);
                                        BigInteger stepforHex_01 = new BigInteger(steps_01, 16);
                                        int step_01 = Integer.parseInt(stepforHex_01.toString());
                                        String steps_02 = Ble_Cmd_Response.substring(14, 18);

                                        BigInteger stepforHex_02 = new BigInteger(steps_02, 16);
                                        int step_02 = Integer.parseInt(stepforHex_02.toString());

                                        Step = step_01+step_02;


                                        String dis_01 = Ble_Cmd_Response.substring(18, 22);
                                        BigInteger disforHex_01 = new BigInteger(dis_01, 16);
                                        int num_dis_01 = Integer.parseInt(disforHex_01.toString());

                                        String dis_02 = Ble_Cmd_Response.substring(22, 26);
                                        BigInteger disforHex_02 = new BigInteger(dis_02, 16);
                                        int num_dis_02 = Integer.parseInt(disforHex_02.toString());


//                                        tvDistance.setText((num_dis_01 + num_dis_02) + "米");
                                        setDistance((num_dis_01 + num_dis_02)+"");


                                        String cal_01 = Ble_Cmd_Response.substring(26, 30);
                                        BigInteger calorHex_01 = new BigInteger(cal_01, 16);
                                        int num_cal_01 = Integer.parseInt(calorHex_01.toString());

                                        String cal_02 = Ble_Cmd_Response.substring(30, 34);
                                        BigInteger calforHex_02 = new BigInteger(cal_02, 16);
                                        int num_cal_02 = Integer.parseInt(calforHex_02.toString());

//                                        tvHot.setText((num_cal_01 + num_cal_02) + "大卡");
                                        setCal((num_cal_01 + num_cal_02));


                                    }
                                });
//                  sb.delete(0, sb.length());
                                break;
                            default:
                                break;

                        }


                        if ("68".equals(Ble_Cmd_Response.substring(0, 2)) && "16".equals(Ble_Cmd_Response.substring(Ble_Cmd_Response.length() - 2, Ble_Cmd_Response.length()))) {
                            String time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA)
                                    .format(new Date(System.currentTimeMillis()));

                            File outputImage = new File(getContext().getExternalCacheDir(), Ble_Cmd_Response.substring(2, 4) + "-" + time + ".txt");
                            FileOutputStream fos = null;
                            BufferedWriter writer = null;
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                                fos = new FileOutputStream(outputImage);
                                fos.write(Ble_Cmd_Response.toString().getBytes());

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (writer != null) {
                                    try {
                                        writer.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }

                    }

                } else {
                    return;
                }
                if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT)) {
                    //showMessage("Device doesn't support UART. Disconnecting");
                    mUartService.disconnect();
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mUartService.close();
                    if (config.isValid() == true) {
                        setTvDeviceStatus("Connecting");
                        mUartService.connect(mDevice.getAddress());
                    }
                }

            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                // When the DeviceListActivity return, with the selected device
                // address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
//
//                    tvDeviceName.setText(mDevice.getName());
                      setTvDeviceStatus("Connecting");

                    if (intf == intf_ble_uart)
                        mUartService.connect(deviceAddress);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
//                    Toast.makeText(this, "Bluetooth has turned on ",
//                            Toast.LENGTH_SHORT).show();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(this, "Problem in BT Turning ON ",
//                            Toast.LENGTH_SHORT).show();
                    //finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    private void setTvDeviceStatus(String status) {
        tvDeviceStatus.setText(status);
    }

    private void setStep(int Step) {
        stepView.stopAnimator(Step);
    }

    private void setDistance(String Step) {
        stepView.setFormat(Step);
    }
    private void setCal(int cal) {
        stepView.setCarol(cal);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (intf == intf_ble_uart) {
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Log.i(TAG, "onResume - BT not enabled yet");
                    Intent enableIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
            }
        }
        showStep();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver
              (UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }

        try {
            getContext().unbindService(mServiceConnection);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        if (mUartService != null) {
            mUartService.stopSelf();
            mUartService = null;
        }
    }

    public void setTx_data(byte[] tx_data) {
        if (tx_data == null) {
            return;
        }
        int len = tx_data.length;
        if (mUartService == null) {
            return;
        }
        if (mUartService.isConnected() != true) {
            return;
        }
        for (int i = 0; i < len; i++) {
            if (tx_data_len >= 512) {
                tx_data_rear = (tx_data_rear + 1) % 512;
                tx_data_len--;
            }
            this.tx_data[tx_data_front] = tx_data[i];
            tx_data_front = (tx_data_front + 1) % 512;
            tx_data_len++;
        }
        if (time_flag == 0) {
            handler.postDelayed(runnable, 200);
            time_flag = 1;
        }
    }

}