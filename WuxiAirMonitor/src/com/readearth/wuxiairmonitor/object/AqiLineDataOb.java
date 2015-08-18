package com.readearth.wuxiairmonitor.object;

import java.util.List;

public class AqiLineDataOb {

	List<HourData> StationHourData;

	public class HourData {
		String siteID, PositionName, SO2, NO2, CO, O31, Pm10, PM2_5, AQI,
				TimePoint;

		/**
		 * @return siteID
		 */
		public String getSiteID() {
			return siteID;
		}

		/**
		 * @param siteID
		 *            要设置的 siteID
		 */
		public void setSiteID(String siteID) {
			this.siteID = siteID;
		}

		/**
		 * @return positionName
		 */
		public String getPositionName() {
			return PositionName;
		}

		/**
		 * @param positionName
		 *            要设置的 positionName
		 */
		public void setPositionName(String positionName) {
			PositionName = positionName;
		}

		/**
		 * @return sO2
		 */
		public String getSO2() {
			return SO2;
		}

		/**
		 * @param sO2
		 *            要设置的 sO2
		 */
		public void setSO2(String sO2) {
			SO2 = sO2;
		}

		/**
		 * @return nO2
		 */
		public String getNO2() {
			return NO2;
		}

		/**
		 * @param nO2
		 *            要设置的 nO2
		 */
		public void setNO2(String nO2) {
			NO2 = nO2;
		}

		/**
		 * @return cO
		 */
		public String getCO() {
			return CO;
		}

		/**
		 * @param cO
		 *            要设置的 cO
		 */
		public void setCO(String cO) {
			CO = cO;
		}

		/**
		 * @return o31
		 */
		public String getO31() {
			return O31;
		}

		/**
		 * @param o31
		 *            要设置的 o31
		 */
		public void setO31(String o31) {
			O31 = o31;
		}

		/**
		 * @return pm10
		 */
		public String getPm10() {
			return Pm10;
		}

		/**
		 * @param pm10
		 *            要设置的 pm10
		 */
		public void setPm10(String pm10) {
			Pm10 = pm10;
		}

		/**
		 * @return pM2_5
		 */
		public String getPM2_5() {
			return PM2_5;
		}

		/**
		 * @param pM2_5
		 *            要设置的 pM2_5
		 */
		public void setPM2_5(String pM2_5) {
			PM2_5 = pM2_5;
		}

		/**
		 * @return aQI
		 */
		public String getAQI() {
			return AQI;
		}

		/**
		 * @param aQI
		 *            要设置的 aQI
		 */
		public void setAQI(String aQI) {
			AQI = aQI;
		}

		/**
		 * @return timePoint
		 */
		public String getTimePoint() {
			return TimePoint;
		}

		/**
		 * @param timePoint
		 *            要设置的 timePoint
		 */
		public void setTimePoint(String timePoint) {
			TimePoint = timePoint;
		}

		/*
		 * （非 Javadoc）
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HourData [siteID=" + siteID + ", PositionName="
					+ PositionName + ", SO2=" + SO2 + ", NO2=" + NO2 + ", CO="
					+ CO + ", O31=" + O31 + ", Pm10=" + Pm10 + ", PM2_5="
					+ PM2_5 + ", AQI=" + AQI + ", TimePoint=" + TimePoint + "]";
		}

		public String getValue(ValueType type) {
			String str = getValue1(type);
			if (str == null | str.equalsIgnoreCase(""))
				return "0";
			else
				return str;
		}

		private String getValue1(ValueType type) {
			switch (type) {
			case aqi:
				return AQI;
			case pm25:
				return PM2_5;
			case pm10:
				return Pm10;
			case so2:
				return SO2;
			case no2:
				return NO2;
			case co:
				return CO;
			case o3:
				return O31;
			default:
				return "-";
			}

		}
	}

	public enum ValueType {
		aqi, pm25, pm10, so2, no2, co, o3,
	}

	/**
	 * @return stationHourData
	 */
	public List<HourData> getStationHourData() {
		return StationHourData;
	}

	/**
	 * @param stationHourData
	 *            要设置的 stationHourData
	 */
	public void setStationHourData(List<HourData> stationHourData) {
		StationHourData = stationHourData;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AqiLineDataOb [StationHourData=" + StationHourData + "]";
	}

}
