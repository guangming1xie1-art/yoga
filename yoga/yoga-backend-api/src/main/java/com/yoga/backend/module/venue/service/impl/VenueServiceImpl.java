package com.yoga.backend.module.venue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.dto.venue.VenueDTO;
import com.yoga.common.entity.Venue;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.venue.mapper.VenueMapper;
import com.yoga.backend.module.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueMapper venueMapper;

    @Override
    public PageResponse<Venue> listVenues(PageRequest pageRequest) {
        Page<Venue> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<Venue> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Venue::getCreatedAt);
        Page<Venue> result = venueMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public Venue getDetail(Long id) {
        Venue venue = venueMapper.selectById(id);
        if (venue == null) {
            throw new BusinessException(ResultCode.VENUE_NOT_FOUND);
        }
        return venue;
    }

    @Override
    public Venue create(VenueDTO dto) {
        Venue venue = new Venue();
        venue.setName(dto.getName());
        venue.setCity(dto.getCity());
        venue.setAddress(dto.getAddress());
        venue.setPhone(dto.getPhone());
        venue.setBusinessHours(dto.getBusinessHours());
        venue.setCoverImage(dto.getCoverImage());
        venue.setDescription(dto.getDescription());
        venue.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        venueMapper.insert(venue);
        return venue;
    }

    @Override
    public Venue update(Long id, VenueDTO dto) {
        Venue venue = getDetail(id);
        venue.setName(dto.getName());
        venue.setCity(dto.getCity());
        venue.setAddress(dto.getAddress());
        venue.setPhone(dto.getPhone());
        venue.setBusinessHours(dto.getBusinessHours());
        venue.setCoverImage(dto.getCoverImage());
        venue.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            venue.setStatus(dto.getStatus());
        }
        venueMapper.updateById(venue);
        return venue;
    }

    @Override
    public void delete(Long id) {
        venueMapper.deleteById(id);
    }
}
