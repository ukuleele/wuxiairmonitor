package com.readearth.wuxiairmonitor.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.ForecastItem;

public class HorizontalLayout extends LinearLayout {
	private TextView textView11, textView12;
	private TextView textView21, textView22;
	private TextView textView31, textView32;

	private LinearLayout linearLayout1, linearLayout2, linearLayout3;

	private Context context;

	// private ForecastItem forecastItem;

	public HorizontalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		this.context = context;
		LayoutInflater mInflater = LayoutInflater.from(context);
		View v = mInflater.inflate(R.layout.horizontal_layout, this);
		textView11 = (TextView) v.findViewById(R.id.nearly_time1);
		textView21 = (TextView) v.findViewById(R.id.nearly_time2);
		textView31 = (TextView) v.findViewById(R.id.nearly_time3);
		textView12 = (TextView) v.findViewById(R.id.nearly_aqi1);
		textView22 = (TextView) v.findViewById(R.id.nearly_aqi2);
		textView32 = (TextView) v.findViewById(R.id.nearly_aqi3);

		linearLayout1 = (LinearLayout) v.findViewById(R.id.forecast_item1);
		linearLayout2 = (LinearLayout) v.findViewById(R.id.forecast_item2);
		linearLayout3 = (LinearLayout) v.findViewById(R.id.forecast_item3);
//		addView(v);
	}

	public void setForecastItem(ForecastItem forecastItem) {
		if (forecastItem == null){
			linearLayout1.setVisibility(View.GONE);
			return;
		}
		if (forecastItem.getH11() != null) {
			linearLayout1.setVisibility(View.VISIBLE);
			textView11.setText(forecastItem.getH11());
			textView12.setText(forecastItem.getH12());
		} else {
			linearLayout1.setVisibility(View.GONE);
		}
		if (forecastItem.getH21() != null) {
			linearLayout2.setVisibility(View.VISIBLE);
			textView21.setText(forecastItem.getH21());
			textView22.setText(forecastItem.getH22());
		} else {
			linearLayout2.setVisibility(View.GONE);
		}
		if (forecastItem.getH31() != null) {
			linearLayout3.setVisibility(View.VISIBLE);
			textView31.setText(forecastItem.getH31());
			textView32.setText(forecastItem.getH32());
		} else {
			linearLayout3.setVisibility(View.GONE);
		}

	}


}
