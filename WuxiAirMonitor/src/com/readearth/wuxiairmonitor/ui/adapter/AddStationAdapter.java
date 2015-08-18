package com.readearth.wuxiairmonitor.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationBase;

public class AddStationAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	Context context;
	// 传入的站点ID列表
	List<StationBase> staList = null;
	List<String> idsList = null;

	public AddStationAdapter(Context context, List<StationBase> stationList,
			List<String> idsList) {
		super();
		this.context = context;
		this.staList = stationList;
		this.idsList = idsList;
	}

	@Override
	public int getCount() {
		return staList.size();
	}

	@Override
	public Object getItem(int position) {
		return staList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder h = null;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final StationBase item = staList.get(position);
//		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_station_list, null);
			h = new ViewHolder();
			h.check = (CheckBox) convertView.findViewById(R.id.check_select);
			h.textView = (TextView) convertView
					.findViewById(R.id.txt1_station_check);
			convertView.setTag(h);
//		} else {
//			// 界面容器
//			h = (ViewHolder) convertView.getTag();
//		}
		if(item.getId().equalsIgnoreCase("201"))
			h.check.setVisibility(View.INVISIBLE);
		h.textView.setText(item.getName());
		h.check.setChecked(isItemSelected(item.getId()));
		h.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO 自动生成的方法存根
				if (isChecked) {
					idsList.add(staList.get(position).getId());
				} else {
					for (int i = 0; i < idsList.size(); i++) {
						if (idsList.get(i).equalsIgnoreCase(item.getId())) {
							idsList.remove(i);
							break;
						}

					}
				}
			}
		});
		return convertView;
	}

	private OnItemClick itemClick;

	public void setOnItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public interface OnItemClick {
		public void onItemClick(int position);
	}

	public static Drawable getImgCheckDrawable(boolean checked, Context context) {
		if (checked) {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_on_background);
			return drawable;
		} else {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_off_background);
			return drawable;
		}
	}

	public static Drawable getImgCheckDrawable(String checked, Context context) {
		if (checked.equalsIgnoreCase("1")) {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_on_background);
			return drawable;
		} else {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_off_background);
			return drawable;
		}
	}

	public static Drawable getImgCheckDrawable(int checked, Context context) {
		if (checked == 1) {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_on_background);
			return drawable;
		} else {
			Drawable drawable = context.getResources().getDrawable(
					android.R.drawable.checkbox_off_background);
			return drawable;
		}
	}

	private boolean isItemSelected(String id) {
		for (String str : idsList) {
			if (str.equalsIgnoreCase(id))
				return true;
		}
		return false;
	}

	public List<String> getIdsList() {
		return idsList;
	}

	class ViewHolder {
		TextView textView;
		CheckBox check;
	}
}
