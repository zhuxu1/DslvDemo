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
	/** ��Ʒ��Ϣ�б� */
	private ArrayList<InfoBean> productInfoList;
	/** ���ּ����� */
	private LayoutInflater mInflater;
	/** �������������� **/
	private ListAdapter mProductListAdapter1;
	/** ѡ����λ�� **/
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
	 * ɾ����Ʒ
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
	 * ɾ����Ʒ
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
	 * ����Ʒ�ƶ���ָ����λ�� 1���ж��ƶ������Ƿ���ѡ����,�������ѡ��������Ϊto
	 * 2������ƶ����ѡ����������������������ƶ�����λ���ڵ�ǰѡ����֮ǰ����ѡ��������Ϊto + 1
	 * 
	 * @param productInfoBean
	 * @param from
	 *            �ƶ�֮ǰ��λ��
	 * @param to
	 *            �ƶ����֮�󣬸ò�Ʒ���ڵ�λ��
	 */
	public void insert(InfoBean productInfoBean, int from, int to) {
		synchronized (this) {
			Log.i(TAG, "from=" + from + "      to =" + to);
			if (mSelectPosition != -1) {// ��ѡ�����ǰ����
				if ((from < mSelectPosition && to < mSelectPosition)
						|| (from > mSelectPosition && to > mSelectPosition)) {
					// ��ѡ����֮ǰ��λ�ý�������������ѡ����֮�����������������Ӱ��ѡ����
				} else if (from < mSelectPosition && to > mSelectPosition) {
					// ��ѡ����֮ǰ���ƶ���ѡ����֮���λ��,��ѡ�����������Ҫ�����仯,Ӧ����ѡ�����λ��-1
					mSelectPosition = mSelectPosition - 1;
				} else if (from > mSelectPosition && to < mSelectPosition) {
					// ��ѡ����֮������ƶ���ѡ����֮ǰ��λ�ã�����Ҳ��Ҫ�仯,Ӧ����ѡ�����λ��+1
					mSelectPosition = mSelectPosition + 1;
				} else if ((from == mSelectPosition && from > to) || (from == mSelectPosition && from < to)) {
					// �����ѡ������ѡ����ǰ��ķ����ϣ���ѡ���������Ҳ��Ҫ�仯,Ӧ�����϶�����λ��
					mSelectPosition = to;
				} else if ((from < mSelectPosition && to == mSelectPosition)) {
					// ��ѡ����֮ǰ���ƶ�����ǰѡ�����λ��,������Ҫ�仯,ѡ���������Ӧ���� ѡ�����ֵ-1
					mSelectPosition = mSelectPosition - 1;
				} else if (from > mSelectPosition && to == mSelectPosition) {
					// ��ѡ����֮����ƶ�����ǰ���λ�ã�������Ҫ�仯��ѡ���������Ӧ����ѡ�����ֵ+1
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
	 * ��ѡ��ť�¼�����
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
	 * ɾ����ǰ��Ʒ������¼�
	 */
	class RemoveItemClickListener implements OnClickListener {
		private int position;

		public RemoveItemClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 1��ɾ�������е�����
			productInfoList.remove(position);
			// 2��ɾ���������˵�����
			mProductListAdapter1.notifyDataSetChanged();
			//
			mSelectPosition = -1;
		}
	}
}
