package com.dqhieuse.sportbookingbackend.modules.venue.service;

import com.dqhieuse.sportbookingbackend.common.dto.MetaResponse;
import com.dqhieuse.sportbookingbackend.common.dto.PageResponse;
import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.Role;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.fileupload.service.FileUploadService;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.*;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueImage;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueStatus;
import com.dqhieuse.sportbookingbackend.modules.venue.mapper.VenueMapper;
import com.dqhieuse.sportbookingbackend.modules.venue.repository.VenueImageRepository;
import com.dqhieuse.sportbookingbackend.modules.venue.repository.VenueRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueMapper venueMapper;
    private final VenueRepository venueRepository;
    private final VenueImageRepository venueImageRepository;
    private final FileUploadService fileUploadService;
    private final EntityManager entityManager;

    @Transactional
    public VenueDetailResponse createVenue(CreateVenueRequest request, User currentUser) {
        if (currentUser.getRole() != Role.VENDOR) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to create venue");
        }

        Venue venue = venueMapper.toEntity(request);

        venue.setOwner(currentUser);
        venue.setStatus(VenueStatus.PENDING);

        if (request.images() != null) {
            request.images().forEach(image -> {
                VenueImage venueImage = VenueImage.builder()
                        .imageUrl(image)
                        .user(currentUser)
                        .build();

                venue.addImage(venueImage);
            });
        }

        Venue savedVenue = venueRepository.save(venue);

        return venueMapper.toDetailResponse(savedVenue);
    }

    public VenueDetailResponse addImageToVenue(Long venueId, AddVenueImagesRequest request, User currentUser) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + venueId)
        );

        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                venue.getOwner().getId().equals(currentUser.getId());

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to add image to this venue");
        }

        request.imageUrls().forEach(imageUrl -> {
            VenueImage venueImage = VenueImage.builder()
                    .imageUrl(imageUrl)
                    .user(currentUser)
                    .build();

            venue.addImage(venueImage);
        });

        Venue updatedVenue = venueRepository.save(venue);

        return venueMapper.toDetailResponse(updatedVenue);
    }

    public PageResponse<VenueSummaryResponse> findAllActiveVenues(Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedVenueFilter").setParameter("isDeleted", false);

        Page<Venue> activeVenues = venueRepository.findAllByStatus(VenueStatus.ACTIVE, pageable);

        session.disableFilter("deletedVenueFilter");

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(activeVenues.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(activeVenues.getTotalElements())
                .totalPages(activeVenues.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .content(venueSummaries)
                .meta(meta)
                .build();
    }

    public PageResponse<VenueSummaryResponse> findAllVenuesDeletedForAdmin(Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedVenueFilter").setParameter("isDeleted", true);

        Page<Venue> deletedVenues = venueRepository.findAll(pageable);

        session.disableFilter("deletedVenueFilter");

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(deletedVenues.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(deletedVenues.getTotalElements())
                .totalPages(deletedVenues.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .content(venueSummaries)
                .meta(meta)
                .build();
    }

    public PageResponse<VenueSummaryResponse> findAllVenuesByStatusForAdmin(VenueStatus status, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedVenueFilter").setParameter("isDeleted", false);

        Page<Venue> venuePage = venueRepository.findAllByStatus(status, pageable);

        session.disableFilter("deletedVenueFilter");

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(venuePage.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(venuePage.getTotalElements())
                .totalPages(venuePage.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .content(venueSummaries)
                .meta(meta)
                .build();
    }

    public PageResponse<VenueSummaryResponse> findAllVenuesForAdmin(Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedVenueFilter").setParameter("isDeleted", false);

        Page<Venue> venuePage = venueRepository.findAll(pageable);

        session.disableFilter("deletedVenueFilter");

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(venuePage.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(venuePage.getTotalElements())
                .totalPages(venuePage.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .content(venueSummaries)
                .meta(meta)
                .build();
    }

    public PageResponse<VenueSummaryResponse> findAllVenuesForVendor(Pageable pageable, User currentUser) {
        boolean isVendor = currentUser.getRole() == Role.VENDOR;

        if (!isVendor) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to view venues");
        }

        Page<Venue> venuePage = venueRepository.findAllByOwner(currentUser, pageable);

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(venuePage.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(venuePage.getTotalElements())
                .totalPages(venuePage.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .meta(meta)
                .content(venueSummaries)
                .build();
    }

    public VenueDetailResponse findVenueDetailById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedVenueFilter").setParameter("isDeleted", false);

        List<VenueStatus> allowedStatuses = List.of(VenueStatus.ACTIVE, VenueStatus.CLOSED);

        Venue venue = venueRepository.findByIdAndStatusIn(id, allowedStatuses).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        session.disableFilter("deletedVenueFilter");

        return venueMapper.toDetailResponse(venue);
    }

    @Transactional
    public void deleteVenueImage(Long imageId, User currentUser) {
        VenueImage venueImage = venueImageRepository.findById(imageId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue image not found with id: " + imageId)
        );

        Venue venue = venueImage.getVenue();
        User imageCreator = venueImage.getUser();

        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                venue.getOwner().getId().equals(currentUser.getId()) ||
                (imageCreator != null && imageCreator.getId().equals(currentUser.getId()));

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to delete this image");
        }

        fileUploadService.deleteFile(venueImage.getImageUrl());

        venueImageRepository.deleteById(imageId);
    }

    @Transactional
    public void softDeleteVenue(Long venueId, User currentUser) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + venueId)
        );

        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                venue.getOwner().getId().equals(currentUser.getId());

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to delete this venue");
        }

        venue.setDeleted(true);
        venueRepository.save(venue);
    }

    @Transactional
    public VenueDetailResponse updateVenue(Long venueId, UpdateVenueRequest request, User currentUser) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + venueId)
        );

        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                venue.getOwner().getId().equals(currentUser.getId());

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You do not have permission to update this venue");
        }

        venueMapper.updateFromRequest(request, venue);

        Venue updatedVenue = venueRepository.save(venue);

        return venueMapper.toDetailResponse(updatedVenue);
    }

    @Transactional
    public void restoreVenue(Long id) {
        Venue venue = venueRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        venue.setDeleted(false);
        venueRepository.save(venue);
    }

    @Transactional
    public void approveVenue(Long id) {
        Venue venue = venueRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        venue.setStatus(VenueStatus.ACTIVE);
        venueRepository.save(venue);
    }

    @Transactional
    public void rejectVenue(Long id) {
        Venue venue = venueRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        venue.setStatus(VenueStatus.REJECTED);
        venueRepository.save(venue);
    }

    @Transactional
    public void closeVenue(Long id, User currentUser) {
        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                currentUser.getRole() == Role.VENDOR;

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to close venue");
        }

        Venue venue = venueRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        if (venue.isDeleted()) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is deleted and cannot be closed");
        }

        if (venue.getStatus() == VenueStatus.CLOSED) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is already closed");
        }

        if (venue.getStatus() == VenueStatus.PENDING) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is pending approval and cannot be closed");
        }

        if (venue.getStatus() == VenueStatus.REJECTED) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is rejected and cannot be closed");
        }

        venue.setStatus(VenueStatus.CLOSED);

        venue.setStatus(VenueStatus.CLOSED);
        venueRepository.save(venue);
    }

    @Transactional
    public void openVenue(Long id, User currentUser) {
        boolean hasPermission =
                currentUser.getRole() == Role.ADMIN ||
                currentUser.getRole() == Role.VENDOR;

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to open venue");
        }

        Venue venue = venueRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Venue not found with id: " + id)
        );

        if (venue.isDeleted()) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is deleted and cannot be opened");
        }

        if (venue.getStatus() == VenueStatus.ACTIVE) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is already active");
        }

        if (venue.getStatus() == VenueStatus.PENDING) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is pending approval, contact admin to approve. You can open venue after approval.");
        }

        if (venue.getStatus() == VenueStatus.REJECTED) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Venue is rejected, contact admin to process. You can open venue after processing.");
        }

        venue.setStatus(VenueStatus.ACTIVE);
        venueRepository.save(venue);
    }
}
