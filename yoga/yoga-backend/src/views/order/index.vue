<template>
  <div class="order-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="订单号" width="150" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="sessionId" label="课程排期ID" width="120" />
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="refundAmount" label="退款金额" width="100">
          <template #default="{ row }">
            {{ row.refundAmount ? '¥' + row.refundAmount : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="paidAt" label="支付时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button 
              size="small" 
              type="danger" 
              :disabled="!canRefund(row.status)"
              @click="handleRefund(row)"
            >
              退款
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
import { getOrderList } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getOrderList({ page: page.value, size: pageSize.value })
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
    'PAID': 'success',
    'REFUNDING': 'info',
    'REFUNDED': 'danger',
    'CANCELLED': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'REFUNDING': '退款中',
    'REFUNDED': '已退款',
    'CANCELLED': '已取消'
  }
  return map[status] || status
}

const canRefund = (status) => {
  return status === 'PAID'
}

const handleRefund = async (row) => {
  await ElMessageBox.confirm('确定退款该订单？', '提示', { type: 'warning' })
  ElMessage.success('退款成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.order-page {
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
