package com.yoga.backend.module.venue.service;

import com.yoga.common.dto.venue.VenueDTO;
import com.yoga.common.entity.Venue;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface VenueService {
    PageResponse<Venue> listVenues(PageRequest pageRequest);
    Venue getDetail(Long id);
    Venue create(VenueDTO dto);
    Venue update(Long id, VenueDTO dto);
    void delete(Long id);
}
