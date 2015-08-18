package com.readearth.wuxiairmonitor.ui;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.AqiLineDataOb;
import com.readearth.wuxiairmonitor.object.AqiLineDataOb.HourData;
import com.readearth.wuxiairmonitor.object.AqiLineDataOb.ValueType;
import com.readearth.wuxiairmonitor.ui.view.DrawPMTrendThread;
import com.readearth.wuxiairmonitor.utils.AppUtil;
import com.readearth.wuxiairmonitor.utils.ChartUtil;
import com.readearth.wuxiairmonitor.utils.UrlFactory;

public class AqilineActivity extends Activity implements Constants,
		OnClickListener {
	private final int WHAT_GET_DATASTR = 1109;

	String id;
	String dataStr;

	int currentVId = R.id.txt_line_aqi;

	List<HourData> datalist;

	Map<Integer, RelativeLayout> vmap;

	TextView textView;
	ImageView back;

	int background_color = 0xFF2F383D;
	int array_color = 0xFFE3E3DD;

	final String TAG = "mytag AQIline";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aqiline);
		Intent intent = getIntent();
		id = intent.getExtras().getString(INTENT_SCAN_ID);
		textView = (TextView) findViewById(R.id.txt2_title);
		back = (ImageView) findViewById(R.id.btnline_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AqilineActivity.this.finish();
			}
		});
		initListener();
		initHistoryData(id);
		initFrameLayout();
	}

	private void initListener() {
		findViewById(R.id.txt_line_pm25).setOnClickListener(this);
		findViewById(R.id.txt_line_pm10).setOnClickListener(this);
		findViewById(R.id.txt_line_aqi).setOnClickListener(this);
		findViewById(R.id.txt_line_so2).setOnClickListener(this);
		findViewById(R.id.txt_line_no2).setOnClickListener(this);
		findViewById(R.id.txt_line_co).setOnClickListener(this);
		findViewById(R.id.txt_line_o3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		int vid = v.getId();
		Log.i("mytag", "onClick");
		switch (vid) {
		case R.id.txt_line_aqi:
			setCurrentView(vid);
			initSingleLine(ValueType.aqi);
			break;
		case R.id.txt_line_pm25:
			setCurrentView(vid);
			initSingleLine(ValueType.pm25);
			break;
		case R.id.txt_line_pm10:
			setCurrentView(vid);
			initSingleLine(ValueType.pm10);
			break;
		case R.id.txt_line_so2:
			setCurrentView(vid);
			initSingleLine(ValueType.so2);
			break;
		case R.id.txt_line_no2:
			setCurrentView(vid);
			initSingleLine(ValueType.no2);
			break;
		case R.id.txt_line_co:
			setCurrentView(vid);
			initSingleLine(ValueType.co);
			break;
		case R.id.txt_line_o3:
			setCurrentView(vid);
			initSingleLine(ValueType.o3);
			break;

		}
	}

	private void initHistoryData(final String id) {

		new Thread() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				super.run();
				String url = UrlFactory.aqiLineUrl(id);
				byte[] databyte = AppUtil.postViaHttpConnection(null, url);
				if (databyte == null) {
					handler.sendEmptyMessage(444);
					return;
				}
				dataStr = new String(databyte);

				Gson gson = new Gson();
				try {
					AqiLineDataOb lineDataOb = gson.fromJson(dataStr,
							AqiLineDataOb.class);
					datalist = lineDataOb.getStationHourData();
					Log.i(TAG, "Station name : "
							+ datalist.get(0).getPositionName()
							+ ", data size : " + datalist.size());
					handler.sendEmptyMessage(WHAT_GET_DATASTR);
				} catch (Exception e) {
					Log.w(TAG,"data read error");
					handler.sendEmptyMessage(555);
				}
			}
		}.start();

	}

	int mFrameWidth, mFrameHeight;

	final int FRAME_HEIGHT = 400;

	@SuppressWarnings("deprecation")
	private void initFrameLayout() {
		chartVeiw = (RelativeLayout) findViewById(R.id.rela_chartview);
		progressBar = (ProgressBar) findViewById(R.id.progress_line);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mFrameWidth = wm.getDefaultDisplay().getWidth();
		LayoutParams p = new LayoutParams(5, 5);
//		mFrameHeight = wm.getDefaultDisplay().getHeight()
//				/2;
		mFrameHeight = (wm.getDefaultDisplay().getHeight()
				- AppUtil.dip2px(this, 200));
		// if (mFrameHeight > AppUtil.dip2px(this, FRAME_HEIGHT))
		// mFrameHeight = AppUtil.dip2px(this, FRAME_HEIGHT)
		// - AppUtil.dip2px(this, 50);
		// .getDimension(R.dimen.chart_orther_height));
		// mFrameHeight = AppUtil.px2dip(mActivity, mActivity.getResources()
		// .getDimension(R.dimen.chart_height));
		// mFrameHeight = (int) mActivity.getResources().getDimension(
		// R.dimen.chart_height) - 50;
	}

	private RelativeLayout chartVeiw;
	private ProgressBar progressBar;
	private int mChartStartX = 50;

	private void initSingleLine(ValueType type) {
		try {
			int chartWidth = mFrameWidth;
			int chartHeight = mFrameHeight;
			List<String> realTimeList = ChartUtil.getLineChartListToAll(
					datalist, type);
			chartVeiw.removeAllViewsInLayout();
			if (realTimeList != null) {
				DrawPMTrendThread pmRT = new DrawPMTrendThread(this,
						mChartStartX, chartWidth, realTimeList,
						chartHeight+20);

				View v = getLayoutInflater().inflate(R.layout.trendview, null);
				RelativeLayout rlhour = (RelativeLayout) v
						.findViewById(R.id.rlhour);
				rlhour.addView(pmRT);

				RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				relLayoutParams.topMargin =0;
				relLayoutParams.bottomMargin = 0;

				chartVeiw.addView(v, relLayoutParams);

				final HorizontalScrollView s = (HorizontalScrollView) v;

				v.post(new Runnable() {
					@Override
					public void run() {
						s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// isInitBarChar = true;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHAT_GET_DATASTR) {
				progressBar.setVisibility(View.GONE);
				if (datalist == null)
					return;
				textView.setText(datalist.get(0).getPositionName());
				setCurrentView(R.id.txt_line_aqi);
				chartVeiw.setVisibility(View.VISIBLE);
				initSingleLine(ValueType.aqi);
			} else if (msg.what == 444) {
				Toast.makeText(AqilineActivity.this,
						getString(R.string.network_error), Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 555) {
				Toast.makeText(AqilineActivity.this,
						getString(R.string.data_leak), Toast.LENGTH_LONG);
			}
		};
	};

	private void setCurrentView(int viewId) {
		findViewById(R.id.txt_line_aqi).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_co).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_no2).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_o3).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_pm10).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_so2).setBackgroundColor(background_color);
		findViewById(R.id.txt_line_pm25).setBackgroundColor(background_color);

		TextView txt = (TextView) findViewById(viewId);
		txt.setBackgroundResource(R.drawable.arrow2);
		currentVId = viewId;

	}
}
