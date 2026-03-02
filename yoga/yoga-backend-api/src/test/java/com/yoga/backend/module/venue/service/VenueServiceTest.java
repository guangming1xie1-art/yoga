package com.yoga.backend.module.venue.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.backend.module.venue.mapper.VenueMapper;
import com.yoga.backend.module.venue.service.impl.VenueServiceImpl;
import com.yoga.common.dto.venue.VenueDTO;
import com.yoga.common.entity.Venue;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("后台管理端场馆服务测试")
class VenueServiceTest {

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Venue venue;
    private VenueDTO venueDTO;
    private List<Venue> venueList;

    @BeforeEach
    void setUp() {
        venue = new Venue();
        venue.setId(1L);
        venue.setName("测试瑜伽馆");
        venue.setCity("北京");
        venue.setAddress("朝阳区测试路123号");
        venue.setPhone("010-12345678");
        venue.setBusinessHours("09:00-21:00");
        venue.setCoverImage("https://example.com/image.jpg");
        venue.setDescription("这是一家测试瑜伽馆");
        venue.setStatus(1);
        venue.setCreatedAt(LocalDateTime.now());
        venue.setUpdatedAt(LocalDateTime.now());

        venueDTO = new VenueDTO();
        venueDTO.setName("测试瑜伽馆");
        venueDTO.setCity("北京");
        venueDTO.setAddress("朝阳区测试路123号");
        venueDTO.setPhone("010-12345678");
        venueDTO.setBusinessHours("09:00-21:00");
        venueDTO.setCoverImage("https://example.com/image.jpg");
        venueDTO.setDescription("这是一家测试瑜伽馆");
        venueDTO.setStatus(1);

        Venue venue2 = new Venue();
        venue2.setId(2L);
        venue2.setName("第二家瑜伽馆");
        venue2.setCity("上海");
        venue2.setStatus(1);

        venueList = Arrays.asList(venue, venue2);
    }

    @Test
    @DisplayName("分页查询场馆列表")
    void listVenues_Success() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);

        Page<Venue> page = new Page<>(1, 10);
        page.setRecords(venueList);
        page.setTotal(2);
        page.setPages(1);

        when(venueMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResponse<Venue> result = venueService.listVenues(pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getList()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getPages()).isEqualTo(1);

        verify(venueMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("分页查询场馆列表-空结果")
    void listVenues_EmptyResult() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);

        Page<Venue> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);
        page.setPages(0);

        when(venueMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResponse<Venue> result = venueService.listVenues(pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getList()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0);

        verify(venueMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取场馆详情成功")
    void getDetail_Success() {
        when(venueMapper.selectById(1L)).thenReturn(venue);

        Venue result = venueService.getDetail(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("测试瑜伽馆");
        assertThat(result.getCity()).isEqualTo("北京");

        verify(venueMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取场馆详情失败-场馆不存在")
    void getDetail_Failure_NotFound() {
        when(venueMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> venueService.getDetail(999L))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.VENUE_NOT_FOUND.getCode());
                });

        verify(venueMapper).selectById(999L);
    }

    @Test
    @DisplayName("创建场馆成功")
    void createVenue_Success() {
        when(venueMapper.insert(any(Venue.class))).thenAnswer(invocation -> {
            Venue v = invocation.getArgument(0);
            v.setId(1L);
            return 1;
        });

        Venue result = venueService.create(venueDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("测试瑜伽馆");
        assertThat(result.getCity()).isEqualTo("北京");
        assertThat(result.getStatus()).isEqualTo(1);

        ArgumentCaptor<Venue> venueCaptor = ArgumentCaptor.forClass(Venue.class);
        verify(venueMapper).insert(venueCaptor.capture());

        Venue capturedVenue = venueCaptor.getValue();
        assertThat(capturedVenue.getName()).isEqualTo("测试瑜伽馆");
        assertThat(capturedVenue.getCity()).isEqualTo("北京");
        assertThat(capturedVenue.getAddress()).isEqualTo("朝阳区测试路123号");
    }

    @Test
    @DisplayName("创建场馆成功-使用默认状态")
    void createVenue_WithDefaultStatus() {
        venueDTO.setStatus(null);

        when(venueMapper.insert(any(Venue.class))).thenAnswer(invocation -> {
            Venue v = invocation.getArgument(0);
            v.setId(1L);
            return 1;
        });

        Venue result = venueService.create(venueDTO);

        assertThat(result.getStatus()).isEqualTo(1);
    }

    @Test
    @DisplayName("更新场馆成功")
    void updateVenue_Success() {
        when(venueMapper.selectById(1L)).thenReturn(venue);
        when(venueMapper.updateById(any(Venue.class))).thenReturn(1);

        venueDTO.setName("更新后的瑜伽馆");
        venueDTO.setCity("上海");

        Venue result = venueService.update(1L, venueDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("更新后的瑜伽馆");
        assertThat(result.getCity()).isEqualTo("上海");

        verify(venueMapper).selectById(1L);
        verify(venueMapper).updateById(any(Venue.class));
    }

    @Test
    @DisplayName("更新场馆成功-部分字段更新")
    void updateVenue_PartialUpdate() {
        when(venueMapper.selectById(1L)).thenReturn(venue);
        when(venueMapper.updateById(any(Venue.class))).thenReturn(1);

        VenueDTO partialDTO = new VenueDTO();
        partialDTO.setName("新名称");
        partialDTO.setCity("北京");
        partialDTO.setStatus(null);

        Venue result = venueService.update(1L, partialDTO);

        assertThat(result.getName()).isEqualTo("新名称");
        assertThat(result.getStatus()).isEqualTo(1);

        verify(venueMapper).updateById(any(Venue.class));
    }

    @Test
    @DisplayName("更新场馆失败-场馆不存在")
    void updateVenue_Failure_NotFound() {
        when(venueMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> venueService.update(999L, venueDTO))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.VENUE_NOT_FOUND.getCode());
                });

        verify(venueMapper).selectById(999L);
        verify(venueMapper, never()).updateById(any(Venue.class));
    }

    @Test
    @DisplayName("删除场馆成功")
    void deleteVenue_Success() {
        when(venueMapper.deleteById(1L)).thenReturn(1);

        venueService.delete(1L);

        verify(venueMapper).deleteById(1L);
    }

    @Test
    @DisplayName("删除场馆-即使场馆不存在也不抛异常")
    void deleteVenue_NotFound() {
        when(venueMapper.deleteById(999L)).thenReturn(0);

        venueService.delete(999L);

        verify(venueMapper).deleteById(999L);
    }
}
