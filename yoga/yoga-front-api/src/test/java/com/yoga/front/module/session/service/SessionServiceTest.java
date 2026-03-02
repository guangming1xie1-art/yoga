package com.yoga.front.module.session.service;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.session.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("用户端课程排期服务测试")
class SessionServiceTest {

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);
    }

    @Test
    @DisplayName("获取课程列表-待实现")
    void listSessions_Todo() {
        assertThatThrownBy(() -> sessionService.listSessions(1L, "基础", "2024-06-15", pageRequest))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }

    @Test
    @DisplayName("获取课程详情-返回未找到异常")
    void getDetail_NotFound() {
        assertThatThrownBy(() -> sessionService.getDetail(1L))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.SESSION_NOT_FOUND.getCode());
                });
    }

    @Test
    @DisplayName("搜索课程-待实现")
    void search_Todo() {
        assertThatThrownBy(() -> sessionService.search("瑜伽", pageRequest))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }

    @Test
    @DisplayName("获取课程评价列表-待实现")
    void listReviews_Todo() {
        assertThatThrownBy(() -> sessionService.listReviews(1L, pageRequest))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("TODO");
    }
}
