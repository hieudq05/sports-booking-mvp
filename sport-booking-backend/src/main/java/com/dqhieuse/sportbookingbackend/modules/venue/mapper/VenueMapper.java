package com.dqhieuse.sportbookingbackend.modules.venue.mapper;

import com.dqhieuse.sportbookingbackend.modules.venue.dto.*;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Court;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "courts", ignore = true),
            @Mapping(target = "images", ignore = true)
    })
    Venue toEntity(CreateVenueRequest createVenueRequest);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "courts", ignore = true),
            @Mapping(target = "images", ignore = true)
    })
    void updateFromRequest(UpdateVenueRequest updateVenueRequest, @MappingTarget Venue venue);

    VenueSummaryResponse toSummaryResponse(Venue venue);

    VenueDetailResponse toDetailResponse(Venue venue);

    List<VenueSummaryResponse> toSummaryResponseList(List<Venue> venues);

    OwnerResponse toOwnerResponse(Venue venue);

    CourtResponse toCourtResponse(Court court);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "venue", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Court toCourtEntity(CreateCourtRequest createCourtRequest);

    List<CourtResponse> toCourtResponseList(List<Court> courts);

    CourtDetailResponse toCourtDetailResponse(Court court);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "venue", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "active", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    void updateCourtFromRequest(UpdateCourtRequest request, @MappingTarget Court court);

    VenueImageResponse toImageResponse(VenueImage venueImage);
}
