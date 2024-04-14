package com.luzhu.truck.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class BaseParam {
    private Integer page;
    private Integer size;

    public Pageable getPageable() {
        if (null == page) {
            page = 1;
        }
        if (null == size) {
            size = 10;
        }
        return PageRequest.of(page - 1, size);
    }
}
