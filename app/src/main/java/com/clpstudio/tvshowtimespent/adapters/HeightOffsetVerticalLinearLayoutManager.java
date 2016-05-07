package com.clpstudio.tvshowtimespent.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * A custom LinearLayoutManager for showing the recyclerview with a variable height based on the content of the list.
 *
 * @author Endava
 */
public class HeightOffsetVerticalLinearLayoutManager extends LinearLayoutManager {

    /**
     * The logging tag for this class.
     */
    private static final String TAG = HeightOffsetVerticalLinearLayoutManager.class.getName();
    /**
     * Measured dimensions of a child.
     */
    private int[] mMeasuredDimension = new int[2];

    /**
     * An offset value that is equal with the height of the shadow of the list. Without this offset the last item from the list will be
     * cutted.
     */
    private int mOffSet;

    /**
     * Constructor.
     *
     * @param context       The context.
     * @param reverseLayout When set to true, layouts from end to start.
     * @param offSet        An offset value that is equal with the height of the shadow of the list.
     */
    public HeightOffsetVerticalLinearLayoutManager(Context context, boolean reverseLayout, int offSet) {
        super(context, LinearLayoutManager.VERTICAL, reverseLayout);
        mOffSet = offSet;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getItemCount(); i++) {
            measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(widthSize, widthMode),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);

            height = height + mMeasuredDimension[1];
            if (i == 0) {
                width = mMeasuredDimension[0];
            }
        }

        if (height < heightSize) {
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    break;
            }
            setMeasuredDimension(width, height + mOffSet);
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }

    /**
     * Measure the child view.
     *
     * @param recycler          The RecyclerView which has setted this layout manager.
     * @param position          The position of the view.
     * @param widthSpec         Measure specifications for the width of the child view.
     * @param heightSpec        Measure specifications for the height of the child view.
     * @param measuredDimension An array that contains the measured dimensions.
     */
    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        try {
            View view = recycler.getViewForPosition(position);
            super.measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin + p.rightMargin;
            measuredDimension[1] = getDecoratedMeasuredHeight(view) + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}