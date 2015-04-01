package com.lianxin.wx;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, GlobalVal.DATABASE_NAME, null, GlobalVal.DATABASE_VERSION);
	}
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + GlobalVal.TABLE_TERMINALNUM + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
		//创建点菜器编号表时默认三个编号，后期可以修改
		db.execSQL("insert into " + GlobalVal.TABLE_TERMINALNUM + "(name)values('001')");
		db.execSQL("insert into " + GlobalVal.TABLE_TERMINALNUM + "(name)values('002')");
		db.execSQL("insert into " + GlobalVal.TABLE_TERMINALNUM + "(name)values('003')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
	}

}
