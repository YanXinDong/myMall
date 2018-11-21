package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.OrderDetailBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 * 评论页面适配器
 */
public class CommentListAdapter extends BaseAdapter{

    private Context mContext;
    private List<OrderDetailBean.OrderItemInfoBean> mOrderInfo = new ArrayList<>();

    private int mPosition;
    private HashMap<Integer,String>  contexts = new HashMap<>();
    private HashMap<Integer,Integer>  scores = new HashMap<>();

    public CommentListAdapter(Context context, List<OrderDetailBean.OrderItemInfoBean> orderInfo) {
        mContext = context;
        if(orderInfo != null && orderInfo.size() > 0) {
            mOrderInfo.clear();
            mOrderInfo.addAll(orderInfo);

            //初始化评分数据
            for(int i = 0; i < orderInfo.size(); i++) {
                scores.put(i,5);
            }
        }
    }

    @Override
    public int getCount() {
        return mOrderInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_list,parent,false);
            viewHodler = new ViewHodler(convertView,position);

            convertView.setTag(viewHodler);

        }else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        Glide
                .with(mContext)
                .load(mOrderInfo.get(position).getThumbnailsUrl())
                .placeholder(R.drawable.zhanwei1)
                .into(viewHodler.ivCommentPig);

        final ViewHodler finalViewHodler = viewHodler;
        viewHodler.ivPicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = 1;
                onClickListenerUploading.onUploadingClick(mPosition,position,v);
                onClickListenerUploading.onReturnView(finalViewHodler.ivPicture1,finalViewHodler.ivPicture2,finalViewHodler.ivPicture3);
            }
        });
        viewHodler.ivPicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = 2;
                onClickListenerUploading.onUploadingClick(mPosition,position,v);
                onClickListenerUploading.onReturnView(finalViewHodler.ivPicture1,finalViewHodler.ivPicture2,finalViewHodler.ivPicture3);
            }
        });
        viewHodler.ivPicture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = 3;
                onClickListenerUploading.onUploadingClick(mPosition,position,v);
                onClickListenerUploading.onReturnView(finalViewHodler.ivPicture1,finalViewHodler.ivPicture2,finalViewHodler.ivPicture3);
            }
        });

        viewHodler.rbStart.setOnRatingChangeListener(new com.BFMe.BFMBuyer.view.RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                scores.put(position, (int)ratingCount);
            }
        });
        onClickListenerUploading.onReturnDatas(contexts, scores);

        return convertView;
    }

    /**
     * 定义接口实现上传图片的点击事件监听
     */
    public interface OnClickListenerUploading {
        void onUploadingClick(int mPosition, int itemPosition, View view);

        //返回需要的控件
        void onReturnView(View view1,View view2,View view3);

        //返回需要的数据
        void onReturnDatas(HashMap<Integer,String>  contexts, HashMap<Integer,Integer>  scores);
    }

    private OnClickListenerUploading onClickListenerUploading;

    public void setOnClickListenerDelete(OnClickListenerUploading onClickListenerUploading) {
        this.onClickListenerUploading = onClickListenerUploading;
    }

    class ViewHodler{

        private ImageView ivCommentPig;
        private EditText etCommDesc;
        private ImageView ivPicture1;
        private ImageView ivPicture2;
        private ImageView ivPicture3;
        private com.BFMe.BFMBuyer.view.RatingBar rbStart;

        public ViewHodler(View convertView, int position) {

            //商品评价商品图片
            ivCommentPig = (ImageView) convertView.findViewById(R.id.iv_comment_pig);
            //商品描述
            etCommDesc = (EditText) convertView.findViewById(R.id.et_comm_desc);
            //添加的图片
            ivPicture1 = (ImageView) convertView.findViewById(R.id.iv_picture1);
            ivPicture2 = (ImageView) convertView.findViewById(R.id.iv_picture2);
            ivPicture3 = (ImageView) convertView.findViewById(R.id.iv_picture3);
            //商品评星级
            rbStart = (com.BFMe.BFMBuyer.view.RatingBar) convertView.findViewById(R.id.rb_start);

            etCommDesc.setTag(position);
            etCommDesc.addTextChangedListener(new TextSwitcher(this));
        }
    }

    class TextSwitcher implements TextWatcher {
        private ViewHodler mHolder;

        public TextSwitcher(ViewHodler mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (int) mHolder.etCommDesc.getTag();
            contexts.put(position, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
