<template>
  <div class="user-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户列表</span>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="openid" label="微信OpenID" width="200" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="disabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.disabled === 0 ? 'success' : 'danger'">
              {{ row.disabled === 0 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button 
              size="small" 
              :type="row.disabled === 0 ? 'danger' : 'success'"
              @click="handleToggleStatus(row)"
            >
              {{ row.disabled === 0 ? '禁用' : '启用' }}
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
import { ElMessage } from 'element-plus'
import { getUserList } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserList({ page: page.value, size: pageSize.value })
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

const handleToggleStatus = async (row) => {
  const newStatus = row.disabled === 0 ? 1 : 0
  ElMessage.success(newStatus === 1 ? '禁用成功' : '启用成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-page {
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
