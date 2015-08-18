package com.readearth.wuxiairmonitor.ui;

import java.io.Flushable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.readearth.wuxiairmonitor.Constants;
import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.datebase.TranscribeDatabase;
import com.readearth.wuxiairmonitor.utils.AppUtil;
import com.readearth.wuxiairmonitor.utils.EncryptorUtil;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;
import com.readearth.wuxiairmonitor.utils.UrlFactory;

public class StartActivity extends Activity implements Constants {

	private String returnStr;
	private static final String TAG = "mytag_start";
	public static final int NET_WORK_ERROR = 444;
	public static final int NET_WORK_OK = 666;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		lordDatabase();
		// requestStations();

	}

	public void lordDatabase() {
		if (!TranscribeDatabase.checkDataBase()) {
			Log.i("mytag", "准备导入源数据库");
			TranscribeDatabase transcribeDatabase = new TranscribeDatabase(this);
			transcribeDatabase
					.setOnTransCompleted(new TranscribeDatabase.OnTransCompleted() {
						@Override
						public void onCompleted() {
							// TODO 自动生成的方法存根
							requestStations();
						}
					});
			transcribeDatabase.startTransWork();

		} else {
			requestStations();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	public void requestStations() {
		new Thread() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				super.run();
				try {
					String urlStr = UrlFactory.parseStationUrl(120.242,
							31.5031, "22", "-1", "201");
					byte[] backDate = AppUtil.postViaHttpConnection(null,
							urlStr);
					String enStr = new String(backDate);
					returnStr = EncryptorUtil.DesDecrypt(enStr);
					if (returnStr != null)
						handler.sendEmptyMessage(NET_WORK_OK);
				} catch (NullPointerException e) {
					handler.sendEmptyMessage(NET_WORK_ERROR);
				}
			}
		}.start();
	}
	
	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		super.onRestart();
		onCreate(null);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NET_WORK_OK) {
				ProjectUtil.setSharedPreference(StartActivity.this,
						SP_SHTATION_KEY, returnStr);
				Intent intent = new Intent(StartActivity.this,
						WuxiActivity.class);
				intent.putExtra(INTENT_START, returnStr);
				finish();
				startActivity(intent);
			} else if (msg.what == NET_WORK_ERROR) {
				String dataStr = ProjectUtil.getSharedPreference(
						StartActivity.this, SP_SHTATION_KEY);
				if (dataStr == null) {
					new AlertDialog.Builder(StartActivity.this)
							.setTitle(getString(R.string.tips))
							.setMessage(getString(R.string.please_open_network))
							.setPositiveButton(getString(R.string.try_again),
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO 自动生成的方法存根
											requestStations();
										}
									}).show();

				} else {
					Intent intent = new Intent(StartActivity.this,
							WuxiActivity.class);
					intent.putExtra(INTENT_START, dataStr);
					startActivity(intent);
					finish();
					Toast.makeText(StartActivity.this,
							getString(R.string.network_error),
							Toast.LENGTH_LONG).show();
				}
			}
		};
	};

}
