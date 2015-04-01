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
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
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
