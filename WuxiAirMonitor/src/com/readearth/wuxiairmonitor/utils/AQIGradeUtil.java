package com.readearth.wuxiairmonitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;

public class AQIGradeUtil implements Constants {

	public static SimpleDateFormat sdf = new SimpleDateFormat("dd日HH时");

	// /**
	// * 根据污染物等级获得文字的颜色背景
	// *
	// * @param grade
	// * @return
	// */
	// public static int getTextViewBackGroupByGrade(int grade) {
	// if (grade == 1) {
	// return R.drawable.idx_excellent_frame;
	// } else if (grade == 2) {
	// return R.drawable.idx_good_frame;
	// } else if (grade == 3) {
	// return R.drawable.idx_low_frame;
	// } else if (grade == 4) {
	// return R.drawable.idx_midlle_frame;
	// } else if (grade == 5) {
	// return R.drawable.idx_high_frame;
	// } else if (grade == 6) {
	// return R.drawable.idx_heavily_frame;
	// } else {
	// return R.drawable.idx_nodatadefault_frame;
	// }
	// }

	// 根据API指数来获取等级
	public static void SetImageView(ImageView iv1, String API, Context context) {
		try {
			float a = Float.parseFloat(API);
			int grade = 1;
			try {
				if (a > 0 && a <= 50) {
					grade = 1;
				} else if (a > 50 && a <= 100) {
					grade = 2;
				} else if (a > 100 && a <= 150) {
					grade = 3;
				} else if (a > 150 && a <= 200) {
					grade = 4;
				} else if (a > 200 && a <= 300) {
					grade = 5;
				} else if (a > 300) {
					grade = 6;
				}
				iv1.setBackgroundColor(getStationColorByGrade(grade));
			} catch (Exception e) {

			}
		} catch (NumberFormatException e) {

		}
	}

	// 获得地图上标注的形式
	// public static int getMapTextViewBackGroup(int grade) {
	// if (grade == 1) {
	// return R.drawable.map_excellent_frame;
	// } else if (grade == 2) {
	// return R.drawable.map_good_frame;
	// } else if (grade == 3) {
	// return R.drawable.map_low_frame;
	// } else if (grade == 4) {
	// return R.drawable.map_midlle_frame;
	// } else if (grade == 5) {
	// return R.drawable.map_high_frame;
	// } else if (grade == 6) {
	// return R.drawable.map_heavily_frame;
	// } else {
	// return R.drawable.idx_nodatadefault_frame;
	// }
	// }

	/**
	 * 根据污染物级别获得卡通人物等级
	 * 
	 * @param grade
	 * @return
	 */
	public static int getImageViewSrcByGrade(int grade) {
		if (grade == 1) {
			return R.drawable.biaoqing1;
		} else if (grade == 2) {
			return R.drawable.biaoqing2;
		} else if (grade == 3) {
			return R.drawable.biaoqing3;
		} else if (grade == 4) {
			return R.drawable.biaoqing4;
		} else if (grade == 5) {
			return R.drawable.biaoqing5;
		} else if (grade == 6) {
			return R.drawable.biaoqing6;
		} else {
			return R.drawable.biaoqing1;
		}
	}

	/**
	 * 根据污染物级别获得颜色
	 * 
	 * @param grade
	 * @return
	 */
	public static int getStationColorByGrade(int grade) {
		if (grade == 1) {
			return COLOR_EXCELLENT;
		} else if (grade == 2) {
			return COLOR_GOOD;
		} else if (grade == 3) {
			return COLOR_LOW;
		} else if (grade == 4) {
			return COLOR_MIDDLE;
		} else if (grade == 5) {
			return COLOR_HIGH;
		} else if (grade == 6) {
			return COLOR_HEAVILY;
		} else {
			return COLOR_NODATA;
		}
	}

	/**
	 * 适用于百度地图的取色
	 * 
	 * @param grade
	 * @return
	 */
	public static int getStationColorByGrade2(int grade) {
		
		if (grade == 1) {
			return COLOR_EXCELLENT;
		} else if (grade == 2) {
			return COLOR_GOOD2;
		} else if (grade == 3) {
			return COLOR_LOW;
		} else if (grade == 4) {
			return COLOR_MIDDLE;
		} else if (grade == 5) {
			return COLOR_HIGH;
		} else if (grade == 6) {
			return COLOR_HEAVILY;
		} else {
			return COLOR_NODATA;
		}
	}

	/**
	 * 获得格式化的最新观测时间
	 * 
	 * @param vo
	 * @param context
	 * @return
	 */
	public static String getLatestDateString(StationDetailVo vo) {
		Date lstDt = vo.getPollutantDate();
		if (lstDt != null)
			return sdf.format(vo.getPollutantDate());
		return "";
	}

	/**
	 * 根据网页的传递过来的Html参数获得污染物类型
	 */
	public static String getPollutantTypeByWebHtml(String htmlText) {
		if (htmlText == null)
			return "";
		String type = "";
		if (htmlText.equalsIgnoreCase("PM<sub>2.5</sub>")) {
			type = "PM2.5";
		} else if (htmlText.equalsIgnoreCase("PM<sub>10</sub>")) {
			type = "PM10";
		} else if (htmlText.equalsIgnoreCase("NO<sub>2</sub>")) {
			type = "NO2";
		} else if (htmlText.equalsIgnoreCase("O<sub>3</sub>")) {
			type = "O3";
		} else if (htmlText.equalsIgnoreCase("SO<sub>2</sub>")) {
			type = "SO2";
		} else if (htmlText.equalsIgnoreCase("CO")) {
			type = "CO";
		}

		return type;

	}

