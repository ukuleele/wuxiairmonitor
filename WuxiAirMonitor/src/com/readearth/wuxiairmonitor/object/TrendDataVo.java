package com.readearth.wuxiairmonitor.object;

import java.io.Serializable;
import java.util.List;

public class TrendDataVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 实时指数
	 */
	private List<AqiItemVo> TableRT;
	
	/**
	 * 24小时滑动指数
	 */
	private List<AqiItemVo> Table24;
	
	/**
	 * 近30天日平均AQI
	 */
	private List<AqiItemVo> Table30;

	public List<AqiItemVo> getTableRT() {
		return TableRT;
	}

	public void setTableRT(List<AqiItemVo> tableRT) {
		TableRT = tableRT;
	}

	public List<AqiItemVo> getTable24() {
		return Table24;
	}

	public void setTable24(List<AqiItemVo> table24) {
		Table24 = table24;
	}

	public List<AqiItemVo> getTable30() {
		return Table30;
	}

	public void setTable30(List<AqiItemVo> table30) {
		Table30 = table30;
	}
	
	

	
	

}
