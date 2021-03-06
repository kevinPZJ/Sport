package com.example.kevin.health.ble;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.kevin.health.Ui.MainActivity;
import com.example.kevin.health.Ui.health.HealFragment;
import com.example.kevin.health.Ui.sport.SportFragment;
import com.example.kevin.health.ble.cmd.cmd_upLoad;
import com.example.kevin.health.ble.cmd.cmd_setStepConfig;
import com.example.kevin.health.ble.cmd.cmd_getNowData;
import com.example.kevin.health.ble.cmd.cmd_getPower;
import com.example.kevin.health.ble.cmd.cmd_RemindOnOff;
import com.example.kevin.health.ble.cmd.cmd_heartRate;
import com.example.kevin.health.ble.cmd.cmd_getAlert;
import com.example.kevin.health.ble.cmd.cmd_set_Sit;
import com.example.kevin.health.ble.cmd.cmd_setNowTime;


public class BleNotifyParse extends BaseBleMessage{

	private static final int BUFFER_MAX_LEN = 1024;
	private byte[] buffer = new byte[BUFFER_MAX_LEN];

	private byte[] bufferTmp = new byte[BUFFER_MAX_LEN];

	private int bufferFront = 0;	//队列尾
	private int bufferRear = 0;  //队列头
	private int bufferLen = 0;  //队列头

	private static BleNotifyParse mBleNotifyParse;



    private String   Cmd__reSponse;


	public static BleNotifyParse getInstance() {
		if (mBleNotifyParse == null) {
			mBleNotifyParse = new BleNotifyParse();
		}
		return mBleNotifyParse;
	}

	public void doParse(Context context, byte[] notifyData) {
		//synchronized (mNotifyLock) {
			l_doParse(context, notifyData);
		//}
	}

	private String l_doParse(Context context, byte[] notifyData) {
        // 加入循环队列
		Log.d(BaseBleMessage.BASE_TAG, "notify: "+BaseBleMessage.byteArrHexToString(notifyData));

        for (int i = 0; i < notifyData.length; i++) {
        	if(bufferLen >= BUFFER_MAX_LEN)
        	{
        		bufferRear = (bufferRear + 1) % BUFFER_MAX_LEN;
        		bufferLen--;
        	}
            buffer[bufferFront] = notifyData[i];
            bufferFront = (bufferFront + 1) % BUFFER_MAX_LEN;
            bufferLen++;
        }

        int notifyIndex = 0;
        int msgLen = 0;
        int pos;
        int step = 0;

        for (int read = 0; read < bufferLen;) {
        	pos = (bufferRear + read) % BUFFER_MAX_LEN;
        	read++;
        	switch(step) {
        	case 0:
        		if (buffer[pos] == 0x68) {
        			notifyIndex = 0;
        			bufferTmp[notifyIndex++] = buffer[pos];
        			bufferRear = pos;
        			bufferLen = bufferLen - read + 1;
        			read = 1;
        			step++;
                }
        		break;
        	case 1:
        		bufferTmp[notifyIndex++] = buffer[pos];
        		if(notifyIndex >= 4)
        		{
        			msgLen = (int)(0xff & bufferTmp[2]);
        			msgLen = msgLen + (int)((0x00ff & bufferTmp[3])<<8);
        			if(msgLen > 256)
        			{
        				step = 0;
        				bufferRear = (bufferRear + 1) % BUFFER_MAX_LEN;
        				bufferLen--;
        				read = 0;
        				break;
        			}
        			step++;
        		}
        		break;
        	case 2:
        		bufferTmp[notifyIndex++] = buffer[pos];
        		if(notifyIndex >= (msgLen + 6))
        		{
        			if(buffer[pos] == 0x16)
        			{
        				Comm_Handle(context, bufferTmp, notifyIndex);
        				bufferLen = bufferLen - notifyIndex;
        				bufferRear = (pos + 1) % BUFFER_MAX_LEN;
        				read = 0;
        			}
        			else {
        				bufferRear = (bufferRear + 1) % BUFFER_MAX_LEN;
        				bufferLen--;
        				read = 0;
        			}
        			step = 0;
        		}

        		break;
        	default:
        		break;
        	}
        }
        return  BaseBleMessage.byteArrHexToString(notifyData);
    }

	private boolean Comm_Handle(Context context, byte[] notifyData, int dataLength) {
		
		//int ret = -1;
		byte[] ret = null;
		
		byte[] frame = new byte[dataLength];
		for (int i = 0; i < dataLength; i++) {
			frame[i] = notifyData[i];
		}

		Log.d(BaseBleMessage.BASE_TAG, "rec: "+BaseBleMessage.byteArrHexToString(frame));

        Cmd__reSponse= BaseBleMessage.byteArrHexToString(frame);

		//byte head = notifyData[0];
		byte cmd = frame[1];
		int dataLen = bytes2Char(frame, 2);
		
		if (dataLen > 1000 || dataLength < dataLen) {
			return false;
		}
		
		byte[] Data = new byte[dataLen];
		for (int i = 0; i < dataLen; i++) {
			Data[i] = frame[i + 4];
		}
		//notifyData = theData;
		
		if ((cmd & 0x80) == 0x80) {
			// 手环回复给手机的数据部分
			byte req_cmd = (byte) (cmd & 0x3f);
			if(req_cmd == cmd_getPower.mTheCmd) {
				cmd_getPower ctrlCmd = new cmd_getPower();
				ret = ctrlCmd.dealBleResponse(Data, dataLen);
			}  else if(req_cmd == cmd_RemindOnOff.mTheCmd) {
				cmd_RemindOnOff ctrlCmd = new cmd_RemindOnOff();
				ret = ctrlCmd.dealBleResponse(Data, dataLen);
			}
			else if(req_cmd == cmd_getNowData.mTheCmd) {
				cmd_getNowData ctrlCmd = new cmd_getNowData();
				ret = ctrlCmd.dealBleResponse(Data, dataLen);
			} else if(req_cmd == cmd_heartRate.mTheCmd) {
				cmd_heartRate ctrlCmd = new cmd_heartRate();
				ret = ctrlCmd.dealBleResponse(Data, dataLen);
			} else if (req_cmd == cmd_setNowTime.mTheCmd) {
				cmd_setNowTime ctrlCmd = new cmd_setNowTime();
				ret = ctrlCmd.dealBleResponse(Data, dataLen);
			}else if (req_cmd == cmd_getAlert.mTheCmd){
				cmd_getAlert ctrCmd = new cmd_getAlert();
				ret =ctrCmd.dealBleResponse(Data,dataLen);
			}
			else if (req_cmd == cmd_set_Sit.mTheCmd){
				cmd_set_Sit ctrCmd = new cmd_set_Sit();
				ret =ctrCmd.dealBleResponse(Data,dataLen);
			}else if (req_cmd == cmd_upLoad.mTheCmd){
                cmd_upLoad ctrCmd = new cmd_upLoad();
                ret =ctrCmd.dealBleResponse(Data,dataLen);
			}else if (req_cmd == cmd_setStepConfig.mTheCmd){
				cmd_setStepConfig ctrCmd = new cmd_setStepConfig();
				ret =ctrCmd.dealBleResponse(Data,dataLen);
			}
			else {
				return false;
			}
		}
		
		if(ret != null)
		{
			new SportFragment().setTx_data(ret);
		}
		
		return true;
	}
	
	int bytes2Char(byte[] data, int offset) {
		int va = data[offset] & 0xff;
		int vb = (data[offset+1] << 8) & 0xffff;
		return va+vb;
	}


	public  String getResponse(){


        return  Cmd__reSponse;
    }

}
