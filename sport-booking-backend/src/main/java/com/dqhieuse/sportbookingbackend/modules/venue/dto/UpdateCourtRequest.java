package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import com.dqhieuse.sportbookingbackend.modules.venue.entity.CourtType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record UpdateCourtRequest(
        @NotBlank(message = "Court name is required.")
        String name,

        @Length(max = 1000, message = "Description must be less than 1000 characters.")
        String description,

        @NotNull(message = "Court type is required.")
        CourtType type,

        @NotNull(message = "Price per hour is required.")
        @Min(value = 0, message = "Price must be non-negative.")
        BigDecimal pricePerHour,

        @NotNull(message = "Capacity is required.")
        @Min(value = 1, message = "Capacity must be at least 1.")
        Integer capacity,

        @NotBlank(message = "Thumbnail URL is required.")
        String thumbnail,

        String images
) {
}
