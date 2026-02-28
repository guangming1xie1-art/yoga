<template>
  <el-container class="basic-layout">
    <!-- 侧边栏 -->
    <el-aside width="220px">
      <div class="logo">
        <h2>瑜伽管理系统</h2>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        class="side-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/venue">
          <el-icon><OfficeBuilding /></el-icon>
          <span>场馆管理</span>
        </el-menu-item>
        <el-menu-item index="/coach">
          <el-icon><User /></el-icon>
          <span>教练管理</span>
        </el-menu-item>
        <el-menu-item index="/course">
          <el-icon><Reading /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/booking">
          <el-icon><Tickets /></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><Wallet /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/user">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/system">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体区域 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header>
        <div class="header-left">
          <el-icon class="fold-icon" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
              <span class="username">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.logout()
    router.push('/login')
  } else if (command === 'profile') {
    // TODO: 跳转个人中心
  }
}
</script>

<style lang="scss" scoped>
.basic-layout {
  height: 100vh;
}

.el-aside {
  background: #304156;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    h2 {
      color: #fff;
      font-size: 18px;
      margin: 0;
    }
  }
  
  .side-menu {
    border-right: none;
  }
}

.el-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  
  .fold-icon {
    font-size: 20px;
    cursor: pointer;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    
    .username {
      font-size: 14px;
    }
  }
}

.el-main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
