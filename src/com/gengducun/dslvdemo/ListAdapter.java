package com.gengducun.dslvdemo;

import java.util.ArrayList;

import com.mobeta.android.dslv.DragSortListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";
	/** 产品信息列表 */
	private ArrayList<InfoBean> productInfoList;
	/** 布局加载器 */
	private LayoutInflater mInflater;
	/** 本类适配器对象 **/
	private ListAdapter mProductListAdapter1;
	/** 选中项位置 **/
	private int mSelectPosition = -1;

	public ListAdapter(ArrayList<InfoBean> productInfoList, Context context,
			DragSortListView mDslvProductList) {
		super();
		this.productInfoList = productInfoList;
		this.mInflater = LayoutInflater.from(context);
		this.mProductListAdapter1 = this;
	}

	@Override
	public int getCount() {
		return null == productInfoList ? 0 : productInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return productInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getmSelectPosition() {
		return mSelectPosition;
	}

	public void setmSelectPosition(int mSelectPosition) {
		this.mSelectPosition = mSelectPosition;
	}

	/**
	 * 删除产品
	 * 
	 * @param productInfoBean
	 */
	public void remove(InfoBean productInfoBean) {
		synchronized (this) {
			productInfoList.remove(productInfoBean);
		}
		notifyDataSetChanged();
	}

	/**
	 * 删除产品
	 * 
	 * @param which
	 */
	public void remove(int which) {
		synchronized (this) {
			productInfoList.remove(which);
		}
		notifyDataSetChanged();
	}

	/**
	 * 将产品移动到指定的位置 1、判断移动的项是否是选中项,如果是则选中项设置为to
	 * 2、如果移动项不是选中项则有两种情况产生，移动到的位置在当前选中项之前，则将选中项设置为to + 1
	 * 
	 * @param productInfoBean
	 * @param from
	 *            移动之前的位置
	 * @param to
	 *            移动完成之后，该产品所在的位置
	 */
	public void insert(InfoBean productInfoBean, int from, int to) {
		synchronized (this) {
			Log.i(TAG, "from=" + from + "      to =" + to);
			if (mSelectPosition != -1) {// 有选中项的前提下
				if ((from < mSelectPosition && to < mSelectPosition)
						|| (from > mSelectPosition && to > mSelectPosition)) {
					// 在选中项之前的位置进行拖拉或者在选中项之后的区域拖拉，不会影响选中项
				} else if (from < mSelectPosition && to > mSelectPosition) {
					// 将选中项之前的移动到选中项之后的位置,则选中项的索引需要发生变化,应该是选中项的位置-1
					mSelectPosition = mSelectPosition - 1;
				} else if (from > mSelectPosition && to < mSelectPosition) {
					// 将选中项之后的项移动到选中项之前的位置，索引也需要弯化,应该是选中项的位置+1
					mSelectPosition = mSelectPosition + 1;
				} else if ((from == mSelectPosition && from > to) || (from == mSelectPosition && from < to)) {
					// 如果将选中项往选中项前面的方向拖，则选中项的索引也需要变化,应该是拖动到的位置
					mSelectPosition = to;
				} else if ((from < mSelectPosition && to == mSelectPosition)) {
					// 将选中项之前的移动到当前选中项的位置,索引需要变化,选中项的索引应该是 选中项的值-1
					mSelectPosition = mSelectPosition - 1;
				} else if (from > mSelectPosition && to == mSelectPosition) {
					// 将选中项之后的移动到当前项的位置，索引需要弯化，选中项的索引应该是选中项的值+1
					mSelectPosition = mSelectPosition + 1;
				}
			}
			productInfoList.add(to, productInfoBean);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if (null == convertView) {
			view = mInflater.inflate(R.layout.item_groups_manager, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
			convertView = view;
		} else {
			view = convertView;
			viewHolder = (ViewHolder) convertView.getTag();
		}
		InfoBean bean = productInfoList.get(position);
		viewHolder.setData(bean);
		return view;
	}

	class ViewHolder {
		TextView tv;

		public ViewHolder(View view) {
			tv = (TextView) view.findViewById(R.id.groups_manager_tv);
		}

		public void setData(InfoBean bean) {
			tv.setText(bean.getProductName());
		}
	}

	/**
	 * 单选按钮事件监听
	 * 
	 * @author Wilson
	 */
	class ProductClickListener implements OnClickListener {
		private int position;

		public ProductClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (mSelectPosition == position) {
				mSelectPosition = -1;
			} else {
				mSelectPosition = position;
			}
			Log.i(TAG, "mSelectPosition=" + mSelectPosition + "   position=" + position);
			mProductListAdapter1.notifyDataSetChanged();
		}
	}

	/**
	 * 删除当前产品项监听事件
	 */
	class RemoveItemClickListener implements OnClickListener {
		private int position;

		public RemoveItemClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 1、删除集合中的数据
			productInfoList.remove(position);
			// 2、删除服务器端的数据
			mProductListAdapter1.notifyDataSetChanged();
			//
			mSelectPosition = -1;
		}
	}
}
