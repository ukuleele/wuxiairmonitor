package com.readearth.wuxiairmonitor.object;

import java.io.Serializable;

public class AqiItemVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String LST_AQI;
	private String SiteID;
	private String SiteName;
	private String Parameter;
	private double Value;
	private int AQI;
	private int GradeID;
	private String Quality;
	
	
	public String getLST_AQI() {
		return LST_AQI;
	}
	public void setLST_AQI(String lST_AQI) {
		LST_AQI = lST_AQI;
	}
	public String getSiteID() {
		return SiteID;
	}
	public void setSiteID(String siteID) {
		SiteID = siteID;
	}
	public String getSiteName() {
		return SiteName;
	}
	public void setSiteName(String siteName) {
		SiteName = siteName;
	}
	public String getParameter() {
		return Parameter;
	}
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	public double getValue() {
		return Value;
	}
	public void setValue(double value) {
		Value = value;
	}
	public int getAQI() {
		return AQI;
	}
	public void setAQI(int aQI) {
		AQI = aQI;
	}
	public int getGradeID() {
		return GradeID;
	}
	public void setGradeID(int gradeID) {
		GradeID = gradeID;
	}
	public String getQuality() {
		return Quality;
	}
	public void setQuality(String quality) {
		Quality = quality;
	}
	
	
}
