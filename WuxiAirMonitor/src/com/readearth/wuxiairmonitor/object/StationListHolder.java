package com.readearth.wuxiairmonitor.object;

import java.io.Serializable;
import java.util.List;

public class StationListHolder implements Serializable {
	
	private List<StationDetailVo> stationList;

	public StationListHolder(List<StationDetailVo> stationList) {
		super();
		this.stationList = stationList;
	}

	public List<StationDetailVo> getStationList() {
		return stationList;
	}

	public void setStationList(List<StationDetailVo> stationList) {
		this.stationList = stationList;
	}
	
	
}
