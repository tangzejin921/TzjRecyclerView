package com.suning.wl.sh.view.recycler.layoutmanager;

public class Span {
    private int count;//总个数
    private int index;//第几个
    private int y;//行
    private int x;//列
    private int xCount;//一行分几份
    private int xSpan;//占一行中的几个
    private Boolean lastY;//最后一行吗？

    /**
     * @param count
     * @param index
     * @param y
     * @param x
     * @param xCount
     * @param xSpan
     * @param lastY  为null 时 可计算 xSpan=1 或 跨度一致，不然会计算不准确
     */
    public Span(int count, int index, int y, int x, int xCount, int xSpan, Boolean lastY) {
        this.count = count;
        this.index = index;
        this.y = y;
        this.x = x;
        this.xCount = xCount;
        this.xSpan = xSpan;
        this.lastY = lastY;
    }

    public boolean isFirstY() {
        return y == 0;
    }

    public boolean isFirstX() {
        return x == 0;
    }

    public boolean isLastX() {
        return xCount <= x + xSpan;
    }

    public boolean isLastY() {
        if (lastY == null) {
            return xCount - x - xSpan >= count - index - 1;//span 为1 的情况/没有跨度的问题
        } else {
            return lastY;
        }
    }

}
