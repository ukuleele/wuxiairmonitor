package com.readearth.wuxiairmonitor.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.ExplainClassVo;
import com.readearth.wuxiairmonitor.utils.AQIGradeUtil;

public class ExplainActivity extends Activity {
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// getActionBar().setTitle(R.string.return_text);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setDisplayShowHomeEnabled(true);

		setContentView(R.layout.explain_layout);
		initActionBar();
		initViewLayout();
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
			finishThisActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishThisActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finishThisActivity() {
		ExplainActivity.this.finish();
	}

	private LinearLayout mClassLayout;
	private LinearLayout mReferenceLayout;

	private void initViewLayout() {

		mClassLayout = (LinearLayout) findViewById(R.id.explain_classView_id);
		mReferenceLayout = (LinearLayout) findViewById(R.id.explain_referenceClass_id);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String colon = getString(R.string.colon);

		for (int i = 0; i <= 6; i++) {
			ExplainClassVo vo = new ExplainClassVo();
			int textColor = 0;
			if (i > 0) {
				vo.setGradeClass(i);
				vo.setGradeText(AQIGradeUtil.getGradeText(i, this));
				vo.setHealthEffect(AQIGradeUtil.getEffectsByGrade(i, this));
				vo.setSuggestion(AQIGradeUtil.getSuggestionByGrade(i, this));
				vo.setAqiRange(AQIGradeUtil.getAqiRangeText(i, this));
				vo.setAqiValueRange(AQIGradeUtil.getValueRangeText(i, this));

				View v = inflater.inflate(R.layout.explain_class_item_layout,
						null);
				TextView classText = (TextView) v
						.findViewById(R.id.explain_item_class_id);
				TextView aqiText = (TextView) v
						.findViewById(R.id.explain_item_aqiIndex_id);
				TextView effectText = (TextView) v
						.findViewById(R.id.explain_item_healthEffect_id);
				TextView suggestionText = (TextView) v
						.findViewById(R.id.explain_item_suggestion_id);

				textColor = AQIGradeUtil.getStationColorByGrade(
						vo.getGradeClass(), this);
				classText.setText(vo.getGradeText());
				classText.setTextColor(textColor);
				aqiText.setText(getResources().getString(
						R.string.aqi_quality_index)
						+ " " + vo.getAqiRange());
				aqiText.setTextColor(textColor);
				effectText.setText(getResources().getString(
						R.string.health_effects)
						+ colon + vo.getHealthEffect());
				suggestionText.setText(getResources().getString(
						R.string.suggestion)
						+ colon + vo.getSuggestion());

				mClassLayout.addView(v);
			}

			View referenceView = inflater.inflate(
					R.layout.explain_class_reference_item_layout, null);
			TextView referenceGrade = (TextView) referenceView
					.findViewById(R.id.explain_class_reference_id);
			TextView referernceAqi = (TextView) referenceView
					.findViewById(R.id.explain_class_aqi_id);
			TextView referernceValue = (TextView) referenceView
					.findViewById(R.id.explain_class_value_id);

			TextView valueType = (TextView) referenceView
					.findViewById(R.id.explain_reference_value_type_id);

			if (i == 0) {
				referenceGrade.setText(R.string.quality_evaluation);
				referernceAqi.setText(R.string.air_index);
				referernceValue.setText(R.string.concentration);
				valueType.setText(R.string.average_value_type);
			} else {
				referenceGrade.setText(vo.getGradeText());
				referenceGrade.setTextColor(textColor);
				referernceAqi.setText(vo.getAqiRange());
				referernceValue.setText(vo.getAqiValueRange());

				if (i == 6) {
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					int bottomPix = (int) Math.ceil(getResources()
							.getDimension(R.dimen.dip_1));
					params.setMargins(0, 0, 0, bottomPix);
					referenceView.setLayoutParams(params);
				}
			}

			mReferenceLayout.addView(referenceView);

		}

	}
}
