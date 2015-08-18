package com.readearth.wuxiairmonitor.ui.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.utils.AppUtil;

@SuppressLint("DrawAllocation")
public class DrawPMTrendThread extends View {
	private List<String> listData;

	private float startX;
	private float unit;

	private static float lines = 6.0f;
	private float radius = 10.0f;
	private float lineWidth = 3.0f;

	private float text_day_size = 25;
	private float text_value_Size = 22;

	private float marginX_Time = 15.0f;
	private float marginY_Time = 100.0f;
	private float marginX_Degree = 26.0f;

	private int lineColor = Color.BLUE;

	private int width;
	private int lasthieght;

	private float padding35 = 35.0f;
	private float padding8 = 8.0f;

	public DrawPMTrendThread(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawPMTrendThread(Context context, int startX, int width,
			List<String> listData, int lasthieght) {
		super(context);

		Resources r = context.getResources();

		lineColor = r.getColor(R.color.chart_line_color);

		text_value_Size = r.getDimension(R.dimen.anchor_label_size);
		text_day_size = r.getDimension(R.dimen.chart_timelabel_size);
		radius = r.getDimension(R.dimen.anchor_point_size);
		lineWidth = r.getDimension(R.dimen.chart_line_width);

		marginX_Time = r.getDimension(R.dimen.marginX_Time);
		marginY_Time = r.getDimension(R.dimen.marginY_Time);
		marginX_Degree = r.getDimension(R.dimen.marginX_Degree);

		padding35 = r.getDimension(R.dimen.padding35);
		padding8 = r.getDimension(R.dimen.padding8);

		this.lasthieght = lasthieght;
		this.listData = listData;
		this.startX = startX;

		this.unit = width / lines + width % lines;

	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = (int) (listData.size() * unit);
		setMeasuredDimension((int) (listData.size() * unit),
				(int) (lasthieght + lasthieght / 2));

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		String data1[] = null;
		String data[] = null;

		float x1 = 0;
		float y1 = 0;
		float x2 = 0;
		float y2 = 0;
		float maxval = 0;
		float minval = 0;
		float bl = 0;

		if (!listData.get(0).split("\\$")[2].equals("")) {
			maxval = Float.parseFloat(listData.get(0).split("\\$")[2]);
			minval = Float.parseFloat(listData.get(0).split("\\$")[2]);
		}
		// 算出最大值
		for (int i = 0; i < listData.size(); i++) {
			if (i == 0) {
				if (!listData.get(i).split("\\$")[2].equals("")) {
					if (maxval < Float
							.parseFloat(listData.get(i).split("\\$")[2])) {
						maxval = Float
								.parseFloat(listData.get(i).split("\\$")[2]);
					}
				}
			} else {

				if (!listData.get(i).split("\\$")[0].equals("")) {
					if (maxval < Float
							.parseFloat(listData.get(i).split("\\$")[0])) {
						maxval = Float
								.parseFloat(listData.get(i).split("\\$")[0]);
						// if (maxval <= 200)
						// maxval = 200;
					}
				}
			}
		}
		// 算出最小值
		for (int i = 0; i < listData.size(); i++) {
			if (i == 0) {
				if (!listData.get(i).split("\\$")[2].equals("")) {
					if (minval > Float
							.parseFloat(listData.get(i).split("\\$")[2])) {
						minval = Float
								.parseFloat(listData.get(i).split("\\$")[2]);
					}
				}
			} else {

				if (!listData.get(i).split("\\$")[0].equals("")) {
					if (minval > Float
							.parseFloat(listData.get(i).split("\\$")[0])) {
						minval = Float
								.parseFloat(listData.get(i).split("\\$")[0]);
					}
				}
			}
		}

		// maxval = maxval + 40;// maxval + 60
		final int Vdeviation =0;
				;//垂直方向向下偏移量

		bl = (lasthieght*0.618f)
				/ (maxval - minval);
//		bl = (lasthieght - AppUtil.dip2px(getContext(), 60))
//				/ (maxval - minval);

		for (int i = 0; i < listData.size(); i++) {
			if (i + 1 != listData.size()) {
				data1 = listData.get(i).split("\\$");
				data = listData.get(i + 1).split("\\$");
				if (i == 0) {
					if (!data[0].equals("")) {
						if (!data[1].equals("")) {
							if (!data1[2].equals("")) {
								if (!data1[3].equals("")) {
									x1 = startX;
									y1 = (lasthieght - 90)
											- bl
											* (Float.parseFloat(data1[2]) - minval)
											+Vdeviation;//垂直方向向下偏移量 
									x2 = x1 + unit;
									y2 = (lasthieght - 90)
											- bl
											* (Float.parseFloat(data[0]) - minval)
											+ Vdeviation;//垂直方向向下偏移量
									paint.setColor(lineColor);
									paint.setStrokeWidth(lineWidth);
									canvas.drawLine(x1, y1, x2, y2, paint);
									paint.setColor(Color.WHITE);
									canvas.drawCircle(x1, y1, radius, paint);
									paint.setColor(Color.WHITE);
									paint.setTextSize(text_value_Size);
									canvas.drawText(data1[2], startX
											- marginX_Time, y1 - marginY_Time,
											paint);
									canvas.drawText(data[0], x2 - marginX_Time,
											y2 - marginY_Time, paint);
									canvas.drawText(data1[3], startX
											- marginX_Degree + 10, lasthieght
											+ padding35, paint);
									canvas.drawText(data[1], x2
											- marginX_Degree + 10, lasthieght
											+ padding35, paint);

									String firstDt = listData.get(
											listData.size() - 1).split("\\$")[2];
									// if (dd<Float.parseFloat(data1[2])) {
									paint1.setColor(Color.WHITE);
									paint1.setTextSize(text_day_size);
									canvas.drawText(firstDt, startX - 20,
											lasthieght, paint1);
									// }else {
									// canvas.drawText(getTimeShort(d)
									// , startX - 20,
									// startY - bl, paint);
									// }

								}
							}
						}
					}
				} else {
					if (!data[0].equals("")) {
						if (!data[1].equals("")) {
							if (!data1[0].equals("")) {
								if (!data1[1].equals("")) {
									x1 = x2;
									y1 = y2;
									x2 = x1 + unit;
									y2 = (lasthieght - 90)
											- bl
											* (Float.parseFloat(data[0]) - minval)
											+ Vdeviation;

									paint.setColor(lineColor);
									paint.setStrokeWidth(lineWidth);
									canvas.drawLine(x1, y1, x2, y2, paint);
									paint.setColor(Color.WHITE);
									canvas.drawCircle(x1, y1, radius, paint);
									if (i + 1 == listData.size() - 1) {
										canvas.drawCircle(x2, y2, radius, paint);

										String lastDt = listData.get(
												listData.size() - 1).split(
												"\\$")[3];
										canvas.drawText(lastDt, x2
												- marginX_Degree, lasthieght,
												paint1);

										canvas.drawLine(0, lasthieght
												+ padding8, width, lasthieght
												+ padding8, paint);
										paint.setColor(Color.WHITE);
									}
									canvas.drawText(data[0], x2 - marginX_Time,
											y2 - marginY_Time, paint);
									canvas.drawText(data[1], x2
											- marginX_Degree + 10, lasthieght
											+ padding35, paint);
								}
							}
						}
					}
				}
			}
		}
	}

}