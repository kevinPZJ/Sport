package com.example.kevin.health.ble;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Config {
	private String name;
	private String address;
	private boolean valid = false;
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDR = "address";
	public static final String KEY_VALID = "valid";
	public static final String DATABASE = "Config";
	private Context context;
	private SharedPreferences sp;
	//private SharedPreferences sp = null;
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Config(Context context)
	{
		super();
		//this.setValid(false);
		this.context = context;
		sp = this.context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
		valid = sp.getBoolean(KEY_VALID, false);
		name = sp.getString(KEY_NAME, "");
		address = sp.getString(KEY_ADDR, "");
		//address = new byte[6];
	}
	
	public void clear_config()
	{
		this.setValid(false);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	
	public void save_config(String ble_name, String ble_addr)
	{
		this.setValid(true);
		this.name = ble_name;
		this.address = ble_addr;
	
		Editor editor = sp.edit();
		editor.putString(KEY_NAME, ble_name);
		editor.putString(KEY_ADDR, ble_addr);
		editor.putBoolean(KEY_VALID, true);
		editor.commit();
	}
	
	public String getName() {
		Log.d("Consig", "dev_name:"+name);
		return name;
	}

	public String getAddress() {
		return address;
	}
}

