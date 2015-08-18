package com.readearth.wuxiairmonitor.ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.object.StationBase;
import com.readearth.wuxiairmonitor.ui.ModifyActivity;
import com.readearth.wuxiairmonitor.utils.ProjectUtil;

public class MannagerAdapter extends BaseAdapter {
	
	public static final String TAG = "mytag_adapter";

	private ModifyActivity context;
	List<StationBase> items;// 适配器的数据源
	WindowManager wm;
	int mWidth = 0;

	public MannagerAdapter(ModifyActivity context, List<StationBase> list) {
		this.context = context;
		this.items = list;
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mWidth = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public List<?> getItemList() {
		return items;
	}

	public void remove(int arg0) {// 删除指定位置的item
		items.remove(arg0);
		this.notifyDataSetChanged();// 不要忘记更改适配器对象的数据源
	}

	public void insert(StationBase item, int arg0) { // 在指定位置插入item
		items.add(arg0, item);
		this.notifyDataSetChanged();
	}

	public void setItems(List<StationBase> items) {
		this.items = items;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StationBase item = (StationBase) getItem(position);
		final ViewHolder h;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_drag_station, null);
			h.tvTitle = (TextView) convertView.findViewById(R.id.txt_drag_name);
			h.tvDelete = (TextView) convertView.findViewById(R.id.click_remove);
			h.ivDragHandle = (ImageView) convertView
					.findViewById(R.id.drag_handle);
			h.ivReadyDelete = (ImageView) convertView
					.findViewById(R.id.img_ready_remove);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		h.tvTitle.setText(item.getName());
		
		final int index = position;
		h.ivReadyDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				h.ivReadyDelete.setVisibility(View.GONE);
				h.tvDelete.setVisibility(View.VISIBLE);
				h.tvDelete.setClickable(true);
				dc.onReadyDelete(index);
			}
		});

		h.tvDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				remove(index);
				dc.onDelete();
				Log.i(TAG,"onDelete Item Clicked");
				ProjectUtil.setIdListSharedPreferenceSB(context, items);
			}
		});
		// 初始化一下返回的item，不然有时会显示错误
		h.tvDelete.setVisibility(View.GONE);
		h.ivReadyDelete.setVisibility(View.VISIBLE);
		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		TextView tvDelete;
		ImageView ivDragHandle;
		ImageView ivReadyDelete;
	}

	OnDeleteClick dc;

	public void setOnDeleteClick(OnDeleteClick dc) {
		this.dc = dc;
	}

	public interface OnDeleteClick {
		public void onReadyDelete(int postion);

		public void onDelete();
	}

}
