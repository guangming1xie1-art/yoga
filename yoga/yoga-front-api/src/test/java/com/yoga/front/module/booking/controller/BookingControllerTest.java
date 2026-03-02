package com.yoga.front.module.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.front.module.booking.service.BookingService;
import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@DisplayName("用户端预约控制器测试")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingVO bookingVO;
    private BookingCreateRequest createRequest;
    private PageResponse<BookingVO> pageResponse;

    @BeforeEach
    void setUp() {
        bookingVO = new BookingVO();
        bookingVO.setId(1L);
        bookingVO.setSessionId(10L);
        bookingVO.setSessionName("瑜伽基础课");
        bookingVO.setSessionStartTime(LocalDateTime.of(2024, 6, 15, 10, 0));
        bookingVO.setSessionEndTime(LocalDateTime.of(2024, 6, 15, 11, 0));
        bookingVO.setCoachName("张教练");
        bookingVO.setVenueName("阳光瑜伽馆");
        bookingVO.setPrice(new BigDecimal("99.00"));
        bookingVO.setStatus("BOOKED");
        bookingVO.setQrCode("QR123456");
        bookingVO.setQrExpireAt(LocalDateTime.of(2024, 6, 15, 11, 30));
        bookingVO.setCreatedAt(LocalDateTime.now());

        createRequest = new BookingCreateRequest();
        createRequest.setSessionId(10L);

        pageResponse = PageResponse.of(List.of(bookingVO), 1, 1, 10);
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建预约成功")
    void createBooking_Success() throws Exception {
        when(bookingService.create(eq(1L), any(BookingCreateRequest.class))).thenReturn(bookingVO);

        mockMvc.perform(post("/api/front/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.sessionId").value(10))
                .andExpect(jsonPath("$.data.sessionName").value("瑜伽基础课"))
                .andExpect(jsonPath("$.data.coachName").value("张教练"))
                .andExpect(jsonPath("$.data.status").value("BOOKED"));

        verify(bookingService).create(eq(1L), any(BookingCreateRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建预约失败-课程排期ID为空")
    void createBooking_Failure_EmptySessionId() throws Exception {
        BookingCreateRequest invalidRequest = new BookingCreateRequest();

        mockMvc.perform(post("/api/front/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));

        verify(bookingService, never()).create(anyLong(), any(BookingCreateRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建预约失败-课程已满")
    void createBooking_Failure_SessionFull() throws Exception {
        when(bookingService.create(eq(1L), any(BookingCreateRequest.class)))
                .thenThrow(new BusinessException(ResultCode.SESSION_FULL));

        mockMvc.perform(post("/api/front/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.SESSION_FULL.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.SESSION_FULL.getMessage()));

        verify(bookingService).create(eq(1L), any(BookingCreateRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建预约失败-已预约该课程")
    void createBooking_Failure_AlreadyBooked() throws Exception {
        when(bookingService.create(eq(1L), any(BookingCreateRequest.class)))
                .thenThrow(new BusinessException(ResultCode.BOOKING_ALREADY_EXISTS));

        mockMvc.perform(post("/api/front/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.BOOKING_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.BOOKING_ALREADY_EXISTS.getMessage()));

        verify(bookingService).create(eq(1L), any(BookingCreateRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("取消预约成功")
    void cancelBooking_Success() throws Exception {
        doNothing().when(bookingService).cancel(eq(1L), eq(1L));

        mockMvc.perform(post("/api/front/booking/1/cancel")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(bookingService).cancel(1L, 1L);
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("取消预约失败-预约不存在")
    void cancelBooking_Failure_NotFound() throws Exception {
        doThrow(new BusinessException(ResultCode.BOOKING_NOT_FOUND))
                .when(bookingService).cancel(eq(1L), eq(999L));

        mockMvc.perform(post("/api/front/booking/999/cancel")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.BOOKING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.BOOKING_NOT_FOUND.getMessage()));

        verify(bookingService).cancel(1L, 999L);
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("取消预约失败-无法取消")
    void cancelBooking_Failure_CannotCancel() throws Exception {
        doThrow(new BusinessException(ResultCode.BOOKING_CANNOT_CANCEL))
                .when(bookingService).cancel(eq(1L), eq(1L));

        mockMvc.perform(post("/api/front/booking/1/cancel")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.BOOKING_CANNOT_CANCEL.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.BOOKING_CANNOT_CANCEL.getMessage()));

        verify(bookingService).cancel(1L, 1L);
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("获取我的预约列表成功")
    void myBookings_Success() throws Exception {
        when(bookingService.myBookings(eq(1L), any(PageRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/front/booking/my")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list[0].id").value(1))
                .andExpect(jsonPath("$.data.list[0].sessionName").value("瑜伽基础课"))
                .andExpect(jsonPath("$.data.total").value(1));

        verify(bookingService).myBookings(eq(1L), any(PageRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("获取我的预约列表-使用默认分页")
    void myBookings_WithDefaultPageParams() throws Exception {
        when(bookingService.myBookings(eq(1L), any(PageRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/front/booking/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(bookingService).myBookings(eq(1L), any(PageRequest.class));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("获取预约详情成功")
    void getDetail_Success() throws Exception {
        when(bookingService.getDetail(1L)).thenReturn(bookingVO);

        mockMvc.perform(get("/api/front/booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.sessionName").value("瑜伽基础课"))
                .andExpect(jsonPath("$.data.coachName").value("张教练"))
                .andExpect(jsonPath("$.data.qrCode").value("QR123456"));

        verify(bookingService).getDetail(1L);
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("获取预约详情失败-预约不存在")
    void getDetail_Failure_NotFound() throws Exception {
        when(bookingService.getDetail(999L))
                .thenThrow(new BusinessException(ResultCode.BOOKING_NOT_FOUND));

        mockMvc.perform(get("/api/front/booking/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.BOOKING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.BOOKING_NOT_FOUND.getMessage()));

        verify(bookingService).getDetail(999L);
    }

    @Test
    @DisplayName("未授权访问预约列表")
    void myBookings_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/front/booking/my"))
                .andExpect(status().isUnauthorized());

        verify(bookingService, never()).myBookings(anyLong(), any(PageRequest.class));
    }

    @Test
    @DisplayName("未授权创建预约")
    void createBooking_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/front/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnauthorized());

        verify(bookingService, never()).create(anyLong(), any(BookingCreateRequest.class));
    }
}
