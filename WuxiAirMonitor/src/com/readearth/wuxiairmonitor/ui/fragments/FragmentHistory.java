package com.readearth.wuxiairmonitor.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.object.TrendDataVo;
import com.readearth.wuxiairmonitor.ui.WuxiActivity;
import com.readearth.wuxiairmonitor.ui.adapter.ViewPageAdapter;
import com.readearth.wuxiairmonitor.ui.view.DrawPMTrendThread;
import com.readearth.wuxiairmonitor.ui.view.PagePointUtil;
import com.readearth.wuxiairmonitor.utils.AQIGradeUtil;
import com.readearth.wuxiairmonitor.utils.AppUtil;
import com.readearth.wuxiairmonitor.utils.ChartUtil;
import com.readearth.wuxiairmonitor.utils.EncryptorUtil;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;
import com.readearth.wuxiairmonitor.utils.UrlFactory;

public class FragmentHistory extends Fragment {
	public static final String TAG = "mytag_FragmentHistory";
	public static final String TREND = "trend";

	private String stationId = null;

	private List<TrendDataVo> mTrendList = null;
	private Gson gson = new Gson();
	private WuxiActivity mActivity = null;

	private StationDetailVo currentStation;
	private List<TrendDataVo> trendList = null;
	private List<String> selectedIds = null;

	private ViewPager viewPager;
	private List<View> viewList;
	private TextView txt_title;
	private LayoutInflater mInflater;

	private int mFrameWidth = 0;
	private int mFrameHeight = 0;
	private final int requestOutTime = 15000;

	private PagePointUtil pagePointUtil;

	static FragmentHistory instance;

	public static FragmentHistory getInstance() {
		if (instance == null)
			instance = new FragmentHistory();
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		System.out.print("onAttch from " + activity.toString());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("FragmentHistory create view");
		View v = inflater.inflate(R.layout.fragment_history, container, false);
		txt_title = (TextView) v.findViewById(R.id.txt2_title);
		mActivity = (WuxiActivity) getActivity();
		selectedIds = mActivity.getSelectedIdList();
		viewPager = (ViewPager) v.findViewById(R.id.viewpager2);
		pagePointUtil = (PagePointUtil) v
				.findViewById(R.id.page_location_layout);
		this.mInflater = inflater;
		instance = this;
		Log.i(TAG, "---History Fragment Created---");
		initFrameLayout();
		requestStations();

		return v;
	}

	public void requestStations() {
		if (trendList == null || trendList.size() != 0)
			trendList = new ArrayList<TrendDataVo>();
		request = new RequestThread();
		request.start();
		outTimeThread = new OutTimeThread();
		outTimeThread.start();
	}

	RequestThread request;
	OutTimeThread outTimeThread;

