package com.readearth.wuxiairmonitor.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.ui.WuxiActivity;
import com.readearth.wuxiairmonitor.ui.adapter.ViewPageAdapter;
import com.readearth.wuxiairmonitor.ui.view.PagePointUtil;
import com.readearth.wuxiairmonitor.utils.AppUtil;

public class FragmentConcentration extends Fragment {
	private ViewPager viewPager;
	private List<View> viewList;
	private LayoutInflater inflater;

	private List<StationDetailVo> stationList;
	private WuxiActivity mActivity;

	private TextView title;
	private PagePointUtil pagePointUtil;

	static FragmentConcentration instance;

	public static FragmentConcentration getInstance() {
		if (instance == null)
			instance = new FragmentConcentration();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_basic, container, false);
		viewPager = (ViewPager) v.findViewById(R.id.viewpager);
		mActivity = (WuxiActivity) getActivity();
		pagePointUtil = (PagePointUtil) v
				.findViewById(R.id.page_location_layout);
		this.inflater = inflater;
		stationList = mActivity.getSelecterStationList();
		title = (TextView) v.findViewById(R.id.text_title);
		initViewPage();
		return v;
	}

	private View getSingleView(StationDetailVo station) {
		View v = inflater.inflate(R.layout.pageview_concentration, null);
		TextView txtPm25 = (TextView) v.findViewById(R.id.txt3_pm25);
		TextView txtPm10 = (TextView) v.findViewById(R.id.txt3_pm10);
		TextView txtO3 = (TextView) v.findViewById(R.id.txt3_o3);
		TextView txtCO = (TextView) v.findViewById(R.id.txt3_co);
		TextView txtSO2 = (TextView) v.findViewById(R.id.txt3_so2);
		TextView txtNO2 = (TextView) v.findViewById(R.id.txt3_no2);
		TextView txtTime = (TextView) v.findViewById(R.id.txt3_time);

		txtPm25.setText(AppUtil.getValueWithUnity(station.getPm25Value(),
				mActivity));
		txtPm10.setText(AppUtil.getValueWithUnity(station.getPm10Value(),
				mActivity));
		txtO3.setText(AppUtil.getValueWithUnity(station.getO3Value(), mActivity));
		txtCO.setText(AppUtil.getValueWithUnity(station.getCoValue(), mActivity));
		txtSO2.setText(AppUtil.getValueWithUnity(station.getSo2Value(),
				mActivity));
		txtNO2.setText(AppUtil.getValueWithUnity(station.getNo2Value(),
				mActivity));
		txtTime.setText(station.getLastUpDataTimeWithOutSecond());
		return v;
	}

	private void initViewPage() {
		viewList = new ArrayList<View>();
		for (StationDetailVo station : stationList) {

			viewList.add(getSingleView(station));
		}
		pagePointUtil.initImageList(viewList.size());
		title.setText(stationList.get(0).getStationName());
		viewPager.setAdapter(new ViewPageAdapter(viewList));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				title.setText(stationList.get(arg0).getStationName());
				pagePointUtil.setPageLocation(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO 自动生成的方法存根

			}
		});
	}

}
