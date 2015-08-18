package com.readearth.wuxiairmonitor.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.datebase.DBManager;
import com.readearth.wuxiairmonitor.object.StationBase;
import com.readearth.wuxiairmonitor.ui.adapter.MannagerAdapter;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;

public class ModifyActivity extends Activity implements Constants {

	private DragSortListView listView;
	private List<StationBase> staList;
	private MannagerAdapter adapter;
	private ActionBar actionBar;
	private LinearLayout addStation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mannger);

		listView = (DragSortListView) findViewById(R.id.drag_list);
		addStation = (LinearLayout) findViewById(R.id.linelayout_add_station);
		addStation.setOnClickListener(new OnClickListener() {
			//

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(ModifyActivity.this,
						AddStationActivity.class);

				startActivityForResult(intent, REQUEST_CODE_SELECT_STATION);
			}
		});
		initActionBar();
		initStationBase();
		initDragListView();
	}

	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setTitle(getString(R.string.save));
	}

	private void initStationBase() {
		// List<String> idlist = new ArrayList<>();
		// idlist.add("201");
		// idlist.add("1190");
		// idlist.add("1188");
		// idlist.add("1191");
		List<String> idlist = ProjectUtil.getIdListSharedPreference(this);
		DBManager manager = new DBManager(this);
		staList = manager.getStationBaseByIdList(idlist);
	}

	private void initDragListView() {
		listView.setDropListener(onDrop);
		listView.setRemoveListener(onRemove);
		if (staList.get(0).getId().equalsIgnoreCase("201"))
			staList.remove(0);
		adapter = new MannagerAdapter(this, staList);
		listView.setAdapter(adapter);
		adapter.setOnDeleteClick(new MannagerAdapter.OnDeleteClick() {

			@Override
			public void onReadyDelete(int position) {
				// TODO 自动生成的方法存根
				itemIndex = position;
				isReadyDelete = true;
			}

			@Override
			public void onDelete() {
				// TODO 自动生成的方法存根
				itemIndex = -1;
				isReadyDelete = false;

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_SELECT_STATION) {
			initStationBase();
			if (staList.get(0).getId().equalsIgnoreCase("201"))
				staList.remove(0);
			adapter.setItems(staList);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent();
			WuxiActivity.getInstance().setFragmentToNull();
			;
			ArrayList<String> idList = (ArrayList<String>) getIdListByAdapter();
			ProjectUtil.setIdListSharedPreference(ModifyActivity.this, idList);
			intent.putStringArrayListExtra(INTENT_SELETED_STATION, idList);
			setResult(RESULT_CODE_ORDER_STATION, intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private List<String> getIdListByAdapter() {
		staList = (List<StationBase>) adapter.getItemList();
		List<String> idList = new ArrayList<>();

		for (StationBase sta : staList) {
			idList.add(sta.getId());
		}
		return idList;
	}

	// 监听器在手机拖动停下的时候触发
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {// from to 分别表示 被拖动控件原位置 和目标位置
			if (from != to) {
				StationBase item = (StationBase) adapter.getItem(from);// 得到listview的适配器
				adapter.remove(from);// 在适配器中”原位置“的数据。
				adapter.insert(item, to);// 在目标位置中插入被拖动的控件。
			}
		}
	};
	// 删除监听器，点击左边差号就触发。删除item操作。
	private RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			adapter.remove(which);
		}
	};

	private boolean isReadyDelete = false;
	private int itemIndex = -1;
	private int[] textLocation = new int[2];
	private Rect mRect = new Rect();

	@Override
	public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
		if (isReadyDelete && itemIndex > -1
				&& paramMotionEvent.getAction() == 0) {
			View itemView = listView.getChildAt(itemIndex);
			View deleteText = itemView.findViewById(R.id.click_remove);
			View rightGroup = itemView.findViewById(R.id.linear_delete_group);
			rightGroup.getLocationInWindow(this.textLocation);
			if (rightGroup.isShown()) {
				mRect.left = rightGroup.getLeft() + deleteText.getLeft();
				mRect.top = this.textLocation[1];
				mRect.right = rightGroup.getLeft() + deleteText.getLeft()
						+ deleteText.getWidth();
				mRect.bottom = (this.textLocation[1] + deleteText.getHeight());

				int clickX = Math.round(paramMotionEvent.getX());
				int clickY = Math.round(paramMotionEvent.getY());
				int scrollY = Math.round(this.listView.getScrollY());

				if (!this.mRect.contains(clickX, (clickY + scrollY))) {
					itemView.findViewById(R.id.img_ready_remove).setVisibility(
							View.VISIBLE);
					deleteText.setVisibility(View.GONE);
					deleteText.setClickable(false);
					this.itemIndex = -1;
					this.isReadyDelete = false;
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(paramMotionEvent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;// 锁定返回键
		}
		return super.onKeyDown(keyCode, event);

	}
}
