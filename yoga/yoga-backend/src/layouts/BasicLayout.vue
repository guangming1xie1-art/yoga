<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <h1>瑜伽管理系统</h1>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="menu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        
        <el-sub-menu index="venue-management">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>场馆管理</span>
          </template>
          <el-menu-item index="/venue">场馆列表</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="course-management">
          <template #title>
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </template>
          <el-menu-item index="/template">课程模板</el-menu-item>
          <el-menu-item index="/schedule">排课管理</el-menu-item>
          <el-menu-item index="/session">课程排期</el-menu-item>
          <el-menu-item index="/coach">教练管理</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="booking-management">
          <template #title>
            <el-icon><Tickets /></el-icon>
            <span>预约订单</span>
          </template>
          <el-menu-item index="/booking">预约管理</el-menu-item>
          <el-menu-item index="/order">订单管理</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/review">
          <el-icon><ChatDotSquare /></el-icon>
          <span>评价管理</span>
        </el-menu-item>
        
        <el-menu-item index="/user">
          <el-icon><Avatar /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        
        <el-menu-item index="/notification">
          <el-icon><Bell /></el-icon>
          <span>通知管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="breadcrumb">{{ currentPageTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><User /></el-icon>
              <span>管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const currentPageTitle = computed(() => route.meta.title || '首页')

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
}

.logo h1 {
  color: #fff;
  font-size: 18px;
  margin: 0;
}

.menu {
  border: none;
}

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.breadcrumb {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
