<template>
  <view class="index-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input class="search-input" placeholder="搜索课程/场馆" @confirm="onSearch" />
    </view>

    <!-- 轮播图 -->
    <swiper class="banner-swiper" indicator-dots autoplay circular>
      <swiper-item v-for="(banner, index) in banners" :key="index">
        <image class="banner-img" :src="banner.image" mode="aspectFill" />
      </swiper-item>
    </swiper>

    <!-- 快捷入口 -->
    <view class="quick-nav">
      <view class="nav-item" @click="goSchedule">
        <image class="nav-icon" src="/static/icons/calendar.png" />
        <text>课程日历</text>
      </view>
      <view class="nav-item" @click="goBooking">
        <image class="nav-icon" src="/static/icons/ticket.png" />
        <text>我的预约</text>
      </view>
      <view class="nav-item">
        <image class="nav-icon" src="/static/icons/coach.png" />
        <text>教练团队</text>
      </view>
      <view class="nav-item">
        <image class="nav-icon" src="/static/icons/venue.png" />
        <text>场馆介绍</text>
      </view>
    </view>

    <!-- 推荐课程 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">推荐课程</text>
        <text class="section-more" @click="goSchedule">更多></text>
      </view>
      <view class="course-list">
        <view class="course-card" v-for="course in courses" :key="course.id" @click="goDetail(course.id)">
          <image class="course-cover" :src="course.coverImage" mode="aspectFill" />
          <view class="course-info">
            <text class="course-name">{{ course.name }}</text>
            <text class="course-meta">{{ course.coachName }} | {{ course.duration }}分钟</text>
            <text class="course-price">¥{{ course.price }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { homeApi, sessionApi } from '@/api/index'

const banners = ref([])
const courses = ref([])

onMounted(async () => {
  try {
    const bannerData = await homeApi.getBanners()
    banners.value = bannerData || []
    
    const courseData = await sessionApi.list({ page: 1, size: 6 })
    courses.value = courseData?.list || []
  } catch (e) {
    console.error('加载数据失败', e)
  }
})

const onSearch = (e) => {
  console.log('搜索:', e.detail.value)
}

const goSchedule = () => {
  uni.switchTab({ url: '/pages/schedule/schedule' })
}

const goBooking = () => {
  uni.switchTab({ url: '/pages/booking/booking' })
}

const goDetail = (id) => {
  uni.navigateTo({ url: `/pages/session/detail?id=${id}` })
}
</script>

<style lang="scss" scoped>
.index-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.search-bar {
  padding: 20rpx 30rpx;
  background: #fff;
  
  .search-input {
    height: 70rpx;
    background: #f5f5f5;
    border-radius: 35rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
  }
}

.banner-swiper {
  height: 300rpx;
  
  .banner-img {
    width: 100%;
    height: 100%;
  }
}

.quick-nav {
  display: flex;
  justify-content: space-around;
  padding: 30rpx 0;
  background: #fff;
  margin-bottom: 20rpx;
  
  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .nav-icon {
      width: 80rpx;
      height: 80rpx;
      margin-bottom: 10rpx;
    }
    
    text {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.section {
  padding: 20rpx;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
    }
    
    .section-more {
      font-size: 24rpx;
      color: #999;
    }
  }
}

.course-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  
  .course-card {
    width: calc(50% - 10rpx);
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    
    .course-cover {
      width: 100%;
      height: 200rpx;
    }
    
    .course-info {
      padding: 16rpx;
      
      .course-name {
        font-size: 28rpx;
        font-weight: bold;
        display: block;
        margin-bottom: 8rpx;
      }
      
      .course-meta {
        font-size: 24rpx;
        color: #999;
        display: block;
        margin-bottom: 8rpx;
      }
      
      .course-price {
        font-size: 28rpx;
        color: #ff6b6b;
        font-weight: bold;
      }
    }
  }
}
</style>
