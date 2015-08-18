package com.readearth.wuxiairmonitor.ui;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.object.StationListHolder;
import com.readearth.wuxiairmonitor.utils.AQIGradeUtil;
import com.readearth.wuxiairmonitor.utils.AppUtil;

public class MapActivity extends Activity implements Constants {

	MapView mMapView;
	BaiduMap mBaiduMap;

	List<StationDetailVo> stationList = null;

	ActionBar actionBar;
	LinearLayout button1, button2, button3, button5;
	View.OnClickListener bottomListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		initButton();
		initData();
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 31.4867 120.269 雪浪

		LatLng point = new LatLng(31.4867, 120.269);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.animateMapStatus(u);

		drawPoint();
	}

	private void initData() {
		Intent data = getIntent();
		StationListHolder listHoler = (StationListHolder) data
				.getSerializableExtra(INTENT_MAP_ACTIVITY);
		stationList = listHoler.getStationList();

	}

	private void drawPoint() {
		for (StationDetailVo station : stationList) {
			if (station.getStationID().equalsIgnoreCase("201")
					|| station.getPrimaryPollutantGrade() == 0)
				continue;
			int grade = station.getPrimaryPollutantGrade();

			LatLng point = new LatLng(station.getLatitude(),
					station.getLongitude());
			OverlayOptions ooText = new TextOptions()
					.bgColor(AQIGradeUtil.getStationColorByGrade2(grade))
					.fontSize(AppUtil.dip2px(MapActivity.this, 20))
					.fontColor(0xFFFFFFFF)
					.text(String.valueOf(station.getStationName() + " "
							+ station.getPrimaryPollutantAQI()))
					.position(point);
			mBaiduMap.addOverlay(ooText);
		}
	}

	private void initButton() {
		button1 = (LinearLayout) findViewById(R.id.btn4_frag1);
		button2 = (LinearLayout) findViewById(R.id.btn4_frag2);
		button3 = (LinearLayout) findViewById(R.id.btn4_frag3);
		button5 = (LinearLayout) findViewById(R.id.btn4_frag5);

		bottomListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MapActivity.this, WuxiActivity.class);
				switch (v.getId()) {
				case R.id.btn4_frag1:
					intent.setAction(ACTION_REALTIME);
					break;
				case R.id.btn4_frag2:
					intent.setAction(ACTION_HISTORY);
					break;
				case R.id.btn4_frag3:
					intent.setAction(ACTION_CONCENTRATION);
					break;
				case R.id.btn4_frag5:
					intent.setAction(ACTION_OPTION);
					break;
				}
				setResult(RESULT_CODE_MAP, intent);
				MapActivity.this.finish();
			}
		};

		button1.setOnClickListener(bottomListener);
		button2.setOnClickListener(bottomListener);
		button3.setOnClickListener(bottomListener);
		button5.setOnClickListener(bottomListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

}
