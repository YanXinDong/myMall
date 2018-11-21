package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.activity.PhotoPickerActivity;
import com.BFMe.BFMBuyer.ugc.bean.Photo;
import com.BFMe.BFMBuyer.utils.OtherUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



/**
 * @Class: PhotoAdapter
 * @Description: 图片适配器
 */
public class PhotoAdapter extends BaseAdapter {

    private List<Photo> mDatas;
    //存放已选中的Photo数据
    private List<String> mSelectedPhotos;
    private Context mContext;
    private int mWidth;
    //图片选择数量
    private int mMaxNum = PhotoPickerActivity.DEFAULT_NUM;

    private View.OnClickListener mOnPhotoClick;
    private PhotoClickCallBack mCallBack;

    public PhotoAdapter(Context context, List<Photo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4))/3;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Photo getItem(int position) {
        if(mDatas == null || mDatas.size() == 0){
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }


    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public void setPhotoClickCallBack(PhotoClickCallBack callback) {
        mCallBack = callback;
    }


    /**
     * 获取已选中相片
     * @return
     */
    public List<String> getmSelectedPhotos() {
        return mSelectedPhotos;
    }


    public void setSelectMode() {
        initMultiMode();
    }

    /**
     * 初始化多选模式所需要的参数
     */
    private void initMultiMode() {
        mSelectedPhotos = new ArrayList<>();
        mOnPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = v.findViewById(R.id.imageview_photo).getTag(R.id.imageid).toString();
                if(mSelectedPhotos.contains(path)) {
                    v.findViewById(R.id.mask).setVisibility(View.GONE);
                    v.findViewById(R.id.checkmark).setSelected(false);
                    mSelectedPhotos.remove(path);
                } else {
                    if(mSelectedPhotos.size() >= mMaxNum) {
                        Toast.makeText(mContext, R.string.msg_maxi_capacity,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectedPhotos.add(path);
                    v.findViewById(R.id.mask).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.checkmark).setSelected(true);
                }
                if(mCallBack != null) {
                    mCallBack.onPhotoClick();
                }
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_photo_layout, null);
            holder.photoImageView = (ImageView) convertView.findViewById(R.id.imageview_photo);
            holder.selectView = (ImageView) convertView.findViewById(R.id.checkmark);
            holder.maskView = convertView.findViewById(R.id.mask);
            holder.wrapLayout = (FrameLayout) convertView.findViewById(R.id.wrap_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.photoImageView.setImageResource(R.drawable.ic_photo_loading);
        Photo photo = getItem(position);
        holder.photoImageView.setTag(R.id.imageid,photo.getPath());
        holder.wrapLayout.setOnClickListener(mOnPhotoClick);
        holder.selectView.setVisibility(View.VISIBLE);
        if(mSelectedPhotos != null && mSelectedPhotos.contains(photo.getPath())) {
            holder.selectView.setSelected(true);
            holder.maskView.setVisibility(View.VISIBLE);
        } else {
            holder.selectView.setSelected(false);
            holder.maskView.setVisibility(View.GONE);
        }
        Glide
                .with(mContext)
                .load(photo.getPath())
                .into(holder.photoImageView);
        return convertView;
    }

    private class ViewHolder {
        private ImageView photoImageView;
        private ImageView selectView;
        private View maskView;
        private FrameLayout wrapLayout;
    }

    /**
     * 多选时，点击相片的回调接口
     */
    public interface PhotoClickCallBack {
        void onPhotoClick();
    }
}
