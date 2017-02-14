package cn.qatime.player.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.List;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.ImageBucket;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageBucket>
{
	private ListView mListDir;

	public ListImageDirPopupWindow(int width, int height,
								   List<ImageBucket> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageBucket>(context, mDatas,
				R.layout.list_dir_item)
		{
			@Override
			public void convert(ViewHolder holder, ImageBucket item, int position) {
				holder.setText(R.id.id_dir_item_name, item.bucketName);
				holder.setImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				holder.setText(R.id.id_dir_item_count, item.count+ "张");
			}
		});
	}

	public interface OnImageDirSelected
	{
		void selected(ImageBucket floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
		this.mImageDirSelected.selected(mDatas.get(0));
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
