package com.swastik.wardrobeapp.modules.notification_helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.modules.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {

	public static NotificationManager mManager;

	@SuppressWarnings("static-access")
	public static void generateNotification(Context context){

		mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(context,MainActivity.class);

		//
		Notification notification = null;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification();
			notification.icon = R.mipmap.ic_launcher;
			try {
				Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				deprecatedMethod.invoke(notification, context, "Wardrobe", null, intent1);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
			}
		} else {
			// Use new API
			PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Builder builder = new Notification.Builder(context)
					.setContentIntent(pendingNotificationIntent)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle("Check out your wardrobe!");
			notification = builder.build();
		}

		mManager.notify(0, notification);
		//
	}
}
