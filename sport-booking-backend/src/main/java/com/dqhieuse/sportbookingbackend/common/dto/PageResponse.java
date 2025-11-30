package com.dqhieuse.sportbookingbackend.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private MetaResponse meta;
}
