package com.yoga.front.module.session.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.entity.Coach;
import com.yoga.common.entity.Venue;
import com.yoga.common.entity.Review;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.session.service.SessionService;
import com.yoga.front.module.template.mapper.TemplateMapper;
import com.yoga.front.module.coach.mapper.CoachMapper;
import com.yoga.front.module.venue.mapper.VenueMapper;
import com.yoga.front.module.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final TemplateMapper templateMapper;
    private final CoachMapper coachMapper;
    private final VenueMapper venueMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public PageResponse<SessionVO> listSessions(Long venueId, String category,
                                                 String date, PageRequest pageRequest) {
        try {
            log.info("查询课程列表，venueId: {}, category: {}, date: {}", venueId, category, date);

            Page<CourseSession> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

            LambdaQueryWrapper<CourseSession> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseSession::getStatus, "SCHEDULED")
                   .ge(CourseSession::getStartTime, LocalDateTime.now())
                   .orderByAsc(CourseSession::getStartTime);

            if (venueId != null) {
                wrapper.eq(CourseSession::getVenueId, venueId);
            }

            Page<CourseSession> result = sessionMapper.selectPage(page, wrapper);

            List<SessionVO> voList = result.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            return PageResponse.of(voList, result.getTotal(), pageRequest.getPage(), pageRequest.getSize());

        } catch (Exception e) {
            log.error("查询课程列表失败", e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    @Cacheable(value = "session", key = "#id", unless = "#result == null")
    public SessionVO getDetail(Long id) {
        try {
            log.info("查询课程详情，课程ID: {}", id);

            CourseSession session = sessionMapper.selectById(id);
            if (session == null) {
                throw new BusinessException(ResultCode.SESSION_NOT_FOUND);
            }

            return convertToVO(session);

        } catch (BusinessException e) {
            log.warn("查询课程详情失败，业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("查询课程详情失败，课程ID: {}", id, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public PageResponse<SessionVO> search(String keyword, PageRequest pageRequest) {
        try {
            log.info("搜索课程，关键词: {}", keyword);

            Page<SessionVO> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
            IPage<SessionVO> resultPage = sessionMapper.searchSessions(page, keyword);
            
            return PageResponse.of(resultPage.getRecords(), resultPage.getTotal(), 
                    pageRequest.getPage(), pageRequest.getSize());

        } catch (Exception e) {
            log.error("搜索课程失败，关键词: {}", keyword, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public PageResponse<?> listReviews(Long sessionId, PageRequest pageRequest) {
        try {
            log.info("查询课程评价，课程ID: {}", sessionId);

            Page<Review> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

            LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Review::getSessionId, sessionId)
                   .eq(Review::getIsVisible, 1)
                   .orderByDesc(Review::getCreatedAt);

            Page<Review> result = reviewMapper.selectPage(page, wrapper);

            return PageResponse.of(result.getRecords(), result.getTotal(), 
                    pageRequest.getPage(), pageRequest.getSize());

        } catch (Exception e) {
            log.error("查询课程评价失败，课程ID: {}", sessionId, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    private SessionVO convertToVO(CourseSession session) {
        SessionVO vo = new SessionVO();
        vo.setId(session.getId());
        vo.setStartTime(session.getStartTime());
        vo.setEndTime(session.getEndTime());
        vo.setCapacity(session.getCapacity());
        vo.setBookedCount(session.getBookedCount());
        vo.setPrice(session.getPrice());
        vo.setStatus(session.getStatus());

        CourseTemplate template = templateMapper.selectById(session.getTemplateId());
        if (template != null) {
            vo.setName(template.getName());
            vo.setCategory(template.getCategory());
            vo.setDescription(template.getDescription());
            vo.setCoverImage(template.getCoverImage());
            vo.setDurationMinutes(template.getDurationMinutes());
            vo.setDifficulty(template.getDifficulty());
        }

        Coach coach = coachMapper.selectById(session.getCoachId());
        if (coach != null) {
            vo.setCoachName(coach.getRealName());
            vo.setCoachAvatar(coach.getAvatar());
        }

        Venue venue = venueMapper.selectById(session.getVenueId());
        if (venue != null) {
            vo.setVenueName(venue.getName());
            vo.setVenueAddress(venue.getAddress());
        }

        return vo;
    }
}
