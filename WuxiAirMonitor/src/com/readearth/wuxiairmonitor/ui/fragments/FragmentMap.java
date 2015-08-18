package com.readearth.wuxiairmonitor.ui.fragments;

import com.baidu.mapapi.map.MapView;
import com.readearth.wuxiairmonitor.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMap extends Fragment{
	MapView mapView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.fragment_map, container, false);
		
//		mapView = (MapView)v.findViewById(R.id.mapview);
		
		return v;
	}
}
