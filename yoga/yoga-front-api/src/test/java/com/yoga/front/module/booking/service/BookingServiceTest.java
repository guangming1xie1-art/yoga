package com.yoga.front.module.booking.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.entity.Booking;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.entity.Order;
import com.yoga.common.entity.User;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.front.module.booking.mapper.BookingMapper;
import com.yoga.front.module.booking.mapper.OrderMapper;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.user.mapper.UserMapper;
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
class BookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Long userId;
    private BookingCreateRequest request;
    private CourseSession session;
    private User user;

    @BeforeEach
    void setUp() {
        userId = 1L;
        request = new BookingCreateRequest();
        request.setSessionId(1L);

        session = new CourseSession();
        session.setId(1L);
        session.setStatus("SCHEDULED");
        session.setBookedCount(5);
        session.setCapacity(10);
        session.setStartTime(LocalDateTime.now().plusHours(2));
        session.setPrice(100.0);

        user = new User();
        user.setId(userId);
        user.setOpenid("test_openid");
    }

    @Test
    void testCreate_Success() {
        when(sessionMapper.selectById(1L)).thenReturn(session);
        when(bookingMapper.selectOne(any())).thenReturn(null);
        when(bookingMapper.insert(any())).thenReturn(1);
        when(orderMapper.insert(any())).thenReturn(1);
        when(userMapper.selectById(userId)).thenReturn(user);

        BookingVO result = bookingService.create(userId, request);

        assertNotNull(result);
        verify(sessionMapper, times(1)).updateById(any());
        verify(bookingMapper, times(2)).insert(any());
        verify(orderMapper, times(1)).insert(any());
    }

    @Test
    void testCreate_SessionNotFound() {
        when(sessionMapper.selectById(1L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> bookingService.create(userId, request));
    }

    @Test
    void testCreate_SessionCancelled() {
        session.setStatus("CANCELLED");
        when(sessionMapper.selectById(1L)).thenReturn(session);

        assertThrows(BusinessException.class, () -> bookingService.create(userId, request));
    }

    @Test
    void testCreate_SessionFull() {
        session.setBookedCount(10);
        when(sessionMapper.selectById(1L)).thenReturn(session);

        assertThrows(BusinessException.class, () -> bookingService.create(userId, request));
    }

    @Test
    void testCancel_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(userId);
        booking.setSessionId(1L);
        booking.setStatus("CONFIRMED");

        Order order = new Order();
        order.setId(1L);
        order.setStatus("PAID");
        order.setAmount(100.0);

        when(bookingMapper.selectById(1L)).thenReturn(booking);
        when(sessionMapper.selectById(1L)).thenReturn(session);
        when(orderMapper.selectById(1L)).thenReturn(order);

        bookingService.cancel(userId, 1L);

        verify(bookingMapper, times(1)).updateById(any());
        verify(sessionMapper, times(1)).updateById(any());
        verify(orderMapper, times(1)).updateById(any());
    }

    @Test
    void testMyBookings_Success() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setUserId(userId);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setUserId(userId);

        List<Booking> bookings = Arrays.asList(booking1, booking2);
        Page<Booking> page = new Page<>(1, 10);
        page.setRecords(bookings);
        page.setTotal(2);

        when(bookingMapper.selectPage(any(), any())).thenReturn(page);

        PageResponse<BookingVO> result = bookingService.myBookings(userId, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotal());
    }
}
