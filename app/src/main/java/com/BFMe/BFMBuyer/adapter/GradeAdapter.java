package com.BFMe.BFMBuyer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.GradeActivity;
import com.BFMe.BFMBuyer.javaBean.GradeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：积分明细
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/15 13:14
 */
public class GradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private GradeActivity mActivity;
    private List<GradeBean.MemberIntegralRecordBean> mList = new ArrayList<>();

    public GradeAdapter(GradeActivity gradeActivity, List<GradeBean.MemberIntegralRecordBean> memberIntegralRecord) {
        this.mActivity = gradeActivity;
        if (memberIntegralRecord != null && memberIntegralRecord.size() > 0) {
            mList.clear();
            mList.addAll(memberIntegralRecord);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_grade, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvTypedesc.setText(mList.get(position).getTypeDesc());
        viewHolder.tvIntegral.setText(mList.get(position).getIntegral() + "");
        viewHolder.tvRecorddate.setText(mList.get(position).getRecordDate() + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTypedesc;
        TextView tvIntegral;
        TextView tvRecorddate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTypedesc = (TextView) itemView.findViewById(R.id.tv_typedesc);
            tvIntegral = (TextView) itemView.findViewById(R.id.tv_integral);
            tvRecorddate = (TextView) itemView.findViewById(R.id.tv_recorddate);
        }
    }

    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, List<GradeBean.MemberIntegralRecordBean> datas) {
        if (datas != null && datas.size() > 0) {
            mList.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<GradeBean.MemberIntegralRecordBean> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mList.clear();
        notifyDataSetChanged();
    }
}
