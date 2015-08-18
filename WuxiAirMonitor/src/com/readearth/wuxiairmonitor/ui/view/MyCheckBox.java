package com.readearth.wuxiairmonitor.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class MyCheckBox extends ImageView {

	private boolean isCheck;

	public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO 自动生成的构造函数存根
	}

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	public void setIsChecked(boolean isCheck){
		this.isCheck = isCheck;
		if(isCheck){
//			this.setImageDrawable(R.);
		}
	}
}
