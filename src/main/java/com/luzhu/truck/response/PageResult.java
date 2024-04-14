package com.luzhu.truck.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> extends AbstractPageResult<T> {
    public PageResult() {
    }

    public PageResult(long total, List<T> list) {
        super(total, list);
    }

    public PageResult(Page<T> jpaPageResult) {
        super(jpaPageResult.getTotalElements(), jpaPageResult.getContent());
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResult)) {
            return false;
        } else {
            PageResult<?> other = (PageResult)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageResult;
    }

//    public int hashCode() {
//        int result = true;
//        return 1;
//    }

    public String toString() {
        return "PageResult()";
    }
}
