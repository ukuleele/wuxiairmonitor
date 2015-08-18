package com.readearth.wuxiairmonitor.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.readearth.wuxiairmonitor.R;

public class SolidLineView extends View {

	private Paint mPaint;

	public SolidLineView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SolidLineView);

		int solidLineColor = a.getColor(
				R.styleable.SolidLineView_solidLineColor, 0xeeffffff);
		float solidLineSize = a.getDimension(
				R.styleable.SolidLineView_solidLineSize, 2);

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(solidLineSize);
		mPaint.setColor(solidLineColor);

		a.recycle();
	}

	public void setLineColor(int newColor) {
		mPaint.setColor(newColor);
		invalidate();
	}

	protected void onDraw(Canvas canvas) {

		int width = this.getWidth();

		Path path = new Path();
		path.moveTo(0, 2);
		path.lineTo(width, 2);
		canvas.drawPath(path, mPaint);
	}

}
