package com.yoga.backend.module.venue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.backend.module.venue.service.VenueService;
import com.yoga.common.dto.venue.VenueDTO;
import com.yoga.common.entity.Venue;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VenueController.class)
@DisplayName("后台管理端场馆控制器测试")
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VenueService venueService;

    private Venue venue;
    private VenueDTO venueDTO;
    private PageResponse<Venue> pageResponse;

    @BeforeEach
    void setUp() {
        venue = new Venue();
        venue.setId(1L);
        venue.setName("测试瑜伽馆");
        venue.setCity("北京");
        venue.setAddress("朝阳区测试路123号");
        venue.setPhone("010-12345678");
        venue.setBusinessHours("09:00-21:00");
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
        venueDTO.setDescription("这是一家测试瑜伽馆");
        venueDTO.setStatus(1);

        pageResponse = PageResponse.of(List.of(venue), 1, 1, 10);
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("获取场馆列表成功")
    void listVenues_Success() throws Exception {
        when(venueService.listVenues(any(PageRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/backend/venue/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list[0].id").value(1))
                .andExpect(jsonPath("$.data.list[0].name").value("测试瑜伽馆"))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10));

        verify(venueService).listVenues(any(PageRequest.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("获取场馆列表-使用默认分页参数")
    void listVenues_WithDefaultPageParams() throws Exception {
        when(venueService.listVenues(any(PageRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/backend/venue/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(venueService).listVenues(any(PageRequest.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("获取场馆详情成功")
    void getDetail_Success() throws Exception {
        when(venueService.getDetail(1L)).thenReturn(venue);

        mockMvc.perform(get("/api/backend/venue/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("测试瑜伽馆"))
                .andExpect(jsonPath("$.data.city").value("北京"))
                .andExpect(jsonPath("$.data.address").value("朝阳区测试路123号"));

        verify(venueService).getDetail(1L);
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("获取场馆详情失败-场馆不存在")
    void getDetail_Failure_NotFound() throws Exception {
        when(venueService.getDetail(999L))
                .thenThrow(new BusinessException(ResultCode.VENUE_NOT_FOUND));

        mockMvc.perform(get("/api/backend/venue/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VENUE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.VENUE_NOT_FOUND.getMessage()));

        verify(venueService).getDetail(999L);
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("创建场馆成功")
    void createVenue_Success() throws Exception {
        when(venueService.create(any(VenueDTO.class))).thenReturn(venue);

        mockMvc.perform(post("/api/backend/venue")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("测试瑜伽馆"));

        verify(venueService).create(any(VenueDTO.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("创建场馆失败-场馆名称为空")
    void createVenue_Failure_EmptyName() throws Exception {
        VenueDTO invalidDTO = new VenueDTO();
        invalidDTO.setName("");
        invalidDTO.setCity("北京");

        mockMvc.perform(post("/api/backend/venue")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));

        verify(venueService, never()).create(any(VenueDTO.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("创建场馆失败-城市为空")
    void createVenue_Failure_EmptyCity() throws Exception {
        VenueDTO invalidDTO = new VenueDTO();
        invalidDTO.setName("测试瑜伽馆");
        invalidDTO.setCity("");

        mockMvc.perform(post("/api/backend/venue")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));

        verify(venueService, never()).create(any(VenueDTO.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("更新场馆成功")
    void updateVenue_Success() throws Exception {
        venue.setName("更新后的瑜伽馆");
        when(venueService.update(eq(1L), any(VenueDTO.class))).thenReturn(venue);

        venueDTO.setName("更新后的瑜伽馆");

        mockMvc.perform(put("/api/backend/venue/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.name").value("更新后的瑜伽馆"));

        verify(venueService).update(eq(1L), any(VenueDTO.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("更新场馆失败-场馆不存在")
    void updateVenue_Failure_NotFound() throws Exception {
        when(venueService.update(eq(999L), any(VenueDTO.class)))
                .thenThrow(new BusinessException(ResultCode.VENUE_NOT_FOUND));

        mockMvc.perform(put("/api/backend/venue/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VENUE_NOT_FOUND.getCode()));

        verify(venueService).update(eq(999L), any(VenueDTO.class));
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("删除场馆成功")
    void deleteVenue_Success() throws Exception {
        doNothing().when(venueService).delete(1L);

        mockMvc.perform(delete("/api/backend/venue/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(venueService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "SYS_ADMIN")
    @DisplayName("删除场馆失败-场馆不存在")
    void deleteVenue_Failure_NotFound() throws Exception {
        doThrow(new BusinessException(ResultCode.VENUE_NOT_FOUND))
                .when(venueService).delete(999L);

        mockMvc.perform(delete("/api/backend/venue/999")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VENUE_NOT_FOUND.getCode()));

        verify(venueService).delete(999L);
    }

    @Test
    @DisplayName("未授权访问场馆列表")
    void listVenues_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/backend/venue/list"))
                .andExpect(status().isUnauthorized());

        verify(venueService, never()).listVenues(any(PageRequest.class));
    }
}
