package com.readearth.wuxiairmonitor.ui.fragments;

import com.readearth.wuxiairmonitor.utils.AppUtil;
import com.readearth.wuxiairmonitor.utils.EncryptorUtil;
import com.readearth.wuxiairmonitor.utils.UrlFactory;

import android.app.Fragment;
import android.os.Bundle;

public class FragmentBase extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}

	public void refreshView(RefreshMode mode) {
		if (mode.equals(RefreshMode.Realtime)) {
			new Thread() {
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					super.run();
					String urlStr = UrlFactory.parseStationUrl(120.242, 31.5031,
							"22", "-1", "201");
					byte[] backDate = AppUtil.postViaHttpConnection(null, urlStr);
					String enStr = new String(backDate);
					String returnStr = EncryptorUtil.DesDecrypt(enStr);
					if (returnStr != null){
						
					}
				}
			}.start();
		} else if (mode.equals(RefreshMode.History)) {

		}
	}

	public static enum RefreshMode {
		Realtime, History;
	}
}
