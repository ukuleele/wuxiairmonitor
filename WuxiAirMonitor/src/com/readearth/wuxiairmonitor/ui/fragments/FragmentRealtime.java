package com.readearth.wuxiairmonitor.ui.fragments;

import java.io.ObjectInputStream.GetField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.datebase.DBManager;
import com.readearth.wuxiairmonitor.object.ForecastItem;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.object.StationListHolder;
import com.readearth.wuxiairmonitor.ui.AddStationActivity;
import com.readearth.wuxiairmonitor.ui.ModifyActivity;
import com.readearth.wuxiairmonitor.ui.ScanActivity;
import com.readearth.wuxiairmonitor.ui.WuxiActivity;
import com.readearth.wuxiairmonitor.ui.adapter.ViewPageAdapter;
import com.readearth.wuxiairmonitor.ui.view.HorizontalLayout;
import com.readearth.wuxiairmonitor.ui.view.PagePointUtil;
import com.readearth.wuxiairmonitor.utils.AQIGradeUtil;
import com.readearth.wuxiairmonitor.utils.AppUtil;
import com.readearth.wuxiairmonitor.utils.EncryptorUtil;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;
import com.readearth.wuxiairmonitor.utils.UrlFactory;

public class FragmentRealtime extends Fragment implements Constants {

	/**
	 * fragment主页面
	 */
	private View v;
	private ViewPager viewPager;
	/**
	 * viewPage中的view列表
	 */
	private List<View> viewList = null;
	/**
	 * 选择的站点的ID列表
	 */
	private List<String> stationIDList = new ArrayList<String>();

	/**
	 * 当前显示viewPage的stationId
	 * 
	 */
	private String currentPageId = null;

	/**
	 * 站点的源数据，json语句
	 */
	private String imformationStr = null;
	private boolean firstInit = true;
	private List<StationDetailVo> stationList = null;
	private List<StationDetailVo> selectedList = null;
	private Map<String, StationDetailVo> stationMap = null;
	private ForecastItem forecastItem = null;

	// private TextView title = null;
	private ImageView addStation = null;
	private ImageView update = null;
	private LayoutInflater inflater = null;

	private DBManager dbManager = null;

//	private HorizontalLayout forecastView;
	private PullToRefreshScrollView mRefreshScroll;
	private int pageNum = 0;
	private PagePointUtil pagePointUtil;

	private static final int MSG_REFRESH = 22;
	private static final int MSG_NETWORK_ERROR = 101;

	static FragmentRealtime instance;
	
	

	public static FragmentRealtime getInstance() {
		if (instance == null)
			instance = new FragmentRealtime();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		v = inflater.inflate(R.layout.fragment_realtime, container, false);
		// title = (TextView) v.findViewById(R.id.text_title);
		viewPager = (ViewPager) v.findViewById(R.id.viewpager);
		dbManager = new DBManager(getActivity());
		initTitle();
		initImformationMap();
		initForeCast();
		initViewPage(inflater);
//		initForecastView(inflater);

		return v;
	}

	private void initTitle() {
		addStation = (ImageView) v.findViewById(R.id.btn_add);
		update = (ImageView) v.findViewById(R.id.btn_update);

		addStation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FragmentRealtime.this.getActivity(),
						ModifyActivity.class);

