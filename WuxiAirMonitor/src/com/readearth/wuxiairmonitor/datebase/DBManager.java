package com.readearth.wuxiairmonitor.datebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity.Header;
import android.provider.SyncStateContract.Helpers;
import android.util.Log;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.object.StationBase;

public class DBManager implements Constants {

	private SQLiteDatabase db;
	private Context context;

	public DBManager(Context context) {
		super();
		this.context = context;
	}

	public Map<String, StationBase> getStationBaseMap() {
		DBHelper helper = new DBHelper(context);
		db = helper.getReadableDatabase();
		Map<String, StationBase> stationMap = new HashMap<String, StationBase>();
		Cursor cursor = db.query(Constants.TABLE_STATIONBASE, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		do {
			StationBase stationBase = new StationBase();
			stationBase
					.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
			stationBase.setName(cursor.getString(cursor
					.getColumnIndex(COLUMN_NAME)));
			stationBase.setArea(cursor.getString(cursor
					.getColumnIndex(COLUMN_Area)));
			stationBase.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LONGITUDE)));
			stationBase.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LATITUDE)));
			stationBase.setIsSelected(cursor.getInt(cursor
					.getColumnIndex(COLUMN_ISSELECTED)));
			stationMap.put(stationBase.getId(), stationBase);
		} while (cursor.moveToNext());
		return stationMap;
	}

	public List<StationBase> getStationBaseList() {
		DBHelper helper = new DBHelper(context);
		db = helper.getReadableDatabase();
		List<StationBase> list = new ArrayList<StationBase>();
		Cursor cursor = db.query(Constants.TABLE_STATIONBASE, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		do {
			StationBase stationBase = new StationBase();
			stationBase
					.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
			stationBase.setName(cursor.getString(cursor
					.getColumnIndex(COLUMN_NAME)));
			stationBase.setArea(cursor.getString(cursor
					.getColumnIndex(COLUMN_Area)));
			stationBase.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LONGITUDE)));
			stationBase.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LATITUDE)));
			stationBase.setIsSelected(cursor.getInt(cursor
					.getColumnIndex(COLUMN_ISSELECTED)));
			list.add(stationBase);
		} while (cursor.moveToNext());
		return list;
	}

	/**
	 * @param idList
	 *            被选中的Id列表
	 */
	public void setIsSelected(List<String> idList) {
		DBHelper helper = new DBHelper(context);
		db = helper.getReadableDatabase();
		// 清空原设置
		db.execSQL("update " + TABLE_STATIONBASE + " set " + COLUMN_ISSELECTED
				+ " = 0");
		// 重新设置
		for (String id : idList) {
			db.execSQL("update " + TABLE_STATIONBASE + " set "
					+ COLUMN_ISSELECTED + " = 1 where " + COLUMN_ID + " = "
					+ id);
		}
	}

	/**
	 * @return 获得被选中的Id列表
	 */
	public List<String> getIsSelectedIdList() {
		DBHelper helper = new DBHelper(context);
		db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_STATIONBASE, null, COLUMN_ISSELECTED
				+ "= 1", null, null, null, null);
		List<String> idList = new ArrayList<String>();
		cursor.moveToFirst();
		try {
			do {
				idList.add(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
			} while (cursor.moveToNext());
		} catch (Exception e) {
			Log.i("mytag", "---cursor equal 0---");
		}
		return idList;
	}

	public List<StationBase> getStationBaseByIdList(List<String> ids) {
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		List<StationBase> list = new ArrayList<>();
		for (String id : ids) {
			Cursor cursor = db.query(TABLE_STATIONBASE, null, COLUMN_ID + " = "
					+ id, null, null, null, null);
			cursor.moveToFirst();
			StationBase stationBase = new StationBase();
			stationBase
					.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
			stationBase.setName(cursor.getString(cursor
					.getColumnIndex(COLUMN_NAME)));
			stationBase.setArea(cursor.getString(cursor
					.getColumnIndex(COLUMN_Area)));
			stationBase.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LONGITUDE)));
			stationBase.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(COLUMN_LATITUDE)));
			stationBase.setIsSelected(cursor.getInt(cursor
					.getColumnIndex(COLUMN_ISSELECTED)));
			list.add(stationBase);
		}

		return list;

	}
}
