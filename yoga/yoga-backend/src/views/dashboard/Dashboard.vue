<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-title">今日预约</div>
              <div class="stat-value">{{ stats.todayBookings }}</div>
            </div>
            <el-icon class="stat-icon" color="#409EFF"><Tickets /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-title">今日收入</div>
              <div class="stat-value">¥{{ stats.todayRevenue }}</div>
            </div>
            <el-icon class="stat-icon" color="#67C23A"><Wallet /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-title">活跃用户</div>
              <div class="stat-value">{{ stats.activeUsers }}</div>
            </div>
            <el-icon class="stat-icon" color="#E6A23C"><UserFilled /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-title">课程总数</div>
              <div class="stat-value">{{ stats.totalCourses }}</div>
            </div>
            <el-icon class="stat-icon" color="#F56C6C"><Reading /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近预约</span>
          </template>
          <el-table :data="recentBookings" style="width: 100%">
            <el-table-column prop="userName" label="用户" />
            <el-table-column prop="courseName" label="课程" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === 'CONFIRMED' ? 'success' : 'warning'">
                  {{ row.status === 'CONFIRMED' ? '已确认' : '待支付' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="预约时间" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近订单</span>
          </template>
          <el-table :data="recentOrders" style="width: 100%">
            <el-table-column prop="orderNo" label="订单号" width="180" />
            <el-table-column prop="amount" label="金额">
              <template #default="{ row }">
                ¥{{ row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === 'PAID' ? 'success' : 'info'">
                  {{ row.status === 'PAID' ? '已支付' : '未支付' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="下单时间" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({
  todayBookings: 0,
  todayRevenue: 0,
  activeUsers: 0,
  totalCourses: 0
})

const recentBookings = ref([])
const recentOrders = ref([])

onMounted(() => {
  // TODO: 加载数据
  stats.value = {
    todayBookings: 28,
    todayRevenue: 5680,
    activeUsers: 156,
    totalCourses: 45
  }
  
  recentBookings.value = [
    { userName: '张三', courseName: '瑜伽基础课', status: 'CONFIRMED', createdAt: '2024-01-15 10:30' },
    { userName: '李四', courseName: '高温瑜伽', status: 'PENDING', createdAt: '2024-01-15 10:25' },
    { userName: '王五', courseName: '空中瑜伽', status: 'CONFIRMED', createdAt: '2024-01-15 10:20' }
  ]
  
  recentOrders.value = [
    { orderNo: 'ORDER20240115001', amount: 99, status: 'PAID', createdAt: '2024-01-15 10:30' },
    { orderNo: 'ORDER20240115002', amount: 199, status: 'PAID', createdAt: '2024-01-15 10:25' },
    { orderNo: 'ORDER20240115003', amount: 0, status: 'UNPAID', createdAt: '2024-01-15 10:20' }
  ]
})
</script>

<style lang="scss" scoped>
.dashboard {
  .stat-card {
    .stat-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .stat-info {
        .stat-title {
          font-size: 14px;
          color: #909399;
          margin-bottom: 10px;
        }
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
        }
      }
      
      .stat-icon {
        font-size: 48px;
      }
    }
  }
}
</style>
