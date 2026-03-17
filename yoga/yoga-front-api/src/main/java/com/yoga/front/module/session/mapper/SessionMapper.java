package com.yoga.front.module.session.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.dto.session.SessionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SessionMapper extends BaseMapper<CourseSession> {
    
    /**
     * 搜索课程排期
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @return 课程排期列表（包含关联的教练和场馆信息）
     */
    IPage<SessionVO> searchSessions(Page<SessionVO> page, @Param("keyword") String keyword);
}
