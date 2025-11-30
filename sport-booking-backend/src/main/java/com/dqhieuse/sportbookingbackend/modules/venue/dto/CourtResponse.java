package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import java.math.BigDecimal;

public record CourtResponse(
        Long id,
        String name,
        String type,
        BigDecimal pricePerHour,
        Integer capacity,
        String thumbnail,
        String status
) {
}
