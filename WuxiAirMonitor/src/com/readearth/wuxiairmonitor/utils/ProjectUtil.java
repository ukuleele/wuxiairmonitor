package com.readearth.wuxiairmonitor.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.object.StationBase;
import com.readearth.wuxiairmonitor.object.StationIdSP;

public class ProjectUtil implements Constants {

	public static final String DEFAULT_ID_JSON = "{\"idAndOrder\":[\"201\"]}";

	public static void setSharedPreference(Context context, String dataType,
			String dataValue) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(dataType, dataValue);
		editor.commit();
	}

	/**
	 * 获取SP中的离线数据
	 * 
	 * @param context
	 * @param dataType
	 *            实时离线数据、历史离线数据
	 * @return
	 */
	public static String getSharedPreference(Context context, String dataType) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		String returnStr = sp.getString(dataType, null);
		return returnStr;
	}

	/**
	 * 向SP存入String的id的list
	 * 
	 * @param context
	 * @param idList
	 */
	public static void setIdListSharedPreference(Context context,
			List<String> idList) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		Gson gson = new Gson();
		StationIdSP stationIdSP = new StationIdSP();
		stationIdSP.setIdAndOrder(idList);
		String idListStr = gson.toJson(stationIdSP);
		Editor editor = sp.edit();
		editor.putString(SP_STATION_IDS, idListStr);
		editor.commit();
	}

	/**
	 * 向SP存入StationBase的id的list
	 * 
	 * @param context
	 * @param staBaseList
	 */
	public static void setIdListSharedPreferenceSB(Context context,
			List<StationBase> staBaseList) {
		List<String> idList = new ArrayList<>();

		for (StationBase sta : staBaseList) {
			idList.add(sta.getId());
		}
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		Gson gson = new Gson();
		StationIdSP stationIdSP = new StationIdSP();
		stationIdSP.setIdAndOrder(idList);
		String idListStr = gson.toJson(stationIdSP);
		Editor editor = sp.edit();
		editor.putString(SP_STATION_IDS, idListStr);
		editor.commit();
	}

	public static List<String> getIdListSharedPreference(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		String idListStr = sp.getString(SP_STATION_IDS, null);
		Gson gson = new Gson();
		StationIdSP idListObj = (StationIdSP) gson.fromJson(idListStr,
				StationIdSP.class);
		List<String> idList = null;
		if (idListObj != null) {
			idList = idListObj.getIdAndOrder();
		}
		if (idList == null)
			idList = new ArrayList<>();
		if (idList.size() == 0)
			idList.add("201");
		return idList;
	}
}
