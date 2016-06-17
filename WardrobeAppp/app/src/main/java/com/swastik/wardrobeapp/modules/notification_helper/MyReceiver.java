package com.swastik.wardrobeapp.modules.notification_helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try{
			Utils.generateNotification(context);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
