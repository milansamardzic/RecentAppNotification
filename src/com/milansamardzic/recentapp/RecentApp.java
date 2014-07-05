package com.milansamardzic.recentapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class RecentApp extends Activity {
	
	private void ToastPoruka() throws NoSuchMethodException,
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

}