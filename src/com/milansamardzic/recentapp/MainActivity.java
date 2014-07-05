package com.milansamardzic.recentapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;

public class MainActivity extends Activity {
	Context mContext;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

			//freeRAM
			MemoryInfo mi = new MemoryInfo();
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);
			long availableMegs = mi.availMem / 1048576L;
			String freeRam = Long.toString(availableMegs);
		
					
			//ongoing Notification
			Context context = getApplicationContext();
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher);
			builder.setContentTitle("App Switcher");
			builder.setContentText("Avaible RAM: "+freeRam+" MB");
			
			
			Intent intent = new Intent(context, MainActivity.class);
			
			
			int mID = 0;
			PendingIntent pIntent = PendingIntent.getActivity(context, mID , intent, 0);
			builder.setContentIntent(pIntent);
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
			Notification notif = builder.build();
			notif.flags = Notification.FLAG_ONGOING_EVENT;
			mNotificationManager.notify(mID, notif);
			
			
			try {
				RecentApplication();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void RecentApplication() throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			RemoteException {

		@SuppressWarnings("rawtypes")
		Class serviceManagerClass = Class.forName("android.os.ServiceManager");
		@SuppressWarnings("unchecked")
		Method getService = serviceManagerClass.getMethod("getService",
				String.class);
		IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass,
				"statusbar");
		@SuppressWarnings("rawtypes")
		Class statusBarClass = Class
				.forName(retbinder.getInterfaceDescriptor());
		@SuppressWarnings("unchecked")
		Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
				"asInterface", IBinder.class).invoke(null,
				new Object[] { retbinder });
		@SuppressWarnings("unchecked")
		Method clearAll = statusBarClass.getMethod("toggleRecentApps");
		clearAll.setAccessible(true);
		clearAll.invoke(statusBarObject);
	}

	public class BootUpReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
		    Intent i = new Intent(context, MainActivity.class);  
		    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    context.startActivity(i);  
		}
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
