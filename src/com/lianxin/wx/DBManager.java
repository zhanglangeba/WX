package com.lianxin.wx;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
        helper = new DBOpenHelper(context);  
        //��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��  
        db = helper.getWritableDatabase();  
    }

	public boolean getTerminalNum(Vector<String> list) {
		Cursor c = db.rawQuery("select * from "+GlobalVal.TABLE_TERMINALNUM, null);
		if (c!=null) {
			while (c.moveToNext()) {
				list.add(c.getString(c.getColumnIndex("name")));
			}
			c.close();
			return true;
		}
		return false;
	}
	
	public void close(){
		db.close();
		helper.close();
	}
}