	class OutTimeThread extends Thread {
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			super.run();
			try {
				sleep(requestOutTime);
				request.interrupt();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				excuteReturn();
				e.printStackTrace();
			}

		}
	}

	class RequestThread extends Thread {
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			super.run();
			for (String selectedId : selectedIds) {
				String urlStr = UrlFactory.parseHistoryUrl(120.242, 31.5031,
						"22", selectedId);
				byte[] backDate = AppUtil.postViaHttpConnection(null, urlStr);
				String trendStr = null;
				try {
					if (backDate != null) {
						String enStr = new String(backDate);
						trendStr = EncryptorUtil.DesDecrypt(enStr);
						ProjectUtil.setSharedPreference(mActivity,
								Constants.SP_HISTORY_KEY, trendStr);
					} else {
						trendStr = ProjectUtil.getSharedPreference(mActivity,
								Constants.SP_HISTORY_KEY);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				// 解析句子
				TrendDataVo trendData = null;
				trendData = gson.fromJson(trendStr, TrendDataVo.class);

				trendList.add(trendData);

			}
			if (trendList.size() == selectedIds.size()) {
				Message msg = new Message();
				msg.what = 777;
				handler.sendMessage(msg);
			}
			// 结束初始化数据

		}
	}

	private void excuteReturn() {
		String trendStr = ProjectUtil.getSharedPreference(mActivity,
				Constants.SP_HISTORY_KEY);
		TrendDataVo trendData = null;
		trendData = gson.fromJson(trendStr, TrendDataVo.class);

		trendList.add(trendData);

		if (trendList.size() == selectedIds.size()) {
			Message msg = new Message();
			msg.what = 777;
			handler.sendMessage(msg);
		}
	}

	private void initViewPage(LayoutInflater inflater) {
		viewList = new ArrayList<View>();

		for (TrendDataVo trend : trendList) {
			try {
				View view = initSingleView(inflater, trend);
				viewList.add(view);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		final List<StationDetailVo> stationDetailList = mActivity
				.getSelecterStationList();
		txt_title.setText(stationDetailList.get(0).getStationName());
		pagePointUtil.initImageList(viewList.size());
		viewPager.setAdapter(new ViewPageAdapter(viewList));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				txt_title.setText(stationDetailList.get(arg0).getStationName());
				try {
					setSingleTrendList(arg0);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
				pagePointUtil.setPageLocation(arg0);
				// Toast.makeText(this,"加载地区:"+trendList.get(arg0), duration)
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
		// 设置第一个走势图
		try {
			setSingleTrendList(0);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			if (onFinishiInitAllView != null)
				onFinishiInitAllView.doOnFinishInit();
		}
	}

	class ViewHolder {
		RadioGroup group;
		RadioButton radio1;
		RadioButton radio2;
		TextView txt_aqitype;
		RelativeLayout relRealTime;
		RelativeLayout relMonth;
		FrameLayout frameLayout;
		ProgressBar progress;
		LayoutInflater inflater;
	}

	/**
	 * 除去绘图控件以外的高度dip
	 */
	final int OTHER_VIEW_HEIGHT = 350;

	@SuppressWarnings("deprecation")
	private void initFrameLayout() {
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		mFrameWidth = wm.getDefaultDisplay().getWidth();
		LayoutParams p = new LayoutParams(5, 5);
		mFrameHeight = wm.getDefaultDisplay().getHeight()
				- AppUtil.dip2px(mActivity, 350);
		if (mFrameHeight > AppUtil.dip2px(mActivity, FRAME_HEIGHT))
			mFrameHeight = AppUtil.dip2px(mActivity, FRAME_HEIGHT)
					- AppUtil.dip2px(mActivity, 50);
		// .getDimension(R.dimen.chart_orther_height));
		// mFrameHeight = AppUtil.px2dip(mActivity, mActivity.getResources()
		// .getDimension(R.dimen.chart_height));
		// mFrameHeight = (int) mActivity.getResources().getDimension(
		// R.dimen.chart_height) - 50;

	}

	final int FRAME_HEIGHT = 400;

	private View initSingleView(LayoutInflater inflater, TrendDataVo trend)
			throws Exception {
		View v = inflater.inflate(R.layout.pageview_history, null);
		final ViewHolder holder = new ViewHolder();
		holder.group = (RadioGroup) v.findViewById(R.id.group_aqi_type);
		holder.radio1 = (RadioButton) v.findViewById(R.id.radio2_realtime);
		holder.radio2 = (RadioButton) v.findViewById(R.id.radio2_30day);
		holder.txt_aqitype = (TextView) v.findViewById(R.id.txt2_aqi_type);
		holder.frameLayout = (FrameLayout) v.findViewById(R.id.frame2);
		if (mFrameHeight == AppUtil.dip2px(mActivity, FRAME_HEIGHT)
				- AppUtil.dip2px(mActivity, 50)) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					AppUtil.dip2px(mActivity, FRAME_HEIGHT));
			holder.frameLayout.setLayoutParams(params);
		}
		holder.relRealTime = (RelativeLayout) v
				.findViewById(R.id.view2_realtime);
		holder.relMonth = (RelativeLayout) v.findViewById(R.id.view2_month);
		holder.progress = (ProgressBar) v.findViewById(R.id.progress2);
		holder.inflater = inflater;

		// 初始曲线图
		// reLoadTrendList(trend, holder);
		// 默认设定
		holder.progress.setVisibility(View.INVISIBLE);
		holder.relRealTime.setVisibility(View.VISIBLE);
		holder.relMonth.setVisibility(View.INVISIBLE);

		holder.txt_aqitype.setText(getString(R.string.aqi_24hour_trend));

		holder.group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO 自动生成的方法存根
				int radioId = group.getCheckedRadioButtonId();
				switch (radioId) {

				case R.id.radio2_realtime:
					holder.relRealTime.setVisibility(View.VISIBLE);
					holder.relMonth.setVisibility(View.INVISIBLE);
					holder.txt_aqitype
							.setText(getString(R.string.aqi_24hour_trend));
					// Log.i("mytag","frame layout value = "+holder.frameLayout.getHeight());
					break;
				case R.id.radio2_30day:
					holder.relRealTime.setVisibility(View.INVISIBLE);
					holder.relMonth.setVisibility(View.VISIBLE);
					holder.txt_aqitype
							.setText(getString(R.string.aqi_month_trend));
					break;

				}
			}
		});

		return v;

	}

	private int mChartStartX = 50;;
	private boolean isInitBarChar = false;

	private void setSingleTrendList(int arg0) throws Exception {
		View v = viewList.get(arg0);
		ViewHolder h = new ViewHolder();
		h.relMonth = (RelativeLayout) v.findViewById(R.id.view2_month);
		h.relRealTime = (RelativeLayout) v.findViewById(R.id.view2_realtime);
		h.inflater = mInflater;
		reLoadTrendList(trendList.get(arg0), h);
	}

	/**
	 * 重新初始化曲线数据布局
	 * 
	 * @param trend
	 */
	private void reLoadTrendList(TrendDataVo trend, ViewHolder h) {
		// 加载滚动条消失
		// v.findViewById(R.id.progress2).setVisibility(
		// View.GONE);
		// .setVisibility(View.VISIBLE);
		//
		// mChartWidth = mChartLayout.getWidth();
		// mChartHeight = mChartLayout.getHeight()
		// - (int) getResources().getDimension(R.dimen.dip_35);
		//
		// if (trend == null)
		// return;

		// 初始化实时指数AQI曲线
		try {
			int chartWidth = mFrameWidth;
			int chartHeight = mFrameHeight;
			List<String> realTimeList = ChartUtil.getLineChartList(trend
					.getTableRT());
			h.relRealTime.removeAllViews();
			if (realTimeList != null) {
				DrawPMTrendThread pmRT = new DrawPMTrendThread(getActivity(),
						mChartStartX, chartWidth, realTimeList,
						chartHeight + 20);

				View v = h.inflater.inflate(R.layout.trendview, null);
				RelativeLayout rlhour = (RelativeLayout) v
						.findViewById(R.id.rlhour);
				rlhour.addView(pmRT);

				RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				relLayoutParams.topMargin = 0;
				relLayoutParams.bottomMargin = 0;

				h.relRealTime.addView(v, relLayoutParams);

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
		try {
			drawMonthTrend(ChartUtil.getBarChartList(trend.getTable30()), h);
		} catch (Exception e) {
			Log.e("mytag", e.getMessage());
		}
		// isInitBarChar = true;
	}

	private void drawMonthTrend(List<String> listData, ViewHolder h) {
		float maxValue = 0; // 最大的AQI值
		float y;
		float toy = 1.0f; // 高度缩放比例

		RelativeLayout rlday;
		LinearLayout layout_target, layout_time;
		ImageView level1, imageView1;
		TextView aqi1, time1;
		String[] data;

		ScaleAnimation myAnimation_Scale;
		TranslateAnimation am;
		TranslateAnimation ta;

		int barChartBottom = 0;
		try {
			barChartBottom = (int) getResources().getDimension(
					R.dimen.barchart_padding_bottom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		h.relMonth.removeAllViews();
		try {
			if (listData != null) {

				for (int i = 0; i < listData.size(); i++) {
					data = listData.get(i).split("\\$");
					if (maxValue < Float.parseFloat(data[1])) {
						maxValue = Float.parseFloat(data[1]);
					}
				}
				y = (float) (1.0 / maxValue);

				View trend = h.inflater.inflate(R.layout.trendbarview, null);

				rlday = (RelativeLayout) trend.findViewById(R.id.rlday);
				for (int i = 0; i < listData.size(); i++) {
					View view = h.inflater.inflate(R.layout.trendbar, null);
					layout_target = (LinearLayout) view
							.findViewById(R.id.target);

					level1 = (ImageView) layout_target.findViewById(R.id.level);
					imageView1 = (ImageView) layout_target
							.findViewById(R.id.imageView);
					aqi1 = (TextView) layout_target.findViewById(R.id.aqi);

					data = listData.get(i).split("\\$");
					toy = y * Float.parseFloat(data[1]);

					myAnimation_Scale = new ScaleAnimation(1.0f, 1.0f, 0.0f,
							toy, Animation.RELATIVE_TO_PARENT, 1.0f,
							Animation.RELATIVE_TO_PARENT, 1.0f);
					myAnimation_Scale.setDuration(990);
					myAnimation_Scale.setFillAfter(true);

					am = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
							0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_PARENT, 1.0f,
							Animation.RELATIVE_TO_PARENT, 1.0f - toy
									- ((1.0f - toy) * 0.005f));
					am.setDuration(980);
					am.setFillAfter(true);

					ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
							0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_PARENT, 1.0f,
							Animation.RELATIVE_TO_PARENT, 1.0f - toy
									- ((1.0f - toy) * 0.005f));
					ta.setDuration(970);
					ta.setFillAfter(true);

					AQIGradeUtil.SetImageView(level1, data[1], getActivity());
					imageView1.startAnimation(myAnimation_Scale);
					aqi1.setText(data[1]);
					aqi1.startAnimation(ta);
					level1.startAnimation(am);
					ta.startNow();
					am.startNow();
					myAnimation_Scale.startNow();

					RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					relLayoutParams.leftMargin = i * (mFrameWidth / 8);
					relLayoutParams.bottomMargin = barChartBottom;
					rlday.addView(view, relLayoutParams);
				}

				View viewline = h.inflater.inflate(R.layout.trendline, null);
				RelativeLayout.LayoutParams relline = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

				relline.width = (mFrameWidth / 8) * listData.size();
				relline.bottomMargin = barChartBottom;
				rlday.addView(viewline, relline);

				for (int i = 0; i < listData.size(); i++) {
					View view = h.inflater.inflate(R.layout.trendtime, null);
					layout_time = (LinearLayout) view
							.findViewById(R.id.layout_time);

					data = listData.get(i).split("\\$");

					time1 = (TextView) layout_time.findViewById(R.id.time);
					time1.setText(data[0]);

					RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					relLayoutParams.leftMargin = i * (mFrameWidth / 8);

					rlday.addView(view, relLayoutParams);
				}
				RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

				h.relMonth.addView(trend, relLayoutParams);

				final HorizontalScrollView s = (HorizontalScrollView) trend;
				trend.postDelayed(new Runnable() {
					@Override
					public void run() {
						s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				}, 0);

			}
		} catch (NumberFormatException e) {

		}
	}

	public void skipToPage(int page) {
		if (viewPager == null)
			return;
		viewPager.setCurrentItem(page);
		final List<StationDetailVo> stationDetailList = mActivity
				.getSelecterStationList();
		txt_title.setText(stationDetailList.get(page).getStationName());
		try {
			setSingleTrendList(page);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		pagePointUtil.setPageLocation(page);
	}

	/**
	 * @author ThinkPad User 完成整个界面创建需要一些时间，该接口用执行必须在界面完成后执行的方法
	 */
	public interface OnFinishiInitAllView {
		public void doOnFinishInit();
	}

	OnFinishiInitAllView onFinishiInitAllView;

	public void setOnFinishiInitAllView(
			OnFinishiInitAllView onFinishiInitAllView) {
		this.onFinishiInitAllView = onFinishiInitAllView;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			if (msg.what == 777) {
				Bundle data = msg.getData();
				String trendStr = data.getString(TREND);
				initViewPage(mInflater);
			}
		}

	};
}
