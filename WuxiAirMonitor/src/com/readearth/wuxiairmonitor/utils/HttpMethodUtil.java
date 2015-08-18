package com.readearth.wuxiairmonitor.utils;

import com.readearth.wuxiairmonitor.Constants;

public class HttpMethodUtil implements Constants{
	
	
	
	/**
	 * 获得Android最新的报警信息
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param imei 手机序列号
	 * @return
	 */
	public static String parseWarnUrl(double longitude,double latitude,String imei)
	{
		String requestUrl=REQUEST_HOST +"method=AndroidWarningTable&lat="+latitude+"&lng="+longitude+"&IMEI=" + imei;
		return requestUrl;
	}
	
	

	/**
	 * 获得Android最新的手机消息
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param imei 手机序列号
	 * @return
	 */
	public static String parsePhoneMessageUrl(double longitude,double latitude,String imei)
	{
		String requestUrl=REQUEST_HOST +"method=AndroidMessageTable&lat="+latitude+"&lng="+longitude+"&IMEI=" + imei;
		return requestUrl;
	}
	
	
	
	
	
	/**
	 * 获得数据版本信息的请求URL
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @return
	 */
	public static String parseVersionUrl(double longitude,double latitude,String imei)
	{
		String requestUrl=REQUEST_HOST +"method=getAndroidVersion&lat="+latitude+"&lng="+longitude+"&IMEI=" + imei;
		return requestUrl;
	}
	
	
	/**
	 * 返回既有站点经纬度又有观测数据的URL
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @param siteIDs
	 * @param groupIDs
	 * @return
	 */
	public static String parseSiteDataUrl(double longitude,double latitude,String imei,String siteIDs,String groupIDs)
	{
		String strLon = "-1";
		String strLat = "-1";
		
		if(longitude >0 && latitude >0)
		{
			strLon = String.valueOf(longitude);
			strLat =String.valueOf(latitude);
		}
		
		String requestUrl=REQUEST_HOST +"method=AndroidSiteData&lat="+strLat+"&lng="+strLon+"&IMEI="+imei+"&siteIDs="+siteIDs+"&groupIDs="+ groupIDs;
		return requestUrl;
	}
	
	
	/**
	 * 返回只有站点观测信息的URL
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @param siteIDs
	 * @param groupIDs
	 * @return
	 */
	public static String parseDataUrl(double longitude,double latitude,String imei,String siteIDs,String groupIDs)
	{
		String requestUrl=REQUEST_HOST +"method=AndroidDataSiteData&lat="+latitude+"&lng="+longitude+"&IMEI="+imei+"&siteIDs="+siteIDs+"&groupIDs="+ groupIDs;
		return requestUrl;
	}
	
	
	/**
	 * 返回只有观测经纬度信息的URL
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @param siteIDs
	 * @param groupIDs
	 * @return
	 */
	public static String parseSiteUrl(double longitude,double latitude,String imei,String siteIDs,String groupIDs)
	{
		String requestUrl=REQUEST_HOST +"method=AndroidBasicSiteData&lat="+latitude+"&lng="+longitude+"&IMEI="+imei+"&siteIDs="+siteIDs+"&groupIDs="+ groupIDs;
		return requestUrl;
	}
	
	
	
	
	/**
	 * 获得分区空气质量预报URL
	 */
	public static String parseForecastDataUrl(double longitude,double latitude,String imei,String groupID)
	{
		String reqUrl=REQUEST_HOST + "method=AndroidForecastData&IMEI="+imei+"&lat="+latitude+"&lng="+longitude+"&groupID="+ groupID;
		return reqUrl;
	}
	
	/**
	 * 根据分区ID获得该时刻所有分站点的数据
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @param groupID
	 * @return
	 */
	public static String parseStationListByGroupUrl(double longitude,double latitude,String imei,String groupID)
	{
		String reqUrl=REQUEST_HOST + "method=AndroidGroupData&groupID="+groupID+"&IMEI="+imei+"&lat="+latitude+"&lng=" + longitude;
		return reqUrl;
	}
	
	
	/**
	 * 获得站点的历史曲线数据
	 * @param longitude 经度
	 * @param latitude 维度
	 * @param imei 用户设备号
	 * @param stationID 站点编号
	 * @param flag 1为分区数据，0为站点数据
	 * @return 返回Http请求Url
	 */
	public static String parseHistoryDataUrl(double longitude,double latitude,String imei,String stationID,int flag)
	{
		String reqUrl=REQUEST_HOST +  "method=AndroidChart&IMEI="+imei+"&lat="+latitude+"&lng="+longitude+"&ID="+stationID+"&flag=" + flag;
		return reqUrl;
	}
	
	
	
	/**
	 * 用户第一次使用上传移动设备信息的请求Url
	 * @param longitude
	 * @param latitude
	 * @param imei
	 * @param phoneNumber
	 * @param osVersion
	 * @param softVersion
	 * @return
	 */
	public static String parseUploadDeviceUrl(double longitude,double latitude,String imei,String phoneNumber,String osVersion,String softVersion){
		String reqUrl=REQUEST_HOST  + "method=AndroidinsertIntoVersion&IMEI="+imei+"&lat="+latitude+"&lng="+longitude+"&phoneNumer="+phoneNumber+"&OSVersion="+osVersion+"&SoftVersion=" + softVersion;
		return reqUrl;
	}

}
