package com.dqhieuse.sportbookingbackend.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaResponse {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private long totalPages;
}
