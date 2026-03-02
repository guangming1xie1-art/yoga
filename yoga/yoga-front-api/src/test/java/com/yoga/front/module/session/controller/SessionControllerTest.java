package com.yoga.front.module.session.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.front.module.session.service.SessionService;
import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("用户端课程排期控制器测试")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    private SessionVO sessionVO;
    private PageResponse<SessionVO> pageResponse;

    @BeforeEach
    void setUp() {
        sessionVO = new SessionVO();
        sessionVO.setId(1L);
        sessionVO.setName("瑜伽基础课");
        sessionVO.setCategory("基础");
        sessionVO.setDescription("适合初学者的瑜伽课程");
        sessionVO.setCoverImage("https://example.com/yoga.jpg");
        sessionVO.setDurationMinutes(60);
        sessionVO.setCapacity(20);
        sessionVO.setBookedCount(10);
        sessionVO.setRemainingSeats(10);
        sessionVO.setPrice(new BigDecimal("99.00"));
        sessionVO.setDifficulty("BEGINNER");
        sessionVO.setStartTime(LocalDateTime.of(2024, 6, 15, 10, 0));
        sessionVO.setEndTime(LocalDateTime.of(2024, 6, 15, 11, 0));
        sessionVO.setStatus("ACTIVE");
        sessionVO.setCoachId(1L);
        sessionVO.setCoachName("张教练");
        sessionVO.setCoachAvatar("https://example.com/coach.jpg");
        sessionVO.setVenueId(1L);
        sessionVO.setVenueName("阳光瑜伽馆");
        sessionVO.setVenueAddress("朝阳区阳光路1号");

        pageResponse = PageResponse.of(List.of(sessionVO), 1, 1, 10);
    }

    @Test
    @DisplayName("获取课程列表成功")
    void listSessions_Success() throws Exception {
        when(sessionService.listSessions(any(), any(), any(), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/front/session/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list[0].id").value(1))
                .andExpect(jsonPath("$.data.list[0].name").value("瑜伽基础课"))
                .andExpect(jsonPath("$.data.list[0].coachName").value("张教练"))
                .andExpect(jsonPath("$.data.list[0].venueName").value("阳光瑜伽馆"));

        verify(sessionService).listSessions(null, null, null, new PageRequest() {{ setPage(1); setSize(10); }});
    }

    @Test
    @DisplayName("获取课程列表-带筛选条件")
    void listSessions_WithFilters() throws Exception {
        when(sessionService.listSessions(any(), any(), any(), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/front/session/list")
                        .param("venueId", "1")
                        .param("category", "基础")
                        .param("date", "2024-06-15")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(sessionService).listSessions(1L, "基础", "2024-06-15", new PageRequest() {{ setPage(1); setSize(10); }});
    }

    @Test
    @DisplayName("获取课程详情成功")
    void getDetail_Success() throws Exception {
        when(sessionService.getDetail(1L)).thenReturn(sessionVO);

        mockMvc.perform(get("/api/front/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("瑜伽基础课"))
                .andExpect(jsonPath("$.data.category").value("基础"))
                .andExpect(jsonPath("$.data.description").value("适合初学者的瑜伽课程"))
                .andExpect(jsonPath("$.data.capacity").value(20))
                .andExpect(jsonPath("$.data.bookedCount").value(10))
                .andExpect(jsonPath("$.data.remainingSeats").value(10))
                .andExpect(jsonPath("$.data.price").value(99.00));

        verify(sessionService).getDetail(1L);
    }

    @Test
    @DisplayName("获取课程详情失败-课程不存在")
    void getDetail_Failure_NotFound() throws Exception {
        when(sessionService.getDetail(999L))
                .thenThrow(new BusinessException(ResultCode.SESSION_NOT_FOUND));

        mockMvc.perform(get("/api/front/session/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.SESSION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.SESSION_NOT_FOUND.getMessage()));

        verify(sessionService).getDetail(999L);
    }

    @Test
    @DisplayName("搜索课程成功")
    void search_Success() throws Exception {
        when(sessionService.search(anyString(), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/front/session/search")
                        .param("keyword", "瑜伽")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list[0].name").value("瑜伽基础课"));

        verify(sessionService).search("瑜伽", new PageRequest() {{ setPage(1); setSize(10); }});
    }

    @Test
    @DisplayName("搜索课程失败-关键字为空")
    void search_Failure_EmptyKeyword() throws Exception {
        mockMvc.perform(get("/api/front/session/search")
                        .param("keyword", "")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(sessionService).search(eq(""), any(PageRequest.class));
    }

    @Test
    @DisplayName("获取课程评价列表成功")
    void listReviews_Success() throws Exception {
        PageResponse<Map<String, Object>> reviewPage = PageResponse.of(
                List.of(Map.of(
                        "id", 1,
                        "userName", "用户A",
                        "rating", 5,
                        "content", "非常好的课程！"
                )),
                1, 1, 10
        );

        when(sessionService.listReviews(eq(1L), any(PageRequest.class)))
                .thenReturn(reviewPage);

        mockMvc.perform(get("/api/front/session/1/reviews")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list[0].userName").value("用户A"))
                .andExpect(jsonPath("$.data.list[0].rating").value(5));

        verify(sessionService).listReviews(1L, new PageRequest() {{ setPage(1); setSize(10); }});
    }

    @Test
    @DisplayName("获取课程评价列表-空结果")
    void listReviews_EmptyResult() throws Exception {
        PageResponse<Map<String, Object>> emptyPage = PageResponse.of(List.of(), 0, 1, 10);

        when(sessionService.listReviews(eq(1L), any(PageRequest.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/front/session/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list").isEmpty())
                .andExpect(jsonPath("$.data.total").value(0));

        verify(sessionService).listReviews(eq(1L), any(PageRequest.class));
    }
}
