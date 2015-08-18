package com.readearth.wuxiairmonitor.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.utils.AQIGradeUtil;
import com.readearth.wuxiairmonitor.utils.AppUtil;

public class ScanListApapter extends BaseAdapter {
	LayoutInflater mInflater;
	Context context;
	// 传入的站点ID列表
	List<StationDetailVo> staList = null;

	public ScanListApapter(Context context, List<StationDetailVo> stationList) {
		super();
		this.context = context;
		this.staList = stationList;
	}

	@Override
	public int getCount() {
		return staList.size();
	}

	@Override
	public Object getItem(int position) {
		return staList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder h = null;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final StationDetailVo item = staList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_sacn, null);
			h = new ViewHolder();
			h.txtAqi = (TextView) convertView.findViewById(R.id.txt_scan_aqi);
			h.txtName = (TextView) convertView.findViewById(R.id.txt_scan_name);
			h.txtLevel = (TextView) convertView
					.findViewById(R.id.txt_scan_level);
			h.txtpolution = (TextView) convertView
					.findViewById(R.id.txt_sacn_polution);
			h.txtpm25 = (TextView) convertView.findViewById(R.id.txt_scan_pm25);
			h.txtpm10 = (TextView) convertView.findViewById(R.id.txt_scan_pm10);
			h.txtso2 = (TextView) convertView.findViewById(R.id.txt_scan_so2);
			h.txtno2 = (TextView) convertView.findViewById(R.id.txt_scan_no2);
			h.txto3 = (TextView) convertView.findViewById(R.id.txt_scan_o3);
			h.txtco = (TextView) convertView.findViewById(R.id.txt_scan_co);
			h.dataList = (LinearLayout) convertView
					.findViewById(R.id.linear_data_list);

			convertView.setTag(h);
		} else {
			// 界面容器
			h = (ViewHolder) convertView.getTag();
		}
		int flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
		String one, two, three;
		int start, end;
		// 设置txtName
		SpannableStringBuilder builder = new SpannableStringBuilder();
		one = staList.get(position).getStationName() + " ";
		two = staList.get(position).getPrimaryPollutantQuality();
		three = "首要污染物 "
				+ AQIGradeUtil.getPollutionCNName(staList.get(position)
						.getPrimaryPollutantType());
		start = 0;
		end = one.length();
		builder.append(one);
		builder.setSpan(new AbsoluteSizeSpan(AppUtil.dip2px(context, 20)),
				start, end, flag);
		// start = end;
		// end += two.length();
		// builder.append(two);
		// builder.setSpan(new AbsoluteSizeSpan(AppUtil.dip2px(context, 18)),
		// start, end, flag);
		// builder.setSpan(new ForegroundColorSpan(0xffffffff), start, end,
		// flag);
		 int color = AQIGradeUtil
		 .getStationColorByGrade2(staList.get(position)
		 .getPrimaryPollutantGrade());
		 if(color ==Constants.COLOR_NODATA)
		 color = 0xff474747;
		// builder.setSpan(new BackgroundColorSpan(color), start, end, flag);
		// start = end ;
		// end += three.length();
		// builder.append(three);
		// builder.setSpan(new AbsoluteSizeSpan(18), start, end, flag);
		StationDetailVo station = staList.get(position);
		h.txtName.setText(builder);
		h.txtLevel.setText(two);
		h.txtLevel.setBackgroundColor(color);
		h.txtpolution.setText(three);
		h.txtAqi.setText(staList.get(position).getPrimaryAQIText());
		h.txtpm25.setText("PM2.5\n" + fitle(station.getPm25Value()));
		h.txtpm10.setText("PM10\n" + station.getPm10AQI());
		h.txtso2.setText("SO2\n" + station.getSo2Value());
		h.txtno2.setText("NO2\n" + station.getNo2Value());
		h.txto3.setText("O3\n" + station.getO3Value());
		h.txtco.setText("CO\n" + station.getCoValue());

		// builder.append(text)

		return convertView;
	}

	private String fitle(double num) {
		if (num == 0)
			return "-";
		else
			return String.valueOf(num);
	}

	class ViewHolder {
		TextView txtName, txtLevel, txtAqi, txtpolution, txtpm25, txtpm10,
				txtso2, txtno2, txto3, txtco;
		LinearLayout dataList;
	}
}