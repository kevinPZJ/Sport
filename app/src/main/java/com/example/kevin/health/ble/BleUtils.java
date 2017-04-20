package com.example.kevin.health.ble;

/**
 * Created by hyx on 2017/4/20.
 */

public class BleUtils {
    
    public static final int REQUEST_SELECT_DEVICE = 101;
    public static final int REQUEST_ENABLE_BT = 102;
    public static final int UART_PROFILE_READY = 10;
    public static final String TAG = "RFUART";
    public static final int UART_PROFILE_CONNECTED = 20;
    public static final int UART_PROFILE_DISCONNECTED = 21;
    public static final int STATE_OFF = 10;
    public static final int GET_POWER = 100;

    public UartService mUartService = null;


//    public static void setTx_data(byte[] tx_data) {
//        if (tx_data == null) {
//            return;
//        }
//        int len = tx_data.length;
//        if (mUartService == null) {
//            return;
//        }
//        if (mUartService.isConnected() != true) {
//            return;
//        }
//        for (int i = 0; i < len; i++) {
//            if (tx_data_len >= 512) {
//                tx_data_rear = (tx_data_rear + 1) % 512;
//                tx_data_len--;
//            }
//            this.tx_data[tx_data_front] = tx_data[i];
//            tx_data_front = (tx_data_front + 1) % 512;
//            tx_data_len++;
//        }
//        if (time_flag == 0) {
//            handler.postDelayed(runnable, 200);
//            time_flag = 1;
//        }
//    }
}
