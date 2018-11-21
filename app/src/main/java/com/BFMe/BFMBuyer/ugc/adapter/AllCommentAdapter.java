package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.CommentItem;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：全部评论adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/8 14:56
 */
public class AllCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentItem> mCommentList = new ArrayList<>();

    public AllCommentAdapter(List<CommentItem> commentList) {
        if (commentList != null && commentList.size() > 0) {
            mCommentList.clear();
            mCommentList.addAll(commentList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ta_talk_comment, parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public void cleanData() {
        mCommentList.clear();
    }

    public void addData(List<CommentItem> commentList) {
        addData(0, commentList);
    }

    public void addData(int position, List<CommentItem> commentList) {
        if(commentList != null && commentList.size() > 0) {
            mCommentList.addAll(position,commentList);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
//        private RelativeLayout rv_item_comment;
        private CircularImageView civ_comment_head;
        private TextView tv_comment_name;
        private TextView tv_comment_content;
        private TextView tv_comment_time;
        private CheckBox cb_praise;
        private Context mContext;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
//            rv_item_comment = (RelativeLayout) itemView.findViewById(rv_item_comment);
            civ_comment_head = (CircularImageView) itemView.findViewById(R.id.civ_comment_head);
            tv_comment_name = (TextView) itemView.findViewById(R.id.tv_comment_name);
            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            tv_comment_time = (TextView) itemView.findViewById(R.id.tv_comment_time);
            cb_praise = (CheckBox) itemView.findViewById(R.id.cb_praise);
            mContext = context;
        }

        public void setData() {
            CommentItem item = mCommentList.get(getLayoutPosition() - 1);
            Glide
                    .with(mContext)
                    .load(item.getPhoto())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.rabit)
                    .into(civ_comment_head);
            tv_comment_name.setText(item.getUserName());
            tv_comment_content.setText(item.getContent());
            tv_comment_time.setText(item.getCreateDate());
            cb_praise.setText(String.valueOf(item.getParseCount()));
            cb_praise.setChecked(item.getIsParse() == 1);

            setListener(item);
        }

        private void setListener(final CommentItem item) {
            civ_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommentItemClickListener.userOnClick(item.getEncryptUserId());
                }
            });
            cb_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onCommentItemClickListener.praiseOnClick(v,item);
                }
            });
        }
    }

    private OnCommentItemClickListener onCommentItemClickListener;
    public interface OnCommentItemClickListener{
        void praiseOnClick(View v,CommentItem item);
//        String commentId
        void userOnClick(String userId);
    }

    public void setOnCommentItemClickListener(OnCommentItemClickListener onCommentItemClickListener) {
        this.onCommentItemClickListener = onCommentItemClickListener;
    }
}
