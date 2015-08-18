package com.readearth.wuxiairmonitor.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.readearth.wuxiairmonitor.R;

public class PagePointUtil extends LinearLayout {
	private ImageView[] img;
	private Context context;

	private int imgCount;

	public PagePointUtil(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		this.context = context;
	}

	public PagePointUtil(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
		this.context = context;
	}

	public void initImageList(int imgSum) {
		this.removeAllViews();
		PagePointUtil.this.setGravity(Gravity.CENTER);
		imgCount = imgSum;
		img = new ImageView[imgSum];
		for (int i = 0; i < imgSum; i++) {
			img[i] = new ImageView(context);
			LayoutParams params = new LayoutParams(30,30);
			img[i].setLayoutParams(params);
			img[i].setScaleType(ImageView.ScaleType.FIT_XY);
			if (0 == i) {
				img[i].setImageResource(R.drawable.dot_d);
			} else {
				img[i].setImageResource(R.drawable.dot);
			}
			img[i].setPadding(10, 10, 10, 10);
			PagePointUtil.this.addView(img[i]);
		}
	}

	public void setPageLocation(int arg) {
		for (int i = 0; i < imgCount; i++) {
			if (arg == i) {
				img[i].setImageResource(R.drawable.dot_d);
			} else {
				img[i].setImageResource(R.drawable.dot);
			}
		}
	}

}
