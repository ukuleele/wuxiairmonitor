package com.readearth.wuxiairmonitor.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.format.DateFormat;

import com.readearth.wuxiairmonitor.object.AqiItemVo;
import com.readearth.wuxiairmonitor.object.AqiLineDataOb.HourData;
import com.readearth.wuxiairmonitor.object.AqiLineDataOb.ValueType;

public class ChartUtil {

	public static List<String> getLineChartList(List<AqiItemVo> table) {
		List<String> listData = new ArrayList<String>();
		int rowCount = table.size();
		String firstDate = "";
		String endDate = "";
		for (int i = 0; i < rowCount; i++) {
			AqiItemVo vo = table.get(i);
			// 193$3$54.5$12:00 54.5$12:00 106.6$11:00$05-24$05-25
			// 2014/05/24 14:00:00
			String xyStr = vo.getAQI() + "$"
					+ vo.getLST_AQI().substring(11, 16);
			if (i == 0) {
				xyStr = "0$0$" + xyStr;
				firstDate = vo.getLST_AQI().substring(5, 10);
			} else if (i == rowCount - 1) {
				endDate = vo.getLST_AQI().substring(5, 10);
				xyStr = xyStr + "$" + firstDate + "$" + endDate;
			}
			listData.add(xyStr);
		}
		return listData;
	}

	/**
	 * 为七种输入类型转换为曲线所需的结构
	 * 
	 * @param datalist
	 *            输入包含所有类型和时间的列表
	 * @param type
	 *            输入选择的化学物质类型
	 * @return 用于转换绘制曲线图的数据列表
	 */
	public static List<String> getLineChartListToAll(List<HourData> datalist,
			ValueType type) {
		List<String> listData = new ArrayList<String>();
		int rowCount = datalist.size();
		String firstDate = "";
		String endDate = "";
		SimpleDateFormat fmt1 = new SimpleDateFormat("MM/dd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("HH:mm");

		for (int i = 0; i < rowCount; i++) {
			HourData data = datalist.get(i);
			// 193$3$54.5$12:00 54.5$12:00 106.6$11:00$05-24$05-25
			// 2014/05/24 14:00:00
			@SuppressWarnings("deprecation")
			Date time = new Date(data.getTimePoint());

			String xyStr = data.getValue(type) + "$" + fmt2.format(time);
			if (i == 0) {
				xyStr = "0$0$" + xyStr;
				firstDate = fmt1.format(time);
			} else if (i == rowCount - 1) {
				endDate = fmt1.format(time);
				xyStr = xyStr + "$" + firstDate + "$" + endDate;
			}
			listData.add(xyStr);
		}
		return listData;
	}

	public static List<String> getBarChartList(List<AqiItemVo> table) {
		try {
			List<String> listData = new ArrayList<String>();
			int rowCount = table.size();

			for (int i = 0; i < rowCount; i++) {
				AqiItemVo vo = table.get(i);

				String xyStr = vo.getLST_AQI().substring(5, 10) + "$"
						+ vo.getAQI();

				listData.add(xyStr);
			}
			return listData;
		} catch (NullPointerException e) {
			return null;
		}
	}

}
