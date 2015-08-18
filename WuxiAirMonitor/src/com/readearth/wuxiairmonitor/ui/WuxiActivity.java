package com.readearth.wuxiairmonitor.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.object.StationListHolder;
import com.readearth.wuxiairmonitor.object.TrendDataVo;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentConcentration;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentHistory;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentHistory.OnFinishiInitAllView;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentMap;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentOptions;
import com.readearth.wuxiairmonitor.ui.fragments.FragmentRealtime;

public class WuxiActivity extends Activity implements Constants {

	private FragmentRealtime fragment1;
	private FragmentHistory fragment2;
	private FragmentConcentration fragment3;
	private FragmentMap fragment4;
	private FragmentOptions fragment5;

	private LinearLayout btn_bottom1;
	private LinearLayout btn_bottom2;
	private LinearLayout btn_bottom3;
	private LinearLayout btn_bottom4;
	private LinearLayout btn_bottom5;

	private String imformationStr = null;
	private List<StationDetailVo> selectedList = null;
	private List<StationDetailVo> allStaList = null;
	private Map<String, StationDetailVo> stationMap = null;
	private Bundle frgBundle = null;

	private StationDetailVo mainPageStation = null;
	private List<StationDetailVo> mStationList = null;

	private List<TrendDataVo> mTrendList = null;
	private List<String> mSelectedIdList = null;

