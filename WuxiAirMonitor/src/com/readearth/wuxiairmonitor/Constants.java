package com.readearth.wuxiairmonitor;

public interface Constants {
//	public static final String REQUEST_HOST = "http://219.233.250.38:8087/semcwx/PatrolHandler.do?provider=MMShareBLL.DAL.WebAQI.Iphone&";
	public static final String HOST = "http://58.214.20.227:10004/semcwx/PatrolHandler.do?";
	public static final String REQUEST_HOST = "http://58.214.20.227:10004/semcwx/PatrolHandler.do?provider=MMShareBLL.DAL.WebAQI.Iphone&";
	
	public static final String GROUPID = "201";

	// Intent
	public static final String INTENT_START = "start";
	public static final String INTENT_SELETED_STATION = "selected_station";
	public static final String INTENT_MAP_ACTIVITY = "map_actity_extra";
	public static final String INTENT_SCAN_ACTIVITY = "scan_actity_extra";
	public static final String INTENT_PAGE_NUM = "page_num";
	public static final String INTENT_SCAN_ID = "scan_id";
	
	public static final String ACTION_REALTIME = "to_realtime";
	public static final String ACTION_HISTORY = "to_history";
	public static final String ACTION_CONCENTRATION = "to_c";
	public static final String ACTION_OPTION = "to_option";
	public static final String ACTION_NOTHING = "to_nothing";
	
	public static final int RESULT_CODE_ORDER_STATION = 1993;
	public static final int RESULT_CODE_SELECT_STATION = 1991;
	public static final int REQUEST_CODE_SELECT_STATION = 1992;
	public static final int REQUEST_CODE_ORDER_STATION = 1994;
	public static final int RESULT_CODE_MAP = 2001;
	public static final int REQUEST_CODE_MAP = 2002;
	public static final int RESULT_CODE_SCAN = 2003;
	public static final int REQUEST_CODE_SCAN = 2004;

	// Key
	public static final String KEY_BUNDLE_STATION = "bundle_station";

	public static final int COLOR_EXCELLENT = 0xff00ff00;
	public static final int COLOR_GOOD = 0xffffff00;
	public static final int COLOR_GOOD2 = 0XFFE0D500;
	public static final int COLOR_LOW = 0xffffa500;
	public static final int COLOR_MIDDLE = 0xffff0000;
	public static final int COLOR_HIGH = 0xff800080;
	public static final int COLOR_HEAVILY = 0xff7e0023;
	public static final int COLOR_NODATA = 0xffeeeeee;

	// Database
	public static final String TABLE_STATIONBASE = "stations";
	public static final String COLUMN_ID = "stationId";
	public static final String COLUMN_NAME = "stationName";
	public static final String COLUMN_Area = "Area";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_ISSELECTED = "isSelected";

	// SharePreference
	public static final String SP_NAME = "offline_data";
	public static final String SP_SHTATION_KEY = "station_data";
	public static final String SP_HISTORY_KEY = "history_data";
	public static final String SP_STATION_IDS = "station_ids";

}
