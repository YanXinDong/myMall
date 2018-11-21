package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.BaseViewPagerAdapter;
import com.BFMe.BFMBuyer.ugc.bean.TopicDetails;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：Ta说详情adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/7 15:07
 */
public class TaTalkDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int USER_INFO = 1; //用户信息
    private static final int TA_TALK_CONTENT = 2; //ta说图片 内容
    private static final int RELEVANCE_COMMODITY = 3;//关联商品
    private static final int TA_TALK_INFO = 4;//日期，点赞数，评论数
    private static final int HEAD_LIST = 5;//点赞头像列表
    private static final int COMMENT_LIST = 6;//评论
    private static final int COMMENT_ALL = 7;//查看全部评论
    private static final int NULL = 8;//空

    private TopicDetails mTopicDetails;
    private List<TopicDetails.TopicPhotosBean> mTopicPhotos = new ArrayList<>();
    private List<String> mImageUrl = new ArrayList<>();
    private List<TopicDetails.TopicproductlistBean> mTopicProductList = new ArrayList<>(3);
    private List<TopicDetails.TopicparseuserlistBean> mTopicPraiseList = new ArrayList<>(8);
    private List<TopicDetails.TopiccommentlistBean> mTopicCommentList = new ArrayList<>(3);

    private Context mContext;
    public TaTalkDetailsAdapter(Context context, TopicDetails topicDetails) {
        mContext = context;
        if (topicDetails != null) {
            mTopicDetails = topicDetails;
            initData();
        }
    }

    private void initData() {
        mTopicPhotos = mTopicDetails.getTopicPhotos();
        mTopicProductList = mTopicDetails.getTopicproductlist();
        mTopicPraiseList = mTopicDetails.getTopicparseuserlist();
        mTopicCommentList = mTopicDetails.getTopiccommentlist();
        for (int i = 0; i < mTopicPhotos.size(); i++) {
            mImageUrl.add(mTopicPhotos.get(i).getImageUrl());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = TA_TALK_CONTENT;
        } else if (position == 1) {
            type = USER_INFO;
        } else if (position >= 2 && position < mTopicProductList.size() + 2) {
            type = RELEVANCE_COMMODITY;
        } else if (position == mTopicProductList.size() + 2) {
            type = TA_TALK_INFO;
        } else if (position == mTopicProductList.size() + 3 && mTopicPraiseList.size() > 0) {
            type = HEAD_LIST;
        } else if (position >= mTopicProductList.size() + 4 && position < mTopicProductList.size() + 4 + mTopicCommentList.size()) {
            type = COMMENT_LIST;
        } else if (position == mTopicProductList.size() + 4 + mTopicCommentList.size()) {
            type = COMMENT_ALL;
        } else {
            type = NULL;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case USER_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info, parent, false);
                viewHolder = new UserInfoViewHolder(view);
                break;
            case TA_TALK_CONTENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_content, parent, false);
                viewHolder = new TaTalkContentViewHolder(view);
                break;
            case RELEVANCE_COMMODITY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relevance_commodity, parent, false);
                viewHolder = new RelevanceCommodityViewHolder(view);
                break;
            case TA_TALK_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_info, parent, false);
                viewHolder = new TaTalkInfoViewHolder(view);
                break;
            case HEAD_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_list, parent, false);
                viewHolder = new HeadListViewHolder(view, parent.getContext());
                break;
            case COMMENT_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ta_talk_comment, parent, false);
                viewHolder = new CommentListViewHolder(view);
                break;
            case COMMENT_ALL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_all, parent, false);
                viewHolder = new CommentAllViewHolder(view);
                break;
            case NULL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_null, parent, false);
                viewHolder = new NullViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case USER_INFO:
                UserInfoViewHolder userInfoViewHolder = (UserInfoViewHolder) holder;
                userInfoViewHolder.setData();
                break;
            case TA_TALK_CONTENT:
                TaTalkContentViewHolder taTalkContentViewHolder = (TaTalkContentViewHolder) holder;
                taTalkContentViewHolder.setData();
                break;
            case RELEVANCE_COMMODITY:
                RelevanceCommodityViewHolder relevanceCommodityViewHolder = (RelevanceCommodityViewHolder) holder;
                relevanceCommodityViewHolder.setData();
                break;
            case TA_TALK_INFO:
                TaTalkInfoViewHolder taTalkInfoViewHolder = (TaTalkInfoViewHolder) holder;
                taTalkInfoViewHolder.setData();
                break;
            case HEAD_LIST:
                HeadListViewHolder headListViewHolder = (HeadListViewHolder) holder;
                headListViewHolder.setData();
                break;
            case COMMENT_LIST:
                CommentListViewHolder commentListViewHolder = (CommentListViewHolder) holder;
                commentListViewHolder.setData();
                break;
            case COMMENT_ALL:
                CommentAllViewHolder commentAllViewHolder = (CommentAllViewHolder) holder;
                commentAllViewHolder.setData();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTopicProductList.size() + 5 + mTopicCommentList.size();
    }

    public void clearData() {
        mTopicDetails = null;
        mTopicPhotos.clear();
        mImageUrl.clear();
        mTopicPraiseList.clear();
        mTopicCommentList.clear();
        mTopicCommentList.clear();
    }

    public void addData(TopicDetails topicDetails) {
        mTopicDetails = topicDetails;
        initData();
        notifyDataSetChanged();
    }

    private class UserInfoViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView civ_user_head;
        private TextView tv_user_name;
        private CheckBox cb_attention;
        private TextView tv_talk_content;

        private Context mContext;

        private UserInfoViewHolder(View itemView) {
            super(itemView);
            civ_user_head = (CircularImageView) itemView.findViewById(R.id.civ_user_head);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            cb_attention = (CheckBox) itemView.findViewById(R.id.cb_attention);
            tv_talk_content = (TextView) itemView.findViewById(R.id.tv_talk_content);

            mContext = itemView.getContext();
        }

        private void setData() {
            Glide
                    .with(mContext)
                    .load(mTopicDetails.getUserPhoto())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.rabit)
                    .into(civ_user_head);
            tv_user_name.setText(mTopicDetails.getUserName());
            cb_attention.setChecked(mTopicDetails.getIsFoucsUser() == 1);

            String content = mTopicDetails.getContent();
            content = content.replace("\r", "\n");
            tv_talk_content.setText(content);

            setListener();
        }

        private void setListener() {
            civ_user_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taTalkOnClickListener.userTopicOnClick(mTopicDetails.getEncryptId());
                }
            });
            cb_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taTalkOnClickListener.userAttentionOnClick(v, mTopicDetails.getEncryptId());
                }
            });
        }
    }

    private class TaTalkContentViewHolder extends RecyclerView.ViewHolder {
        private ViewPager vp_content;
        private TextView tv_number;

        private TaTalkContentViewHolder(View itemView) {
            super(itemView);
            vp_content = (ViewPager) itemView.findViewById(R.id.vp_content);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
        }

        private void setData() {
            tv_number.setText(1 + "/" + mImageUrl.size());
            vp_content.setOffscreenPageLimit(2);
            vp_content.setAdapter(new BaseViewPagerAdapter(mImageUrl,true));
            vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //以去除动态设置高度
//
//                    int height;
//                    if (mTopicPhotos.size() == 1) {//如果只有一张图片
//                        height = mTopicPhotos.get(position).getImageHeight() == 0 ? 400 : mTopicPhotos.get(position).getImageHeight();
//                    } else {
//                        if (position == mTopicPhotos.size() - 1) {
//                            return;
//                        }
//
//                        //计算ViewPager现在应该的高度
//                        height = (int) ((mTopicPhotos.get(position).getImageHeight() == 0 ? 400 : mTopicPhotos.get(position).getImageHeight())
//                                * (1 - positionOffset) +
//                                (mTopicPhotos.get(position + 1).getImageHeight() == 0 ? 400 : mTopicPhotos.get(position + 1).getImageHeight())
//                                        * positionOffset);
//                    }
//
//                    //为ViewPager设置高度
//                    ViewGroup.LayoutParams params = vp_content.getLayoutParams();
//                    params.height = height;
//                    vp_content.setLayoutParams(params);
                }

                @Override
                public void onPageSelected(int position) {
                    if (mImageUrl.size() > 0) {
                        tv_number.setText(position % mImageUrl.size() + 1 + "/" + mImageUrl.size());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        }
    }

    private class RelevanceCommodityViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_layout;
        private ImageView iv_relevance_commodity;
        private TextView tv_relevance_commodity_name;
        private TextView tv_relevance_commodity_price;
        private Context mContext;

        private RelevanceCommodityViewHolder(View itemView) {
            super(itemView);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            iv_relevance_commodity = (ImageView) itemView.findViewById(R.id.iv_relevance_commodity);
            tv_relevance_commodity_name = (TextView) itemView.findViewById(R.id.tv_relevance_commodity_name);
            tv_relevance_commodity_price = (TextView) itemView.findViewById(R.id.tv_relevance_commodity_price);
            mContext = itemView.getContext();
        }

        private void setData() {
            final TopicDetails.TopicproductlistBean item = mTopicProductList.get(getLayoutPosition() - 2);
            Glide
                    .with(mContext)
                    .load(item.getPhoto())
                    .placeholder(R.drawable.zhanwei1)
                    .error(R.drawable.zhanwei1)
                    .into(iv_relevance_commodity);
            tv_relevance_commodity_name.setText(item.getTitle());
            tv_relevance_commodity_price.setText(Utils.doubleSave2(item.getPrise()));
            rl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taTalkOnClickListener.relevanceCommodityOnClick(item.getId());
                }
            });
        }


    }

    private class TaTalkInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_time;
        private TextView iv_commodity;
        private CheckBox cb_praise;
        private Context mContext;

        private TaTalkInfoViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_commodity = (TextView) itemView.findViewById(R.id.iv_commodity);
            cb_praise = (CheckBox) itemView.findViewById(R.id.cb_praise);
            mContext = itemView.getContext();
        }

        private void setData() {
            if (mTopicDetails.getState() != 1) {//审核中与审核未通过
                iv_commodity.setVisibility(View.GONE);
                cb_praise.setVisibility(View.GONE);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.collect_select);
                drawable.setBounds(0, 0, UiUtils.dip2px(20), UiUtils.dip2px(20));
                cb_praise.setCompoundDrawables(drawable, null, null, null);

                Drawable drawableCommodity = mContext.getResources().getDrawable(R.drawable.icon_comment);
                drawableCommodity.setBounds(0, 0, UiUtils.dip2px(18), UiUtils.dip2px(18));
                iv_commodity.setCompoundDrawables(drawableCommodity, null, null, null);

                iv_commodity.setText(mTopicDetails.getCommentCount() + "");
//                cb_praise.setText(mTopicDetails.getParseCount() + "");
                cb_praise.setChecked(mTopicDetails.getIsParse() == 1);
            }

            tv_time.setText(mTopicDetails.getCreateDate());

            setListener();
        }

        private void setListener() {
            cb_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = cb_praise.isChecked();
                    cb_praise.setChecked(checked);
//                    if (mTopicDetails.getIsParse() == 1) {//之前赞过
//                        if (checked) {
//                            cb_praise.setText(mTopicDetails.getParseCount() + "");
//                        } else {
//                            cb_praise.setText(mTopicDetails.getParseCount() - 1 + "");
//                        }
//                    } else {
//                        if (checked) {
//                            cb_praise.setText(mTopicDetails.getParseCount() + 1 + "");
//                        } else {
//                            cb_praise.setText(mTopicDetails.getParseCount() + "");
//                        }
//                    }
                    taTalkOnClickListener.topicLikeOnClick();
                }
            });
        }
    }

    private class HeadListViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout fl_praise_list;
        private RecyclerView rv_head_list;
        private Context mContext;

        private HeadListViewHolder(View itemView, Context context) {
            super(itemView);
            fl_praise_list = (FrameLayout) itemView.findViewById(R.id.fl_praise_list);
            rv_head_list = (RecyclerView) itemView.findViewById(R.id.rv_head_list);
            mContext = context;
        }

        private void setData() {
            if (mTopicPraiseList.size() <= 0) {//无人点赞
                fl_praise_list.setVisibility(View.GONE);
            } else {
                rv_head_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                PraiseHeadAdapter praiseHeadAdapter = new PraiseHeadAdapter(mTopicPraiseList);
                rv_head_list.setAdapter(praiseHeadAdapter);
                praiseHeadAdapter.setOnItemClickListener(new PraiseHeadAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(String userId) {
                        taTalkOnClickListener.userTopicOnClick(userId);
                    }
                });

            }

        }
    }

    private class CommentListViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rv_item_comment;
        private CircularImageView civ_comment_head;
        private TextView tv_comment_name;
        private TextView tv_comment_time;
        private TextView tv_comment_content;
        private CheckBox cb_praise;
        private Context mContext;

        private CommentListViewHolder(View itemView) {
            super(itemView);
            rv_item_comment = (RelativeLayout) itemView.findViewById(R.id.rv_item_comment);
            civ_comment_head = (CircularImageView) itemView.findViewById(R.id.civ_comment_head);
            tv_comment_name = (TextView) itemView.findViewById(R.id.tv_comment_name);
            tv_comment_time = (TextView) itemView.findViewById(R.id.tv_comment_time);
            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            cb_praise = (CheckBox) itemView.findViewById(R.id.cb_praise);
            mContext = itemView.getContext();
        }

        private void setData() {
            TopicDetails.TopiccommentlistBean item = mTopicCommentList.get(getLayoutPosition() - 4 - mTopicProductList.size());
            Glide
                    .with(mContext)
                    .load(item.getUserPhoto())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.rabit)
                    .into(civ_comment_head);
            tv_comment_name.setText(item.getUserName());
            tv_comment_time.setText(item.getCreateDate());
            tv_comment_content.setText(item.getContent());
            cb_praise.setText(item.getParseCount() + "");
            cb_praise.setChecked(item.getIsParse() == 1);
            setListener(item);
        }

        private void setListener(final TopicDetails.TopiccommentlistBean item) {
            civ_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taTalkOnClickListener.userTopicOnClick(item.getEncryptUserId());
                }
            });
            cb_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = cb_praise.isChecked();
                    cb_praise.setChecked(checked);
                    if (item.getIsParse() == 1) {//之前赞过
                        if (checked) {
                            cb_praise.setText(item.getParseCount() + "");
                        } else {
                            cb_praise.setText(item.getParseCount() - 1 + "");
                        }
                    } else {
                        if (checked) {
                            cb_praise.setText(item.getParseCount() + 1 + "");
                        } else {
                            cb_praise.setText(item.getParseCount() + "");
                        }
                    }

                    taTalkOnClickListener.commentPraiseOnClick(item.getId());
                }
            });
        }
    }

    private class CommentAllViewHolder extends RecyclerView.ViewHolder {
        private Button btn_all_comment;
        private RelativeLayout rl_fail_reason;
        private TextView tv_fail_title;
        private TextView tv_fail_reason;

        private CommentAllViewHolder(View itemView) {
            super(itemView);
            btn_all_comment = (Button) itemView.findViewById(R.id.btn_all_comment);
            rl_fail_reason = (RelativeLayout) itemView.findViewById(R.id.rl_fail_reason);
            tv_fail_title = (TextView) itemView.findViewById(R.id.tv_fail_title);
            tv_fail_reason = (TextView) itemView.findViewById(R.id.tv_fail_reason);
        }

        private void setData() {
            if (mTopicDetails.getState() != 1) {
                btn_all_comment.setVisibility(View.GONE);
                rl_fail_reason.setVisibility(View.VISIBLE);
                if (mTopicDetails.getState() == 0) {//待审核
                    tv_fail_title.setText(mContext.getString(R.string.under_review));
                    tv_fail_reason.setText(mContext.getString(R.string.under_review_hint));
                } else {//审核未通过
                    tv_fail_title.setText(mContext.getString(R.string.not_pass_cause));
                    tv_fail_reason.setText(mTopicDetails.getFailReason());
                }

            } else if (mTopicCommentList.size() <= 0) {
                btn_all_comment.setVisibility(View.GONE);
            } else {
                btn_all_comment.setVisibility(View.VISIBLE);
                btn_all_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taTalkOnClickListener.allCommentOnClick();
                    }
                });
            }
        }
    }

    private class NullViewHolder extends RecyclerView.ViewHolder {

        private NullViewHolder(View itemView) {
            super(itemView);
        }
    }

    private TaTalkOnClickListener taTalkOnClickListener;

    public interface TaTalkOnClickListener {
        void userTopicOnClick(String userId);

        void userAttentionOnClick(View v, String concernUserId);

        void relevanceCommodityOnClick(String productId);

        void topicLikeOnClick();

        void commentPraiseOnClick(String commentId);

        void allCommentOnClick();
    }

    public void setTaTalkOnClickListener(TaTalkOnClickListener taTalkOnClickListener) {
        this.taTalkOnClickListener = taTalkOnClickListener;
    }
}
