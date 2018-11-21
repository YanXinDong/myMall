package com.BFMe.BFMBuyer.base.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;

/**
 * Description：分组Adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/12 17:46
 */
public abstract class SectionAdapter extends RecyclerView.Adapter<ViewHolder> {

    //item的总数，注意，是总数，包含所有项
    private int count = 0;

    //用来标记整个列表的Header
    protected static final int TYPE_HEADER = 0;  //顶部HeaderView

    //用来标记每个分组的Header
    protected static final int TYPE_SECTION_HEADER = -1;

    //用来标记每个分组的内容
    protected static final int TYPE_ITEM = -2;

    //用来保存分组section位置
    private int[] sectionForPosition = null;

    //用来保存分组内的每项的position位置
    private int[] positionWithinSection = null;

    //用来记录每个位置是否是一个组内Header
    private boolean[] isHeader = null;

    public SectionAdapter() {

        //RecyclerView采用观察者(Observer)模式，对外提供了registerDataSetObserver和unregisterDataSetObserver
        //两个方法，用来监控数据集的变化
        registerAdapterDataObserver(new SectionDataObserver());//主要用于注册与解绑适配器数据的观察者模式
    }



    //定义一个内部类，每当数据集合发生改变时，设置控件的位置信息
    class SectionDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            setupPosition();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }
    }

    protected void setupPosition() {
        count = countItems();//计算出item的总数量
        setupArrays(count);//得到item的总数量后，初始化几个数组:
        // 初始化与position相对应的section数组，
        // 初始化与section相对应的position的数组，
        // 初始化当前位置是否是一个Header的数组，
        // 初始化当前位置是否是一个Footer的数组
        calculatePositions();//通过计算每个item的位置信息，将上一步初始化后的数组填充数据，最终这几个数组保存了每个位置的item
        // 的状态信息，即：是否是header，是否是footer，所在的position是多少，所在的section是多少
    }

    /**
     * 计算item的总数量
     *
     * @return
     */
    private int countItems() {
        int count = 0;
        int sections = getSectionCount();

        for (int i = 0; i < sections; i++) {
            count += 1 + getItemCountForSection(i);
        }
        return count;
    }

    /**
     * 通过item的总数量，初始化几个数组:初始化与position相对应的section数组，
     * 初始化与section相对应的position的数组，初始化当前位置是否是一个Header的数组，
     * 初始化当前位置是否是一个Footer的数组
     *
     * @param count item的总数量
     */
    private void setupArrays(int count) {
        sectionForPosition = new int[count];
        positionWithinSection = new int[count];
        isHeader = new boolean[count];
    }

    /**
     * 通过计算每个item的位置信息，将上一步初始化后的数组填充数据，
     * 最终这几个数组保存了每个位置的item的状态信息，即：是否是header，是否是footer，
     * 所在的position是多少，所在的section是多少
     */
    private void calculatePositions() {
        int sections = getSectionCount();
        int index = 0;

        for (int i = 0; i < sections; i++) {
            setupItems(index, true, i, 0);
            index++;

            for (int j = 0; j < getItemCountForSection(i); j++) {
                setupItems(index, false, i, j);
                index++;
            }
        }
    }

    /**
     * 保存每个位置对应的数据信息
     *
     * @param index    从0开始的每个最小单位所在的位置，从0开始，到count结束
     * @param isHeader 所在index位置的item是否是header
     * @param section  所在index位置的item对应的section
     * @param position 所在index位置的item对应的position
     */
    private void setupItems(int index, boolean isHeader, int section, int
            position) {
        this.isHeader[index] = isHeader;
        sectionForPosition[index] = section;
        positionWithinSection[index] = position;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader()) {//如果整个列表有头布局
            if (position == 0) {
                return getHeaderViewType();
            } else {
                if (isSectionHeaderPosition(position - 1)) {
                    return getSectionHeaderViewType();
                } else {
                    return getSectionItemViewType();
                }
            }
        } else {
            if (isSectionHeaderPosition(position)) {
                return getSectionHeaderViewType();
            } else {
                return getSectionItemViewType();
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (isSectionHeaderViewType(viewType)) {
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType);
        } else if (isHeaderViewType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (hasHeader()) {
            if (position == 0) {
                onBindHeaderViewHolder(holder);
            } else {
                int section = sectionForPosition[position - 1];
                int index = positionWithinSection[position - 1];

                if (isSectionHeaderPosition(position - 1)) {//当前位置是分组header
                    onBindSectionHeaderViewHolder(holder, section);

                } else {//当前位置是组内item
                    onBindItemViewHolder(holder, section, index);
                }
            }
        } else {
            int section = sectionForPosition[position];
            int index = positionWithinSection[position];

            if (isSectionHeaderPosition(position)) {
                onBindSectionHeaderViewHolder(holder, section);
            } else {
                onBindItemViewHolder(holder, section, index);
            }
        }
    }

    /**
     * 返回item总数（包含顶部Header、底部Footer、分组hader和分组footer以及分组item内容）
     */
    @Override
    public int getItemCount() {
        if (hasHeader()) {
            return count + 1;
        } else {
            return count;
        }
    }

    /**
     * 对应位置是否是一个分组header
     */
    public boolean isSectionHeaderPosition(int position) {
        if (isHeader == null) {
            setupPosition();
        }
        return isHeader[position];
    }

    private int getHeaderViewType() {
        return TYPE_HEADER;
    }

    private int getSectionHeaderViewType() {
        return TYPE_SECTION_HEADER;
    }

    private int getSectionItemViewType() {
        return TYPE_ITEM;
    }

    /**
     * 是否是分组header
     *
     * @param viewType
     * @return
     */
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == TYPE_SECTION_HEADER;
    }

    /**
     * 是否是列表的Header
     *
     * @param viewType
     * @return
     */
    protected boolean isHeaderViewType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * 整个列表是否有Header
     */
    protected abstract boolean hasHeader();

    /**
     * @return 返回分组的数量
     */
    protected abstract int getSectionCount();

    /**
     * @param section 分组的position
     * @return 返回当前分组的item数量
     */
    protected abstract int getItemCountForSection(int section);

    /**
     *  创建列表头布局
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建分组头布局
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建分组item
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定列表头布局的数据
     * @param holder
     */
    protected abstract void onBindHeaderViewHolder(ViewHolder holder);

    /**
     * 绑定分组头布局数据
     * @param holder
     * @param section
     */
    protected abstract void onBindSectionHeaderViewHolder(ViewHolder holder, int section);

    /**
     * 绑定分组item数据
     * @param holder
     * @param section
     * @param position
     */
    protected abstract void onBindItemViewHolder(ViewHolder holder, int section, int position);
}
