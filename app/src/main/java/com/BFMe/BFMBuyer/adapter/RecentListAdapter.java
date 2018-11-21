package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.PushMainList;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：最近联系人适配
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/6 14:50
 */

public class RecentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RecentContact> mRecents = new ArrayList<>();
    List<PushMainList.DataBean> mPushData = new ArrayList<>();
    private static final int ITEM_PUSH = 1; //推送通知
    private static final int ITEM_COMMENT = 2; //最新评论
    private static final int ITEM_HISTORY = 3;//热门话题列表
    private static final int ITEM_RECENT_MESSAGE = 4;//推荐话题

    public RecentListAdapter(Context context, List<RecentContact> recents, List<PushMainList.DataBean> pushData) {
        this.mContext = context;
        if (recents != null && recents.size() > 0) {
            mRecents.addAll(recents);
        }
        if (pushData != null && pushData.size() > 0) {
            mPushData.addAll(pushData);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 3) {
            return ITEM_PUSH;
        } else if (position == 3) {
            return ITEM_COMMENT;
        } else if (position == 4) {
            return ITEM_HISTORY;
        } else {
            return ITEM_RECENT_MESSAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case ITEM_PUSH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_push_list, parent, false);
                viewHolder = new PushViewHolder(view);
                break;
            case ITEM_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                viewHolder = new CommentViewHolder(view);
                break;
            case ITEM_HISTORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list, parent, false);
                viewHolder = new HistoryViewHolder(view);
                break;
            case ITEM_RECENT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_list, parent, false);
                viewHolder = new RecentViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_PUSH:
                PushViewHolder pushViewHolder = (PushViewHolder) holder;
                pushViewHolder.setData();
                break;
            case ITEM_COMMENT:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.setData();
                break;
            case ITEM_HISTORY:
                HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
                historyViewHolder.setData();
                break;
            case ITEM_RECENT_MESSAGE:
                RecentViewHolder rvholder = (RecentViewHolder) holder;
                rvholder.setData();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecents.size() + mPushData.size() + 2;
    }

    private class PushViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_push_item;
        private ImageView iv_icon;
        private View is_remind;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;

        private PushViewHolder(View itemView) {
            super(itemView);
            rl_push_item = (RelativeLayout) itemView.findViewById(R.id.rl_push_item);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            is_remind = itemView.findViewById(R.id.is_remind);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void setData() {
            PushMainList.DataBean pushItem = mPushData.get(getAdapterPosition());

            if (getAdapterPosition() == 0) {
                tv_title.setText(mContext.getString(R.string.official_recommend));
                iv_icon.setBackgroundResource(R.drawable.official);
            } else if (getAdapterPosition() == 1) {
                tv_title.setText(mContext.getString(R.string.topic_choiceness));
                iv_icon.setBackgroundResource(R.drawable.topic);
            } else {
                tv_title.setText(mContext.getString(R.string.logistics_inform));
                iv_icon.setBackgroundResource(R.drawable.logistics);
            }

            tv_content.setText(pushItem.getContent());
            tv_time.setText(pushItem.getDate());
            if(pushItem.getIsRead() == 1) {
                is_remind.setVisibility(View.VISIBLE);
            }else {
                is_remind.setVisibility(View.GONE);
            }

            rl_push_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.PushOnClick(getAdapterPosition(), tv_title.getText().toString());
                }
            });
        }
    }

    /**
     * 最新评论
     */
    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_new_comment;

        private CommentViewHolder(View itemView) {
            super(itemView);
            rl_new_comment = (RelativeLayout) itemView.findViewById(R.id.rl_new_comment);
        }

        public void setData() {
            rl_new_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.CommentOnClickListener();
                }
            });
        }
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        private HistoryViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public void setData() {
            if(mRecents.size() == 0) {
                tv_title.setText(mContext.getString(R.string.history_message_empt));
            }else {
                tv_title.setText(mContext.getString(R.string.history_message));
            }
        }
    }

    /**
     * 最近联系人列表
     */
    private class RecentViewHolder extends RecyclerView.ViewHolder {
        private HeadImageView ivHead;
        private TextView tvUserName;
        private TextView tvTime;
        private TextView tvNumber;
        private TextView tvContent;
        private RelativeLayout rv_layout;

        private RecentViewHolder(View itemView) {
            super(itemView);
            rv_layout = (RelativeLayout) itemView.findViewById(R.id.rv_layout);
            ivHead = (HeadImageView) itemView.findViewById(R.id.iv_head);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
        }

        public void setData() {
            final RecentContact item = mRecents.get(getAdapterPosition() - 5);

            if (item.getUnreadCount() > 0) {
                tvNumber.setVisibility(View.VISIBLE);
                tvNumber.setText(item.getUnreadCount() + "");
            } else {
                tvNumber.setVisibility(View.INVISIBLE);
            }
            MoonUtil.identifyFaceExpressionAndTags(mContext, tvContent, item.getContent(), ImageSpan.ALIGN_BOTTOM, 0.45f);
            String timeString = TimeUtil.getTimeShowString(item.getTime(), true);
            tvTime.setText(timeString);
            ivHead.loadBuddyAvatar(item.getContactId());

            tvUserName.setText(UserInfoHelper.getUserTitleName(item.getContactId(),
                    item.getSessionType()));
            //tvUserName.setText(item.getFromNick());
            rv_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.RecentOnClickListener(item.getContactId());
                }
            });
            rv_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.OnLongClickListener(getAdapterPosition(), item);
                    return false;
                }
            });
        }
    }

    public void deleteItem(int position) {
        mRecents.remove(position);
    }


    public interface OnItemClickListener {
        void RecentOnClickListener(String s);

        void CommentOnClickListener();

        void PushOnClick(int position, String title);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnLongClickListener {
        void OnLongClickListener(int position, RecentContact s);
    }

    private OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


}
