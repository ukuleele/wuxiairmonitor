package com.readearth.wuxiairmonitor.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.datebase.DBManager;
import com.readearth.wuxiairmonitor.object.StationBase;
import com.readearth.wuxiairmonitor.ui.adapter.AddStationAdapter;
import com.readearth.wuxiairmonitor.ui.adapter.AddStationAdapter.OnItemClick;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;

public class AddStationActivity extends Activity implements Constants {

	private ActionBar actionBar;
	private ListView listView;
	private List<StationBase> stationList = null;
	private Map<String, Boolean> selectedMap = new HashMap<String, Boolean>();
	private AddStationAdapter adapter;

	// private List<String> idsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_choice);
		initActionBar();
		initStationBase();
		initListView();
		// idsList = ProjectUtil.getIdListSharedPreference(this);

	}

	private void initStationBase() {
		DBManager db = new DBManager(this);
		stationList = db.getStationBaseList();

	}

	private void initListView() {
		List<String> idsList = ProjectUtil.getIdListSharedPreference(this);
		listView = (ListView) findViewById(R.id.list_station_choice);
		adapter = new AddStationAdapter(this, stationList, idsList);
		listView.setAdapter(adapter);
		adapter.setOnItemClick(new OnItemClick() {

			@Override
			public void onItemClick(int position) {
				// TODO 自动生成的方法存根

			}
		});
	}

	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setTitle(getString(R.string.save));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ArrayList<String> selectedList = new ArrayList<String>();
		if (item.getItemId() == android.R.id.home) {
			// for (StationBase sta : stationList) {
			// if (sta.getIsSelectedBool())
			// selectedList.add(sta.getId());
			// }
			Intent intent = new Intent();
			// intent.putStringArrayListExtra(INTENT_SELETED_STATION,
			// selectedList);
			ProjectUtil.setIdListSharedPreference(this, adapter.getIdsList());
			setResult(RESULT_CODE_SELECT_STATION, intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;//锁定返回键
		}
		return super.onKeyDown(keyCode, event);

	}

}
