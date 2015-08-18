package com.readearth.wuxiairmonitor.ui;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationDetailVo;
import com.readearth.wuxiairmonitor.object.StationListHolder;
import com.readearth.wuxiairmonitor.ui.adapter.ScanListApapter;

public class ScanActivity extends Activity implements Constants {

	ListView listView;
	StationListHolder listHolder;
	List<StationDetailVo> stationList;
	ImageView backimg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		listView = (ListView) findViewById(R.id.list_scan);
		backimg = (ImageView) findViewById(R.id.btn_back);
		initActionBar();
		backimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setResult(RESULT_CODE_SCAN, new Intent(ACTION_NOTHING));
				ScanActivity.this.finish();
			}
		});
		initData();
		initListView();

	}
	
	ActionBar actionBar;
	
	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setTitle(getString(R.string.return_text));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {
		Intent data = getIntent();
		StationListHolder listHodler = (StationListHolder) data
				.getSerializableExtra(INTENT_SCAN_ACTIVITY);
		stationList = listHodler.getStationList();
		stationList.remove(0);
	}

	private void initListView() {
		ScanListApapter adapter = new ScanListApapter(this, stationList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(ScanActivity.this,
						AqilineActivity.class);

				intent.putExtra(INTENT_SCAN_ID, stationList.get(arg2)
						.getStationID());
				startActivity(intent);
				// setResult(RESULT_CODE_SCAN, intent);
//				ScanActivity.this.finish();
			}
		});
	}
}
