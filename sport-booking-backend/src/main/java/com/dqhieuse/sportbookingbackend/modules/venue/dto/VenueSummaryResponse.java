package com.dqhieuse.sportbookingbackend.modules.venue.dto;

public record VenueSummaryResponse(
        Long id,
        String name,
        String thumbnail,
        String address,
        String district,
        String status
) {
}
