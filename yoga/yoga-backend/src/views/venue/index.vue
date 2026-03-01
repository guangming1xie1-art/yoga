<template>
  <div class="venue-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>场馆列表</span>
          <el-button type="primary" @click="handleCreate">新增场馆</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="场馆名称" />
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="场馆名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="form.city" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="营业时间">
          <el-input v-model="form.businessHours" />
        </el-form-item>
        <el-form-item label="场馆简介">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getVenueList, createVenue, updateVenue, deleteVenue } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增场馆')
const formRef = ref(null)
const editId = ref(null)

const form = reactive({
  name: '',
  city: '',
  address: '',
  phone: '',
  businessHours: '',
  description: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入场馆名称', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getVenueList({ page: page.value, size: pageSize.value })
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

const handleCreate = () => {
  editId.value = null
  dialogTitle.value = '新增场馆'
  Object.assign(form, {
    name: '', city: '', address: '', phone: '',
    businessHours: '', description: '', status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editId.value = row.id
  dialogTitle.value = '编辑场馆'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (editId.value) {
      await updateVenue(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createVenue(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该场馆？', '提示', { type: 'warning' })
  await deleteVenue(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.venue-page {
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
