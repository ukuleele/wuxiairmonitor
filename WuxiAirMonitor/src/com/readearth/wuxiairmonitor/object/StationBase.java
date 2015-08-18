package com.readearth.wuxiairmonitor.object;

public class StationBase {
	String id;
	String name;
	String area;
	double longitude;
	double latitude;
	// 是否被选中1为是，0为否
	int isSelected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public boolean getIsSelectedBool() {
		switch (isSelected) {
		case 0:
			return false;
		case 1:
			return true;
		default:
			return false;
		}
	}

}