	final FragmentManager fm = getFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		setContentView(R.layout.activity_main);
		initBottomMenu();
		if (savedInstanceState == null)
			setDefaultFragment();
		else {
			try {
				FragmentTransaction transaction = fm.beginTransaction();
				fragment1 = (FragmentRealtime) getFragmentManager()
						.findFragmentByTag(fragment1.getClass().getName());
				fragment2 = (FragmentHistory) getFragmentManager()
						.findFragmentByTag(fragment2.getClass().getName());
				fragment3 = (FragmentConcentration) getFragmentManager()
						.findFragmentByTag(fragment3.getClass().getName());
				fragment5 = (FragmentOptions) getFragmentManager()
						.findFragmentByTag(fragment5.getClass().getName());
				transaction.show(fragment1);
				transaction.hide(fragment2);
				transaction.hide(fragment3);
				transaction.hide(fragment5);
			} catch (Exception e) {
				Log.w("mytag", e.toString());
				setDefaultFragment();
			}
		}

	}

	static WuxiActivity instance;

	public static WuxiActivity getInstance() {
		return instance;
	}

	private void initData() {
		Intent intent = getIntent();
		imformationStr = intent.getStringExtra(INTENT_START);
		java.lang.reflect.Type type = new TypeToken<LinkedList<StationDetailVo>>() {
		}.getType();
		Gson gson = new Gson();
		selectedList = gson.fromJson(imformationStr, type);
		stationMap = new HashMap<String, StationDetailVo>();
		try {
			for (StationDetailVo station : selectedList) {
				if (station == null)
					continue;
				stationMap.put(station.getStationID(), station);

			}
			frgBundle = new Bundle();
			frgBundle.putString(KEY_BUNDLE_STATION, imformationStr);
			fragment1.setArguments(frgBundle);
		} catch (Exception e) {
			this.finish();
		}
	}

	private void initBottomMenu() {
		btn_bottom1 = (LinearLayout) findViewById(R.id.btn_bottom_1);
		btn_bottom2 = (LinearLayout) findViewById(R.id.btn_bottom_2);
		btn_bottom3 = (LinearLayout) findViewById(R.id.btn_bottom_3);
		btn_bottom4 = (LinearLayout) findViewById(R.id.btn_bottom_4);
		btn_bottom5 = (LinearLayout) findViewById(R.id.btn_bottom_5);
		btn_bottom1.setOnClickListener(bottomListener);
		btn_bottom2.setOnClickListener(bottomListener);
		btn_bottom3.setOnClickListener(bottomListener);
		btn_bottom4.setOnClickListener(bottomListener);
		btn_bottom5.setOnClickListener(bottomListener);
	}

	/**
	 * 设置初始Fragment
	 */
	private void setDefaultFragment() {
		if (mContent != null) {
			return;
		}
		FragmentTransaction transaction = fm.beginTransaction();
		fragment1 = FragmentRealtime.getInstance();
		mContent = fragment1;
		((ImageView) WuxiActivity.this.findViewById(R.id.img1))
				.setImageResource(R.drawable.shishi_ico_d);
		((TextView) WuxiActivity.this.findViewById(R.id.txt1))
				.setTextColor(0xff00ff00);
		initData();
		// 暂时初始fragment3
		// fragment3 = FragmentConcentration.getInstance();
		// transaction.replace(R.id.fragment_main, fragment3);
		transaction.replace(R.id.fragment_main, fragment1);
		transaction.commit();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("mytag", "onActivityResult in main Activity");

		if (requestCode == REQUEST_CODE_MAP) {
			String action = data.getAction();
			skipToFragment(action);
		}
		if (resultCode == RESULT_CODE_SCAN) {
			if (data.getAction().equalsIgnoreCase(ACTION_HISTORY)) {
				final int page = data.getExtras().getInt(INTENT_PAGE_NUM);
				skipToFragment(data.getAction());
				fragment2.setOnFinishiInitAllView(new OnFinishiInitAllView() {

					@Override
					public void doOnFinishInit() {
						// TODO 自动生成的方法存根
						fragment2.skipToPage(page);
					}
				});
				fragment2.skipToPage(page);
			}
		}

	};

	private void skipToFragment(String action) {
		FragmentTransaction transaction = fm.beginTransaction();
		origenal();
		switch (action) {
		case ACTION_REALTIME:
			if (fragment1 == null)
				fragment1 = FragmentRealtime.getInstance();
			// transaction.replace(R.id.fragment_main, fragment1);
			switchContent(mContent, fragment1);
			Log.i("mytag", "------1------");
			((ImageView) WuxiActivity.this.findViewById(R.id.img1))
					.setImageResource(R.drawable.shishi_ico_d);
			((TextView) WuxiActivity.this.findViewById(R.id.txt1))
					.setTextColor(0xff00ff00);
			break;

		case ACTION_HISTORY:
			if (fragment2 == null)
				fragment2 = FragmentHistory.getInstance();
			System.out.println("FragmentHistory init");
			// transaction.replace(R.id.fragment_main, fragment2);
			switchContent(mContent, fragment2);
			((ImageView) WuxiActivity.this.findViewById(R.id.img2))
					.setImageResource(R.drawable.lishi_ico_d);
			((TextView) WuxiActivity.this.findViewById(R.id.txt2))
					.setTextColor(0xff00ff00);
			Log.i("mytag", "------2------");
			break;

		case ACTION_CONCENTRATION:
			onCreate(frgBundle);
			if (fragment3 == null)
				fragment3 = FragmentConcentration.getInstance();
			// transaction.replace(R.id.fragment_main, fragment3);
			switchContent(mContent, fragment3);
			((ImageView) WuxiActivity.this.findViewById(R.id.img3))
					.setImageResource(R.drawable.nongdu_ico_d);
			((TextView) WuxiActivity.this.findViewById(R.id.txt3))
					.setTextColor(0xff00ff00);
			break;

		case ACTION_OPTION:
			if (fragment5 == null)
				fragment5 = FragmentOptions.getInstance();
			// transaction.replace(R.id.fragment_main, fragment5);
			switchContent(mContent, fragment5);
			((ImageView) WuxiActivity.this.findViewById(R.id.img5))
					.setImageResource(R.drawable.settings_ico_d);
			((TextView) WuxiActivity.this.findViewById(R.id.txt5))
					.setTextColor(0xff00ff00);
			break;

		default:
			break;
		}
		transaction.addToBackStack(null);
		transaction.commit();
	}

	View.OnClickListener bottomListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			FragmentTransaction transaction = fm.beginTransaction();
			Intent mapIntent = new Intent();
			StationListHolder stationDList = new StationListHolder(mStationList);
			origenal();
			switch (v.getId()) {
			case R.id.btn_bottom_1:
				if (fragment1 == null)
					fragment1 = FragmentRealtime.getInstance();
				((ImageView) WuxiActivity.this.findViewById(R.id.img1))
						.setImageResource(R.drawable.shishi_ico_d);
				((TextView) WuxiActivity.this.findViewById(R.id.txt1))
						.setTextColor(0xff00ff00);
				// transaction.replace(R.id.fragment_main, fragment1);

				switchContent(mContent, fragment1);
				Log.i("mytag", "------1------");
				break;
			case R.id.btn_bottom_2:
				if (fragment2 == null)
					fragment2 = FragmentHistory.getInstance();

				((ImageView) WuxiActivity.this.findViewById(R.id.img2))
						.setImageResource(R.drawable.lishi_ico_d);
				((TextView) WuxiActivity.this.findViewById(R.id.txt2))
						.setTextColor(0xff00ff00);

				// transaction.replace(R.id.fragment_main, fragment2);
				switchContent(mContent, fragment2);

				Log.i("mytag", "------2------");
				break;
			case R.id.btn_bottom_3:
				if (fragment3 == null)
					fragment3 = FragmentConcentration.getInstance();

				((ImageView) WuxiActivity.this.findViewById(R.id.img3))
						.setImageResource(R.drawable.nongdu_ico_d);
				((TextView) WuxiActivity.this.findViewById(R.id.txt3))
						.setTextColor(0xff00ff00);
				// transaction.replace(R.id.fragment_main, fragment3);

				switchContent(mContent, fragment3);
				break;
			case R.id.btn_bottom_4:
				// if (fragment4 == null)
				// fragment4 = new FragmentMap();
				// transaction.replace(R.id.fragment_main, fragment4);
				// 地图功能用另一个Activity
				mapIntent.putExtra(INTENT_MAP_ACTIVITY, new StationListHolder(
						allStaList));
				mapIntent.setClass(WuxiActivity.this, MapActivity.class);
				startActivityForResult(mapIntent, REQUEST_CODE_MAP);
				break;
			case R.id.btn_bottom_5:
				if (fragment5 == null)
					fragment5 = FragmentOptions.getInstance();

				((ImageView) WuxiActivity.this.findViewById(R.id.img5))
						.setImageResource(R.drawable.settings_ico_d);
				((TextView) WuxiActivity.this.findViewById(R.id.txt5))
						.setTextColor(0xff00ff00);
				// transaction.replace(R.id.fragment_main, fragment5);
				switchContent(mContent, fragment5);
				break;
			}
			transaction.addToBackStack(null);
			transaction.commit();
		}
	};
	private Fragment mContent;

	public void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = fm.beginTransaction();
			if (to.isAdded()) {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			} else {
				transaction
						.hide(from)
						.add(R.id.fragment_main, to,
								mContent.getClass().getName()).commit(); // 隐藏当前的fragment，add下一个到Activity中
			}
			// if (to.isDetached()) { // 先判断是否被add过
			// transaction.detach(from).attach(to).commit(); //
			// 隐藏当前的fragment，显示下一个
			// } else {
			// transaction.detach(from).add(R.id.fragment_main, to).commit(); //
			// 隐藏当前的fragment，add下一个到Activity中
			// }
		}
	}

	private void origenal() {
		((ImageView) WuxiActivity.this.findViewById(R.id.img1))
				.setImageResource(R.drawable.shishi_ico);
		((TextView) WuxiActivity.this.findViewById(R.id.txt1))
				.setTextColor(0xffa5a9ac);
		((ImageView) WuxiActivity.this.findViewById(R.id.img2))
				.setImageResource(R.drawable.lishi_ico);
		((TextView) WuxiActivity.this.findViewById(R.id.txt2))
				.setTextColor(0xffa5a9ac);
		((ImageView) WuxiActivity.this.findViewById(R.id.img3))
				.setImageResource(R.drawable.nongdu_ico);
		((TextView) WuxiActivity.this.findViewById(R.id.txt3))
				.setTextColor(0xffa5a9ac);
		((ImageView) WuxiActivity.this.findViewById(R.id.img5))
				.setImageResource(R.drawable.settings_ico);
		((TextView) WuxiActivity.this.findViewById(R.id.txt5))
				.setTextColor(0xffa5a9ac);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 自动生成的方法存根
		super.onWindowFocusChanged(hasFocus);
		try {
			if (winFocusChange != null)
				winFocusChange.winFrocusChange();
		} catch (Exception e) {

		}
	}

	public void setFragmentToNull() {
		// fragment1 = null;
		fragment2 = null;
		fragment3 = null;
	}

	private OnWinFocusChange winFocusChange;

	public void setOnWinFocusChange(OnWinFocusChange focusChange) {
		this.winFocusChange = focusChange;
	}

	public interface OnWinFocusChange {
		void winFrocusChange();
	}

	public void setMainPageStaion(StationDetailVo station) {
		this.mainPageStation = station;
	}

	public StationDetailVo getMainPageStation() {
		return mainPageStation;
	}

	public List<String> getSelectedIdList() {
		return mSelectedIdList;
	}

	public void setSelectedIdList(List<String> mSelectedIdList) {
		this.mSelectedIdList = mSelectedIdList;
	}

	public List<StationDetailVo> getSelecterStationList() {
		return mStationList;
	}

	public void setSelecterStationList(List<StationDetailVo> mStationList) {
		this.mStationList = mStationList;
	}

	public void setAllStationDetail(List<StationDetailVo> stationlist) {
		this.allStaList = stationlist;
	}

	public List<StationDetailVo> getAllStationDetail() {
		return allStaList;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;// 锁定返回键
		}
		return super.onKeyDown(keyCode, event);

	}
}
