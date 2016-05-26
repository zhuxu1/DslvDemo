package com.gengducun.dslvdemo;

import java.util.ArrayList;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	/** ��Ʒ��Ϣ�б� **/
	private ArrayList<InfoBean> dataInfoList;
	/** ��Ʒ�б�ؼ� **/
	private DragSortListView mDslvList;
	/** ��Ʒ��Ϣ�б� */
	private ListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initList();
	}

	private void initList() {
		mDslvList = (DragSortListView) findViewById(R.id.dslv_product_list);
		mListAdapter = new ListAdapter(dataInfoList, this, mDslvList);
		mDslvList.setAdapter(mListAdapter);

		mDslvList.setDropListener(new DropListener() {

			@Override
			public void drop(int from, int to) {
				if (from != to) {
					InfoBean item = (InfoBean) mListAdapter.getItem(from);
					mListAdapter.remove(item);
					mListAdapter.insert(item, from, to);
					mDslvList.moveCheckState(from, to);
				}
			}
		});
		mDslvList.setRemoveListener(new RemoveListener() {

			@Override
			public void remove(final int which) {
				mListAdapter.remove(which);
				// new AlertDialog.Builder(MainActivity.this).setTitle("ע��")
				// .setMessage("��ȷ��Ҫɾ�� " +
				// dataInfoList.get(which).getProductName() + " ��")
				// .setPositiveButton("ɾ��", new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int _which) {
				// }
				// }).setNegativeButton("�����", new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// mListAdapter.notifyDataSetChanged();
				// }
				// }).create().show();
			}
		});
		// mDslvProductList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private void initData() {
		dataInfoList = new ArrayList<InfoBean>();
		InfoBean bean;
		for (int i = 0; i < 10; i++) {
			bean = new InfoBean("item " + i);
			dataInfoList.add(bean);
		}
	}
}
