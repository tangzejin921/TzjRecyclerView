package androidx.recyclerview.widget;

import android.graphics.Point;

import com.tzj.view.recyclerview.layoutmanager.Span;


/**
 *
 */
public class TzjSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    @Override
    public int getSpanSize(int position) {
        return 1;
    }

    public Span getSpan(int count, int index, int spanCount) {
        int spanGroupIndex = 0;//第几行
        int spanIndex = 0;//第几个
//        spanGroupIndex = getSpanGroupIndex(index, spanCount);//第几行
//        spanIndex = getSpanIndex(index, spanCount);//第几个
        Point point = getPoint(count, index, spanCount);
        spanGroupIndex = Math.abs(point.y);
        spanIndex = point.x;

        int spanSize = getSpanSize(index);//占几个
        return new Span(count, index, spanGroupIndex, spanIndex, spanCount, spanSize, point.y > 0);
    }

    /**
     * 将 getSpanGroupIndex 与 getSpanIndex 合并的结果
     * point y 为负数时表示不是最后一行
     */
    public Point getPoint(int count, int position, int spanCount) {
        int indexTemp = 0;//span
        int group = 0;//第几行
        int index = 0;//第几个
        int startPos = 0;
        int positionSpanSize = getSpanSize(position);
        //第几个
        if (positionSpanSize == spanCount) {
            index = 0; // quick return for full-span items
            startPos = position;
        } else {
            // If caching is enabled, try to jump
            if (isSpanIndexCacheEnabled() && mSpanIndexCache.size() > 0) {
                int prevKey = findFirstKeyLessThan(mSpanIndexCache,position);
                if (prevKey >= 0) {
                    index = mSpanIndexCache.get(prevKey) + getSpanSize(prevKey);
                    startPos = prevKey + 1;
                }
            }
        }
        int i = 0;
        for (; i < startPos; i++) {//第几行
            int size = getSpanSize(i);
            indexTemp += size;
            if (indexTemp == spanCount) {
                indexTemp = 0;
                group++;
            } else if (indexTemp > spanCount) {
                // did not fit, moving to next row / column
                indexTemp = size;
                group++;
            }
        }
        for (i = startPos; i < position; i++) {//第几个+第几行
            int size = getSpanSize(i);
            //第几行
            indexTemp += size;
            if (indexTemp == spanCount) {
                indexTemp = 0;
                group++;
            } else if (indexTemp > spanCount) {
                // did not fit, moving to next row / column
                indexTemp = size;
                group++;
            }
            //第几个 这里可以优化
            index += size;
            if (index == spanCount) {
                index = 0;
            } else if (index > spanCount) {
                // did not fit, moving to next row / column
                index = size;
            }
        }
        if (indexTemp + positionSpanSize > spanCount) {
            group++;
        }
        if (index + positionSpanSize > spanCount) {
            index = 0;
        }

        //计算是不是最后一行
        if (position == count - 1) {//下一个就是最后一个
            return new Point(index, group);
        }
        for (i = position; i < count; i++) {
            int size = getSpanSize(i);
            indexTemp += size;
            if (indexTemp == spanCount) {
                indexTemp = 0;
                return new Point(index, -group);
            } else if (indexTemp > spanCount) {
                // did not fit, moving to next row / column
                indexTemp = size;
                return new Point(index, -group);
            }
        }
        if (indexTemp + positionSpanSize > spanCount) {
            return new Point(index, -group);
        } else {
            return new Point(index, group);
        }
    }
}
