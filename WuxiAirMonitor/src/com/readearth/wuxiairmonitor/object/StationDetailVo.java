package com.readearth.wuxiairmonitor.object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class StationDetailVo implements Serializable,
		Comparable<StationDetailVo> {

	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 站号
	 */
	private String stationID;

	/**
	 * 站名称
	 */
	private String stationName;

	/**
	 * 站点类型，国控点、上海市控点、区站点
	 */
	private String stationType;

	private Boolean isGroup = false;// 因为站点名和区号可能相等，所以加一个标示，如果是true的话是区号。

	/**
	 * 站点的属于哪一类
	 */
	private String groupParentID;
	private String LST_AQI;
	private boolean isHasData = true;

	public int showOrder = 999;

	/**
	 * 经度
	 */
	private double longitude;

	/**
	 * 纬度
	 */
	private double latitude;

	/**
	 * 是否为离用户最近的站点
	 */
	private boolean bNearestStation = false;

	/**
	 * 最近的距离
	 */
	private double nearestDistance;

	/**
	 * pm25的AQI指数
	 */
	private int pm25AQI;

	/**
	 * pm25的浓度值
	 */
	private double pm25Value;
	private int pm25Grade;
	private String pm25Quality;

	/**
	 * pm10的AQI指数
	 */
	private int pm10AQI;

	/**
	 * pm10的浓度值
	 */
	private double pm10Value;
	private int pm10Grade;
	private String pm10Quality;

	/**
	 * o3的AQI指数
	 */
	private int o3AQI;

	/**
	 * o3的浓度值
	 */
	private double o3Value;
	private int o3Grade;
	private String o3Quality;

	/**
	 * co的AQI指数
	 */
	private int coAQI;

	/**
	 * co的浓度值
	 */
	private double coValue;
	private int coGrade;
	private String coQuality;

	/**
	 * so2的AQI指数
	 */
	private int so2AQI;

	/**
	 * so2的浓度值
	 */
	private double so2Value;
	private int so2Grade;
	private String so2Quality;

	/**
	 * no2的AQI值
	 */
	private int no2AQI;

	/**
	 * no2的浓度值
	 */
	private double no2Value;
	private int no2Grade;
	private String no2Quality;

	/**
	 * 首要污染物的类型
	 */
	private String primaryPollutantType;
	private int primaryPollutantAQI;
	private double primaryPollutantValue;
	private int primaryPollutantGrade;
	private String primaryPollutantQuality;

	/**
	 * 当前站点需要展示的污染物类型
	 */
	private int aqi;

	private String displayType;

	private String displayValue;

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = primaryPollutantType;
	}

	public int getDisplayGrade() {
		int curGrade = 1;
		String currentDisplayType = this.getDisplayType().toUpperCase();
		if (currentDisplayType.equalsIgnoreCase("AQI")) {
			curGrade = this.getPrimaryPollutantGrade();
		} else if (currentDisplayType.equalsIgnoreCase("PM2.5")) {
			curGrade = this.getPm25Grade();
		} else if (currentDisplayType.equalsIgnoreCase("PM10")) {
			curGrade = this.getPm10Grade();
		} else if (currentDisplayType.equalsIgnoreCase("SO2")) {
			curGrade = this.getSo2Grade();
		} else if (currentDisplayType.equalsIgnoreCase("CO")) {
			curGrade = this.getCoGrade();
		} else if (currentDisplayType.equalsIgnoreCase("NO2")) {
			curGrade = this.getNo2Grade();
		} else if (currentDisplayType.equalsIgnoreCase("O3")) {
			curGrade = this.getO3Grade();
		}

		return curGrade;
	}

	public String getDisplayValue() {
		String currentDisplayType = null;
		try {
			currentDisplayType = this.getDisplayType().toUpperCase();
		} catch (Exception e) {
			currentDisplayType = "AQI";
			Log.w("mytag",
					"Network data error: DispalyType of StationDetail is null");
		}
		double currentValue = 0;
		if (currentDisplayType.equalsIgnoreCase("AQI")) {
			currentValue = this.getPrimaryPollutantAQI();
		} else if (currentDisplayType.equalsIgnoreCase("PM2.5")) {
			currentValue = this.getPm25Value();
		} else if (currentDisplayType.equalsIgnoreCase("PM10")) {
			currentValue = this.getPm10Value();
		} else if (currentDisplayType.equalsIgnoreCase("SO2")) {
			currentValue = this.getSo2Value();
		} else if (currentDisplayType.equalsIgnoreCase("CO")) {
			currentValue = this.getCoValue();
		} else if (currentDisplayType.equalsIgnoreCase("NO2")) {
			currentValue = this.getNo2Value();
		} else if (currentDisplayType.equalsIgnoreCase("O3")) {
			currentValue = this.getO3Value();
		}

		return getDisplayValueText(currentValue, currentDisplayType);
	}

	/**
	 * 获得浓度值的表示
	 * 
	 * @param value
	 * @return
	 */
	public static String getDisplayValueText(double value, String type) {
		if (value <= 0.0)
			return "—";

		if (!type.equalsIgnoreCase("CO")) {
			if (value >= 1) {
				return String.format("%.0f", value);
			} else if (value > 0) {
				return String.format("%.1f", value);
			}
		} else {
			return String.format("%.1f", value);
		}
		return "—";
	}

	public void setDisplayValue(String dsiplayValue) {
		this.displayValue = dsiplayValue;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getPrimaryPollutantType() {
		return primaryPollutantType;
	}

	public void setPrimaryPollutantType(String primaryPollutantType) {
		this.primaryPollutantType = primaryPollutantType;
		displayType = primaryPollutantType;
	}

	public String getStationID() {
		return stationID;
	}

	public void setStationID(String stationID) {
		this.stationID = stationID;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getGroupParentID() {
		return groupParentID;
	}

	public void setGroupParentID(String groupParentID) {
		this.groupParentID = groupParentID;
	}

	public boolean isbNearestStation() {
		return bNearestStation;
	}

	public void setbNearestStation(boolean bNearestStation) {
		this.bNearestStation = bNearestStation;
	}

	public double getNearestDistance() {
		return nearestDistance;
	}

	public void setNearestDistance(double nearestDistance) {
		this.nearestDistance = nearestDistance;
	}

	public int getPm25AQI() {
		return pm25AQI;
	}

	public void setPm25AQI(int pm25aqi) {
		pm25AQI = pm25aqi;
	}

	public double getPm25Value() {
		return pm25Value;
	}

	public void setPm25Value(double pm25Value) {
		this.pm25Value = pm25Value;
	}

	public int getPm10AQI() {
		return pm10AQI;
	}

	public void setPm10AQI(int pm10aqi) {
		pm10AQI = pm10aqi;
	}

	public double getPm10Value() {
		return pm10Value;
	}

	public void setPm10Value(double pm10Value) {
		this.pm10Value = pm10Value;
	}

	public int getO3AQI() {
		return o3AQI;
	}

	public void setO3AQI(int o3aqi) {
		o3AQI = o3aqi;
	}

	public double getO3Value() {
		return o3Value;
	}

	public void setO3Value(double o3Value) {
		this.o3Value = o3Value;
	}

	public int getCoAQI() {
		return coAQI;
	}

	public void setCoAQI(int coAQI) {
		this.coAQI = coAQI;
	}

	public double getCoValue() {
		return coValue;
	}

	public void setCoValue(double coValue) {
		this.coValue = coValue;
	}

	public int getSo2AQI() {
		return so2AQI;
	}

	public void setSo2AQI(int so2aqi) {
		so2AQI = so2aqi;
	}

	public double getSo2Value() {
		return so2Value;
	}

	public void setSo2Value(double so2Value) {
		this.so2Value = so2Value;
	}

	public int getNo2AQI() {
		return no2AQI;
	}

	public void setNo2AQI(int no2aqi) {
		no2AQI = no2aqi;
	}

	public double getNo2Value() {
		return no2Value;
	}

	public void setNo2Value(double no2Value) {
		this.no2Value = no2Value;
	}

	public String getLST_AQI() {
		return LST_AQI;
	}

	public void setLST_AQI(String lST_AQI) {
		LST_AQI = lST_AQI;
	}

	public boolean isHasData() {
		return isHasData;
	}

	public void setHasData(boolean isHasData) {
		this.isHasData = isHasData;
	}

	public int getPm25Grade() {
		return pm25Grade;
	}

	public void setPm25Grade(int pm25Grade) {
		this.pm25Grade = pm25Grade;
	}

	public String getPm25Quality() {
		return pm25Quality;
	}

	public void setPm25Quality(String pm25Quality) {
		this.pm25Quality = pm25Quality;
	}

	public int getPm10Grade() {
		return pm10Grade;
	}

	public void setPm10Grade(int pm10Grade) {
		this.pm10Grade = pm10Grade;
	}

	public String getPm10Quality() {
		return pm10Quality;
	}

	public void setPm10Quality(String pm10Quality) {
		this.pm10Quality = pm10Quality;
	}

	public int getO3Grade() {
		return o3Grade;
	}

	public void setO3Grade(int o3Grade) {
		this.o3Grade = o3Grade;
	}

	public String getO3Quality() {
		return o3Quality;
	}

	public void setO3Quality(String o3Quality) {
		this.o3Quality = o3Quality;
	}

	public int getCoGrade() {
		return coGrade;
	}

	public void setCoGrade(int coGrade) {
		this.coGrade = coGrade;
	}

	public String getCoQuality() {
		return coQuality;
	}

	public void setCoQuality(String coQuality) {
		this.coQuality = coQuality;
	}

	public int getSo2Grade() {
		return so2Grade;
	}

	public void setSo2Grade(int so2Grade) {
		this.so2Grade = so2Grade;
	}

	public String getSo2Quality() {
		return so2Quality;
	}

	public void setSo2Quality(String so2Quality) {
		this.so2Quality = so2Quality;
	}

	public int getNo2Grade() {
		return no2Grade;
	}

	public void setNo2Grade(int no2Grade) {
		this.no2Grade = no2Grade;
	}

	public String getNo2Quality() {
		return no2Quality;
	}

	public void setNo2Quality(String no2Quality) {
		this.no2Quality = no2Quality;
	}

	public int getPrimaryPollutantAQI() {
		return primaryPollutantAQI;
	}

	public String getPrimaryAQIText() {
		if (primaryPollutantAQI > 0)
			return String.valueOf(primaryPollutantAQI);
		return "—";
	}

	public void setPrimaryPollutantAQI(int primaryPollutantAQI) {
		this.primaryPollutantAQI = primaryPollutantAQI;
	}

	public double getPrimaryPollutantValue() {
		return primaryPollutantValue;
	}

	public void setPrimaryPollutantValue(double primaryPollutantValue) {
		this.primaryPollutantValue = primaryPollutantValue;
	}

	public int getPrimaryPollutantGrade() {
		return primaryPollutantGrade;
	}

	public void setPrimaryPollutantGrade(int primaryPollutantGrade) {
		this.primaryPollutantGrade = primaryPollutantGrade;
	}

	public String getPrimaryPollutantQuality() {
		if (primaryPollutantQuality == null)
			return "-";
		return primaryPollutantQuality;
	}

	public void setPrimaryPollutantQuality(String primaryPollutantQuality) {
		this.primaryPollutantQuality = primaryPollutantQuality;
	}

	/**
	 * 获得最新观测的时间
	 * 
	 * @return
	 */
	public String getLastUpDataTimeWithOutSecond() {
		String lst = getLST_AQI();
		return lst.substring(0, lst.length() - 3);
	}

	public Date getPollutantDate() {
		try {
			return sdf.parse(getLST_AQI());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public int compareTo(StationDetailVo another) {
		if (this.showOrder < another.showOrder)
			return -1;
		else
			return 1;
	}

}
