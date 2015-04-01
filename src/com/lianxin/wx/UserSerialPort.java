package com.lianxin.wx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.ViewDebug.IntToString;

import com.posin.device.SerialPort;
import com.posin.device.SerialPortFinder;

public class UserSerialPort {

	private SerialPort serial = null;
	private InputStream reader = null;
	private OutputStream writer = null;
	private InputStreamReader bufReader = null;
	public InputStreamReader getBufReader() {
		return bufReader;
	}

	/*
	 * 
	 */
	public void start(int port){
		try {
			SerialPortFinder finder = SerialPortFinder.newInstance();
			String[] paths = finder.getAllDevicesPath();
			List<String> list = new ArrayList<String>();

			for(String path : paths) {
				if(path.contains("/dev/tty")) {
					list.add(path);
				}
			}
			serial = SerialPort.open(list.get(port).toString(), true);
			
			// 波特率，数据位，校验位，停止位，流控制
			serial.setParam(19200, 8, SerialPort.PARITY_NONE, 
					SerialPort.STOPBITS_1, SerialPort.FLOWCONTROL_NONE);
			
		//	writer = new BufferedWriter(new OutputStreamWriter(serial.getOutputStream()));
			writer = serial.getOutputStream();
			reader = serial.getInputStream();
			bufReader = new InputStreamReader(serial.getInputStream());
			
			Log.i("info", "SerialPort Open..."+list.get(port).toString());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public SerialPort getSerial() {
		return serial;
	}

	public void send(String msg){
		try {
			byte[] buf = new byte[6];
			int i = Integer.parseInt(msg);
			buf[0] = (byte)0;
			buf[1] = (byte)0;
			buf[2] = (byte)0;
			buf[3] = (byte)192;//0xc0
			buf[4] = (byte)1;
			buf[5] = (byte)i;
			writer.write(buf);
		//	Log.i("info", "send: "+(int)(buf[3]&0xff));
		//	writer.write((byte)i);
			writer.flush();
		//	Log.i("info", "send: "+msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receive(byte[] buffer){
		try {
			int i = 0;
		//	while(i==0)
				i = reader.available();
			reader.read(buffer, 0, i);
	//		Log.i("info","rece: "+ i + buffer.toString());
	//		reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		// 如果串口打开的先进行关闭串口
		if(serial != null) 
			serial.close();
		Log.i("info", "SerialPort Close...");
	}
}
