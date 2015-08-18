package com.readearth.wuxiairmonitor.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.ui.ExplainActivity;
import com.readearth.wuxiairmonitor.ui.WuxiActivity;

public class FragmentOptions extends Fragment {
	View v;
	
	static FragmentOptions instance;

	public static FragmentOptions getInstance() {
		if (instance == null)
			instance = new FragmentOptions();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (WuxiActivity) getActivity();
		v = inflater.inflate(R.layout.fragment_options, container, false);
		initViews();
		return v;

	}

	private LinearLayout l_standard;
	private WuxiActivity activity;

	private void initViews() {
		l_standard = (LinearLayout) v.findViewById(R.id.linear_standard);
		l_standard.setOnClickListener(onclick);
	}

	View.OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			switch (v.getId()) {
			case R.id.linear_standard:
				Intent intent = new Intent();
				intent.setClass(activity, ExplainActivity.class);
				FragmentOptions.this.startActivity(intent);
			}

		}
	};
}
