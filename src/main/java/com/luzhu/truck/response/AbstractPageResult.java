package com.luzhu.truck.response;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPageResult<T> {

    private long total;
    private List<T> pageList;

    protected AbstractPageResult(long total, List<T> list) {
        this.total = total;
        this.pageList = list;
    }

    protected AbstractPageResult() {
        this.total = 0L;
        this.pageList = Collections.emptyList();
    }

    public long getTotal() {
        return this.total;
    }

    public List<T> getPageList() {
        return this.pageList;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public void setPageList(final List<T> pageList) {
        this.pageList = pageList;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AbstractPageResult)) {
            return false;
        } else {
            AbstractPageResult<?> other = (AbstractPageResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getTotal() != other.getTotal()) {
                return false;
            } else {
                Object this$pageList = this.getPageList();
                Object other$pageList = other.getPageList();
                if (this$pageList == null) {
                    if (other$pageList != null) {
                        return false;
                    }
                } else if (!this$pageList.equals(other$pageList)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AbstractPageResult;
    }

//    public int hashCode() {
//        int PRIME = true;
//        int result = 1;
//        long $total = this.getTotal();
//        result = result * 59 + (int)($total >>> 32 ^ $total);
//        Object $pageList = this.getPageList();
//        result = result * 59 + ($pageList == null ? 43 : $pageList.hashCode());
//        return result;
//    }

    public String toString() {
        long var10000 = this.getTotal();
        return "AbstractPageResult(total=" + var10000 + ", pageList=" + this.getPageList() + ")";
    }
}
