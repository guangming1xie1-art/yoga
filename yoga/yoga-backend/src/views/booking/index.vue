<template>
  <div class="booking-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="sessionId" label="课程排期ID" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cancelBy" label="取消人" width="100" />
        <el-table-column prop="cancelReason" label="取消原因" />
        <el-table-column prop="createdAt" label="预约时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button 
              size="small" 
              type="danger" 
              :disabled="!canCancel(row.status)"
              @click="handleCancel(row)"
            >
              取消预约
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        class="pagination"
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBookingList } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getBookingList({ page: page.value, size: pageSize.value })
    tableData.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (val) => {
  page.value = val
  loadData()
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'warning',
    'CONFIRMED': 'success',
    'CANCELLED': 'danger',
    'COMPLETED': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'CANCELLED': '已取消',
    'COMPLETED': '已完成'
  }
  return map[status] || status
}

const canCancel = (status) => {
  return status === 'PENDING' || status === 'CONFIRMED'
}

const handleCancel = async (row) => {
  await ElMessageBox.confirm('确定取消该预约？', '提示', { type: 'warning' })
  ElMessage.success('取消成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.booking-page {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
