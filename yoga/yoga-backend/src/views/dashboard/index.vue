<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409EFF;">
            <el-icon size="28"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.users }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67C23A;">
            <el-icon size="28"><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.venues }}</div>
            <div class="stat-label">场馆数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #E6A23C;">
            <el-icon size="28"><Tickets /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.bookings }}</div>
            <div class="stat-label">今日预约</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #F56C6C;">
            <el-icon size="28"><Wallet /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.orders }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="welcome-card">
      <h2>欢迎使用瑜伽场馆管理系统</h2>
      <p>系统已就绪，您可以开始管理场馆、课程、预约等业务。</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({
  users: 0,
  venues: 0,
  bookings: 0,
  orders: 0
})

onMounted(() => {
  // TODO: 加载统计数据
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-card :deep(.el-card__body) {
  display: flex;
  width: 100%;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 16px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.welcome-card {
  margin-top: 20px;
}

.welcome-card h2 {
  margin: 0 0 12px 0;
  color: #303133;
}

.welcome-card p {
  margin: 0;
  color: #606266;
}
</style>