	public static boolean isPrimaryPollutantTypeValid(String valueType) {
		if (valueType == null)
			return false;
		if (valueType.equalsIgnoreCase("—"))
			return false;
		if (valueType.equalsIgnoreCase(""))
			return false;
		return true;
	}

	/**
	 * 根据污染物类型获得相应的HTML显示
	 * 
	 * @param valueType
	 *            数据类型 PM2.5 PM10等等
	 * @return <font>PM</font><small>2.5</small>
	 */
	public static String getPollutantTypeHtml(String valueType) {
		if (valueType == null)
			valueType = "";
		String titleStyle = "<font>";
		String subStyle = "<small>";
		String titleStylePosfix = "</font>";
		String subStylePosfix = "</small>";
		String title = "";
		String sub = "";

		if (valueType.equalsIgnoreCase("PM2.5")) {
			title = "PM";
			sub = "2.5";
		} else if (valueType.equalsIgnoreCase("PM10")) {
			title = "PM";
			sub = "10";
		} else if (valueType.equalsIgnoreCase("O3")) {
			title = "O";
			sub = "3";
		} else if (valueType.equalsIgnoreCase("CO")) {
			title = "CO";
			sub = "";
		} else if (valueType.equalsIgnoreCase("SO2")) {
			title = "SO";
			sub = "2";
		} else if (valueType.equalsIgnoreCase("NO2")) {
			title = "NO";
			sub = "2";
		}

		String htmlText = titleStyle + title + titleStylePosfix;
		if (!sub.equalsIgnoreCase("")) {
			htmlText = htmlText + subStyle + sub + subStylePosfix;
		}

		return htmlText;
	}

	public static Spanned getPollutantTypeValueSpanned(StationDetailVo vo,
			Context context) {
		String valueType = vo.getPrimaryPollutantType();
		if (valueType == null)
			valueType = "";
		String htmlString = getPollutantTypeHtml(valueType);

		String valueUnit = context.getString(R.string.ugm3_text);
		if (valueType.equalsIgnoreCase("CO")) {
			valueUnit = context.getString(R.string.mgm3_text);
		}

		htmlString = htmlString + "<font>" + " "
				+ getDisplayValueText(vo.getPrimaryPollutantValue(), valueType)
				+ " " + valueUnit + "</font>";
		Spanned type = Html.fromHtml(htmlString);
		return type;
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

	/**
	 * 根据等级获得建议措施
	 * 
	 * @param grade
	 * @param context
	 * @return
	 */
	public static String getSuggestionByGrade(int grade, Context context) {
		String[] suggestions = context.getResources().getStringArray(
				R.array.suggestion_array);
		return suggestions[grade - 1];
	}

	/**
	 * 根据污染物级别获得颜色
	 * 
	 * @param grade
	 * @return
	 */
	public static int getStationColorByGrade(int grade, Context context) {
		if (grade == 1) {
			return context.getResources().getColor(
					R.color.condition_excellent_color);
		} else if (grade == 2) {
			return context.getResources()
					.getColor(R.color.condition_good_color);
		} else if (grade == 3) {
			return context.getResources().getColor(R.color.condition_low_color);
		} else if (grade == 4) {
			return context.getResources().getColor(
					R.color.condition_middle_color);
		} else if (grade == 5) {
			return context.getResources()
					.getColor(R.color.condition_high_color);
		} else if (grade == 6) {
			return context.getResources().getColor(
					R.color.conditon_heavily_color);
		} else {
			return context.getResources().getColor(
					R.color.condition_nodatadefault_color);
		}
	}

	/**
	 * 根据等级获得对健康的影响
	 * 
	 * @param grade
	 * @param context
	 * @return
	 */
	public static String getEffectsByGrade(int grade, Context context) {
		String[] healths = context.getResources().getStringArray(
				R.array.health_array);
		return healths[grade - 1];
	}

	public static String getGradeText(int grade, Context context) {
		String[] classTexts = context.getResources().getStringArray(
				R.array.condition_array);
		return classTexts[grade - 1];
	}

	public static String getAqiRangeText(int grade, Context context) {
		String[] classTexts = context.getResources().getStringArray(
				R.array.aqiRange_array);
		return classTexts[grade - 1];
	}

	public static String getValueRangeText(int grade, Context context) {
		String[] classTexts = context.getResources().getStringArray(
				R.array.aqiValue_array);
		return classTexts[grade - 1];
	}

	public static String getPollutionCNName(String type) {
		if (type == null)
			return "-";
		switch (type) {
		case "PM2.5":
			return "细颗粒物 (PM2.5)";
		case "PM10":
			return "颗粒物 (PM10)";
		case "SO2":
			return "二氧化硫 (SO2)";
		case "CO":
			return "一氧化碳 (CO)";
		case "NO2":
			return "二氧化氮 (NO2)";
		case "O3":
			return "臭氧 (O3)";
		default:
			return "-";

		}
	}

}
