package com.lianxin.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class WxService extends Service {

	private UserSerialPort usp = new UserSerialPort();
	
	private Handler objHandler = new Handler();
	
//	private Thread mThreadDm;
//	private Thread mThreadRece;

	private DBManager dbManager;
	
	private int intCounter=0;							//循环点名计数
	
	Vector<String> list = new Vector<String>();			//存放点名的机器号的数组

//	private byte[] buffer = new byte[24];				//接收数据缓存

	protected boolean isRece = false;							//允许接收数据标志
	protected boolean isSend = true;							//允许发送数据标志
	
	private Thread mThreadDm = new Thread(new Runnable() {
		public void run() {
			
		}
	});
	
	private Thread mThreadRece = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	});
	
	private Runnable mTasksDm = new Runnable(){
		
		public void run()
		{
			if(isSend){
				isRece = false;
				usp.send(list.get(intCounter).toString());
				if(intCounter<list.size()-1)
					intCounter++;
				else
					intCounter=0;
				isRece = true;
				objHandler.post(mTasksRece);
			//	isSend = false;
			}
			objHandler.postDelayed(mTasksDm, GlobalVal.DELAY_TIME_DM);
	    }
	};
	
	private Runnable mTasksRece = new Runnable() {
		
		@Override
		public void run() {
		byte[] buffer = new byte[124];
				//	while(isRece){
				usp.receive(buffer);
				if((buffer[0]==(byte)0)||(buffer[1]==(byte)0)||(buffer[2]==(byte)0)){
					isSend = false;
					int len = (int)buffer[4]&0xff;
					String result = "";
//			        for (int i = 8; i-8 < len; i++) {
//			            String hexString = Integer.toHexString(buffer[i] & 0xFF);
//			            if (hexString.length() == 1) {
//			                hexString = '0' + hexString;
//			            }
//			            result += hexString.toUpperCase();
//			        }
					for(int i = 8; i-8 < len - 4; i++){
						char strChar = (char) ((int)buffer[i]&0xff);
						result += strChar;
					}
					if(len!=0)
					Log.i("info", len + result);
					if(buffer[7]==(byte)1){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						usp.receive(buffer);
						if((buffer[0]==(byte)0)||(buffer[1]==(byte)0)||(buffer[2]==(byte)0)){
							len = (int)buffer[4]&0xff;
							result = "";
//							for(int i = 8; i-8 < len - 4; i++){
//								char strChar = (char) ((int)buffer[i]&0xff);
//								result += strChar;
//							}
							for (int i = 8; i-8 < len; i++) {
					            String hexString = Integer.toHexString(buffer[i] & 0xFF);
					            if (hexString.length() == 1) {
					                hexString = '0' + hexString;
					            }
					            result += hexString.toUpperCase();
					        }
							if(len!=0)
							Log.i("info", len + ": " + result);
						}
					}
				}
				isSend = true;
	//		}
			
		}
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i("info", "onCreate"+list);
//		if (usp.getSerial()==null)
//			usp.start(6);

//		dbManager = new DBManager(this);
//		dbManager.getTerminalNum(list);
//		
//		objHandler.postDelayed(mTasksDm, GlobalVal.DELAY_TIME_DM);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i("info", "onDestroy");
		objHandler.removeCallbacks(mTasksDm);
		usp.close();
		dbManager.close();
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("info", "onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
	//	usp.send("001");
		Log.i("info", "onStart");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("info", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("info", "onUnbind");
		return super.onUnbind(intent);
	}
	
	public class WxBinder extends Binder{
		public WxService getService(){
			return WxService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i("info", "onBind");
		return null;
	}
	
//	public static void main(String[] args){
//		WxService w = new WxService();
//		w.onCreate();
//	}

}
