package com.readearth.wuxiairmonitor.utils;

import com.readearth.wuxiairmonitor.Constants;

public class UrlFactory implements Constants {
	public static final String HOST_PREFORE = "http://219.233.250.38:8087/semcwx/PatrolHandler.do?provider=MMShareBLL.DAL.WebAQI.Iphone&";

	public static String parseStationUrl(double longitude, double latitude,
			String imei, String siteIDs, String groupIDs) {
		String requestUrl = REQUEST_HOST + "method=IPhoneSiteData&lat="
				+ latitude + "&lng=" + longitude + "&IMEI=" + imei
				+ "&siteIDs=" + siteIDs + "&groupIDs=" + groupIDs;
		return requestUrl;
	}

	public static String aqiLineUrl(String id) {
		String requestUrl = "http://58.214.20.227:10004/semcwx/PatrolHandler.do?provider=MMShareBLL.DAL.WebAQI.Iphone&method=GetStationHourData&siteID=xidxA";
		requestUrl = requestUrl.replace("xidx", id);
		return requestUrl;
	}

	public static String parseSiteDataUrl(double longitude, double latitude,
			String imei, String siteIDs, String groupIDs) {
		String strLon = "-1";
		String strLat = "-1";

		if (longitude > 0 && latitude > 0) {
			strLon = String.valueOf(longitude);
			strLat = String.valueOf(latitude);
		}

		String requestUrl = REQUEST_HOST + "method=AndroidSiteData&lat="
				+ strLat + "&lng=" + strLon + "&IMEI=" + imei + "&siteIDs="
				+ siteIDs + "&groupIDs=" + groupIDs;
		return requestUrl;
	}

	public static String parseHistoryUrl(double longitude, double latitude,
			String imei, String id) {
		String historyUrl = null;
		String strLon = "-1";
		String strLat = "-1";
		String flag = "0";
		if (id.equalsIgnoreCase("201"))
			flag = "1";

		if (longitude > 0 && latitude > 0) {
			strLon = String.valueOf(longitude);
			strLat = String.valueOf(latitude);
		}

		historyUrl = REQUEST_HOST + "method=IphoneChart&lat=" + strLat
				+ "&lng=" + strLon + "&IMEI=" + imei + "&ID=" + id + "&flag="
				+ flag;
		return historyUrl;
	}

	public static String getForecatUrl() {
		String str = HOST
				+ "provider=MMShareBLL.DAL.WebAQI.Iphone&method=ZoneForecastData";
		return str;
	}

}
