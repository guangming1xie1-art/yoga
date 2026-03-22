package com.yoga.front.module.session.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.entity.Coach;
import com.yoga.common.entity.Venue;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.template.mapper.TemplateMapper;
import com.yoga.front.module.coach.mapper.CoachMapper;
import com.yoga.front.module.venue.mapper.VenueMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private TemplateMapper templateMapper;

    @Mock
    private CoachMapper coachMapper;

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private CourseSession session;
    private CourseTemplate template;
    private Coach coach;
    private Venue venue;

    @BeforeEach
    void setUp() {
        template = new CourseTemplate();
        template.setId(1L);
        template.setName("瑜伽课程");
        template.setDurationMinutes(60);
        template.setCapacity(10);

        coach = new Coach();
        coach.setId(1L);
        coach.setRealName("张教练");

        venue = new Venue();
        venue.setId(1L);
        venue.setName("瑜伽馆");

        session = new CourseSession();
        session.setId(1L);
        session.setTemplateId(1L);
        session.setCoachId(1L);
        session.setVenueId(1L);
        session.setStartTime(LocalDateTime.now().plusHours(2));
        session.setEndTime(LocalDateTime.now().plusHours(3));
        session.setCapacity(10);
        session.setBookedCount(5);
        session.setPrice(100.0);
        session.setStatus("SCHEDULED");
    }

    @Test
    void testGetDetail_Success() {
        when(sessionMapper.selectById(1L)).thenReturn(session);
        when(templateMapper.selectById(1L)).thenReturn(template);
        when(coachMapper.selectById(1L)).thenReturn(coach);
        when(venueMapper.selectById(1L)).thenReturn(venue);

        SessionVO result = sessionService.getDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sessionMapper, times(1)).selectById(1L);
    }

    @Test
    void testGetDetail_NotFound() {
        when(sessionMapper.selectById(1L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> sessionService.getDetail(1L));
    }

    @Test
    void testListSessions_Success() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);

        Page<CourseSession> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(session));
        page.setTotal(1);

        when(sessionMapper.selectPage(any(), any())).thenReturn(page);

        PageResponse<SessionVO> result = sessionService.listSessions(null, null, null, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(sessionMapper, times(1)).selectPage(any(), any());
    }
}
