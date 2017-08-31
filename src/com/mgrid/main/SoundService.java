package com.mgrid.main;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

public class SoundService extends Service {

	
	private MediaPlayer mp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp=new MediaPlayer();
		mp.setLooping(true); 
		String song = Environment.getExternalStorageDirectory().getPath()  + "/vtu_pagelist/Alarm.wav";
       // System.out.println(song);
				try {
            mp.setDataSource(song);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.release();
        stopSelf();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		boolean playing = intent.getBooleanExtra("playing", false);
		 if (playing) {
	            mp.start();
	        } else {
	            mp.pause(); 
	        }
		
		 /*
		  * START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象
		  *   START_NOT_STICKY：“非粘性的“。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务
		  *   START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
		  *     START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
		  */
		return   START_NOT_STICKY;  
	}

}