				FragmentRealtime.this.startActivityForResult(intent,
						REQUEST_CODE_ORDER_STATION);
				;
			}
		});
	}

	private void initImformationMap() {
		Bundle bundle = getArguments();
		if (firstInit) {
			imformationStr = bundle.getString(KEY_BUNDLE_STATION);
			firstInit = false;
		}
		java.lang.reflect.Type type = new TypeToken<LinkedList<StationDetailVo>>() {
		}.getType();
		Gson gson = new Gson();
		stationList = gson.fromJson(imformationStr, type);
		stationMap = new HashMap<String, StationDetailVo>();
		for (StationDetailVo station : stationList) {
			if (station == null)
				continue;
			stationMap.put(station.getStationID(), station);

		}
	}

	private void initForeCast() {
		new Thread() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				super.run();
				try {
					byte[] returnData = AppUtil.postViaHttpConnection(null,
							UrlFactory.getForecatUrl());
					String returnStr = new String(returnData);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<ForecastItem>() {
					}.getType();
					forecastItem = gson.fromJson(returnStr, type);

				} catch (Exception e) {
				}
			}
		}.start();
	}

	private void initViewPage(LayoutInflater inflater) {

		viewList = new ArrayList<View>();
		selectedList = new ArrayList<StationDetailVo>();

		stationIDList = ProjectUtil.getIdListSharedPreference(getActivity());
		// try {
		// if (stationIDList == null || stationIDList.get(0) == null) {
		// stationIDList.add("201");
		//
		// }
		//
		// } catch (Exception e) {
		// stationIDList = new ArrayList<String>();
		// stationIDList.add("201");
		// }
		// 保证不管怎样都有无锡市排在第一
		if (stationIDList != null) {
			for (int i = 0; i < stationIDList.size(); i++) {
				if (stationIDList.get(i).equalsIgnoreCase("201")) {
					stationIDList.remove(i);
				}
			}
			stationIDList.add(0, "201");
		} else {
			stationIDList = new ArrayList<>();
			stationIDList.add("201");
		}

		for (String stationId : stationIDList) {
			StationDetailVo station = stationMap.get(stationId);
			selectedList.add(station);
			View view = getSingleViewByStation(station, inflater);
			viewList.add(view);
		}
		pagePointUtil = (PagePointUtil) v
				.findViewById(R.id.page_location_layout);
		pagePointUtil.initImageList(viewList.size());
		// title.setText(selectedList.get(0).getStationName());
		currentPageId = selectedList.get(0).getStationID();
		WuxiActivity activity = (WuxiActivity) getActivity();
		activity.setSelectedIdList(stationIDList);
		activity.setSelecterStationList(selectedList);
		activity.setAllStationDetail(stationList);
		viewPager.setAdapter(new ViewPageAdapter(viewList));
		viewPager.setCurrentItem(pageNum, true);
		viewPager.setAnimation(AnimationUtils.loadAnimation(activity,
				R.anim.veiw_display));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				pageNum = arg0;
				// title.setText(selectedList.get(arg0).getStationName());
				currentPageId = selectedList.get(arg0).getStationID();
				WuxiActivity activity = (WuxiActivity) getActivity();
				activity.setMainPageStaion(selectedList.get(arg0));
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

//	private void initForecastView(LayoutInflater inflater) {
//		forecastView = (HorizontalLayout) v.findViewById(R.id.forecast_view);
//		forecastView.setForecastItem(forecastItem);
//	}

	// 初始化并返回单一个viewPage
	private View getSingleViewByStation(StationDetailVo station,
			LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.pageview_realtime, null);
		final PullToRefreshScrollView refreshScroll = (PullToRefreshScrollView) view
				.findViewById(R.id.refresh_scroll);
		refreshScroll.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO 自动生成的方法存根
				refreshData();
				mRefreshScroll = refreshScroll;
			}

		});
		if(station.getStationID().equalsIgnoreCase("201")){
		HorizontalLayout forecastView = (HorizontalLayout)view.findViewById(R.id.forecast_view);
		forecastView.setForecastItem(forecastItem);
		forecastView.setVisibility(View.VISIBLE);}
		TextView txtName = (TextView) view.findViewById(R.id.txt1_sta_name);
		ImageView imgScan = (ImageView) view.findViewById(R.id.img1_sta_scan);
		TextView txtTime = (TextView) view.findViewById(R.id.txt1_time);
		TextView txtIndex = (TextView) view.findViewById(R.id.txt1_index);
		TextView txtLevel = (TextView) view.findViewById(R.id.txt1_pltlevel);
		TextView txtMianPlt = (TextView) view.findViewById(R.id.txt1_mainplt);
		ImageView imgBoy = (ImageView) view.findViewById(R.id.img1_boy);
		TextView txt_influence = (TextView) view
				.findViewById(R.id.txt1_influence);
		TextView txt_advice = (TextView) view.findViewById(R.id.txt1_advice);

		// 为了转换日期格式写了这么多。。。
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日 HH时");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			date = format3.parse(station.getLST_AQI());
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
		}
		try {
			date = format1.parse(station.getLST_AQI());
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
		}
		txtTime.setText(format2.format(date));

		if (station.getStationID().equalsIgnoreCase("201"))
			imgScan.setVisibility(View.VISIBLE);

		// 主要污染物等级
		int grade = station.getPrimaryPollutantGrade();
		station.setDisplayType(station.getPrimaryPollutantType());
		// txtTime.setText(station.getLST_AQI());
		txtIndex.setText(String.valueOf(station.getPrimaryPollutantAQI()));
		txtLevel.setText(station.getPrimaryPollutantQuality());
		txtName.setText(station.getStationName());
		String str1 = getString(R.string.main_polution) + "\n";
		String str2 = AppUtil.getValueWithUnity(station.getDisplayValue(),
				getActivity());
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(str1);
		int start = 0;
		int end = str1.length();
		builder.setSpan(
				new AbsoluteSizeSpan(AppUtil.dip2px(getActivity(), 12)), start,
				end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(new ForegroundColorSpan(0xbbffffff), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.append(str2);
		start = end;
		end += str2.length();
		builder.setSpan(
				new AbsoluteSizeSpan(AppUtil.dip2px(getActivity(), 14)), start,
				end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txtMianPlt.setText(builder);
		imgScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				StationListHolder sendList = new StationListHolder(stationList);
				Intent intent = new Intent(getActivity(), ScanActivity.class);
				intent.putExtra(INTENT_SCAN_ACTIVITY, sendList);
				getActivity().startActivityForResult(intent, REQUEST_CODE_SCAN);
			}
		});
		txtIndex.setTextColor(AQIGradeUtil.getStationColorByGrade(grade));
		txtLevel.setTextColor(AQIGradeUtil.getStationColorByGrade(grade));
		String[] adviceStr = (getActivity().getResources()
				.getStringArray(R.array.suggestion_array));
		String[] influenceStr = getActivity().getResources().getStringArray(
				R.array.health_array);
		try {
			txt_influence.setText(influenceStr[grade - 1]);
			txt_advice.setText(adviceStr[grade - 1]);
		} catch (Exception e) {
			Log.i("mytag",
					"**This Station has no data: Id= " + station.getStationID()
							+ "  Name= " + station.getStationName());
		}
		imgBoy.setImageDrawable(getActivity().getResources().getDrawable(
				AQIGradeUtil.getImageViewSrcByGrade(grade)));
		return view;
	}

	public String getCurrentPageStationId() {
		return currentPageId;
	}

	public void refreshData() {
		new Thread() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				super.run();
				String urlStr = UrlFactory.parseStationUrl(120.242, 31.5031,
						"22", "-1", "201");
				byte[] backDate = AppUtil.postViaHttpConnection(null, urlStr);
				if (backDate != null) {
					String enStr = new String(backDate);
					String returnStr = EncryptorUtil.DesDecrypt(enStr);
					imformationStr = returnStr;
					// mRefreshScroll = refreshScroll;
					handler.sendEmptyMessage(MSG_REFRESH);
				} else {
					handler.sendEmptyMessage(MSG_NETWORK_ERROR);
				}
			}
		}.start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_CODE_ORDER_STATION) {
			stationIDList = new ArrayList<String>();
			List<String> idList = data
					.getStringArrayListExtra(INTENT_SELETED_STATION);
			for (String id : idList) {
				stationIDList.add(id);
			}
			dbManager.setIsSelected(idList);
			initViewPage(inflater);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_REFRESH) {
				mRefreshScroll.onRefreshComplete();

				new Thread() {
					@Override
					public void run() {
						try {
							sleep(160);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						handler.sendEmptyMessage(444);

					};
				}.start();

			} else if (msg.what == 444) {
				viewPager.setAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.view_dismiss));
				initImformationMap();
				initForeCast();
				initViewPage(inflater);

//				initForecastView(inflater);

			} else if (msg.what == MSG_NETWORK_ERROR) {
				mRefreshScroll.onRefreshComplete();

			}
		};
	};


}
