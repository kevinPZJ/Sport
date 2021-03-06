package com.example.kevin.health.ble.cmd;

import com.example.kevin.health.ble.BaseBleMessage;


public class cmd_RemindOnOff extends BaseBleMessage {
	
	public static final String TAG = "tixing";
/**
 * 
 * 
 * 
 */
	public static final byte mTheCmd  = 0x05;
	
	public static final int REMIND_TYPE_LOST  = 1;
	public static final int REMIND_TYPE_SMS  = 2;
	public static final int REMIND_TYPE_PHONE  = 3;
	
	public byte[] switchRemind(int remindType, boolean remindOnOff) {
		byte[] data = new byte[3];
		data[0] = 0x00;
		switch (remindType) {
		case REMIND_TYPE_LOST: {
			// LOST
			data[1] = 0x01;
			data[2] = (byte)(remindOnOff ? 0x01 : 0x00);
			return setMessageByteData(mTheCmd, data, data.length);
		}
		case REMIND_TYPE_SMS: {
			// sms
			data[1] = 0x02;
			data[2] = (byte)(remindOnOff ? 0x01 : 0x00);
			return setMessageByteData(mTheCmd, data, data.length);
		}
		case REMIND_TYPE_PHONE: {
			// phone
			data[1] = 0x03;
			data[2] = (byte)(remindOnOff ? 0x01 : 0x00);
			return setMessageByteData(mTheCmd, data, data.length);
		}
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param context
	 * @param type    1：防丢提醒        2：短信提醒        3：来电提醒
	 * @return		  0：关              1：开
	 */
	public byte[] readRemindStatus(int type) {
		byte[] data = new byte[2];
		// 新协议
		data[0] = 0x01;
		switch (type) {
		case REMIND_TYPE_LOST:
			data[1] = 0x01;
			break;
		case REMIND_TYPE_SMS:
			data[1] = 0x02;
			break;
		case REMIND_TYPE_PHONE:
			data[1] = 0x03;
			break;
		default:
			return null;
		}
		
		return setMessageByteData(mTheCmd, data, data.length);
	}

	/**
	 * 存入缓存
	 * @param context
	 * @param notifyData
	 * @param dataLen
	 */
	public byte[] dealBleResponse(byte[] notifyData, int dataLen) {
		// TODO Auto-generated method stub
		if (dataLen <= 2) {
			return null;
		}
		// ru: 01 01 01
		int type = notifyData[1];
		boolean isOpened = (notifyData[2] == 0x01)?true:false;

//		switch (type) {
//		case REMIND_TYPE_LOST:
//			Log.d("", "===========防丢状态：" + isOpened);
//			demo.hrDK.mToggleButtonLose.setChecked(isOpened);
//			break;
//		case REMIND_TYPE_SMS:
//			Log.d("", "===========短信状态：" + isOpened);
//			demo.hrDK.mToggleButtonSms.setChecked(isOpened);
//			break;
//		case REMIND_TYPE_PHONE:
//			Log.d("", "===========电话状态：" + isOpened);
//			demo.hrDK.mToggleButtonCall.setChecked(isOpened);
//			break;
//		default:
//			break;
//		}
		return null;
	}
	
}
