package com.yoga.front.module.booking.service;

import com.yoga.front.module.booking.service.impl.BookingServiceImpl;
import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("用户端预约服务测试")
class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingCreateRequest createRequest;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        createRequest = new BookingCreateRequest();
        createRequest.setSessionId(10L);

        pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);
    }

    @Test
    @DisplayName("创建预约-待实现")
    void create_Todo() {
        assertThatThrownBy(() -> bookingService.create(1L, createRequest))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }

    @Test
    @DisplayName("取消预约-待实现")
    void cancel_Todo() {
        assertThatThrownBy(() -> bookingService.cancel(1L, 1L))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }

    @Test
    @DisplayName("我的预约列表-待实现")
    void myBookings_Todo() {
        assertThatThrownBy(() -> bookingService.myBookings(1L, pageRequest))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }

    @Test
    @DisplayName("获取预约详情-待实现")
    void getDetail_Todo() {
        assertThatThrownBy(() -> bookingService.getDetail(1L))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }
}
