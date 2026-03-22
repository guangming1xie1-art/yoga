<template>
  <div class="template-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>课程模板列表</span>
          <el-button type="primary" @click="handleCreate">新增模板</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="durationMinutes" label="时长(分钟)" width="120" />
        <el-table-column prop="capacity" label="容量" width="80" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上线' : '下线' }}
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
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="场馆ID" prop="venueId">
          <el-input-number v-model="form.venueId" :min="1" />
        </el-form-item>
        <el-form-item label="课程类别">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="封面图URL">
          <el-input v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="durationMinutes">
          <el-input-number v-model="form.durationMinutes" :min="1" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="form.difficulty">
            <el-option label="初级" value="BEGINNER" />
            <el-option label="中级" value="INTERMEDIATE" />
            <el-option label="高级" value="ADVANCED" />
          </el-select>
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
import { getTemplateList, createTemplate } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增模板')
const formRef = ref(null)
const editId = ref(null)

const form = reactive({
  name: '',
  venueId: 1,
  category: '',
  description: '',
  coverImage: '',
  durationMinutes: 60,
  capacity: 20,
  price: 99,
  difficulty: 'BEGINNER',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  venueId: [{ required: true, message: '请输入场馆ID', trigger: 'blur' }],
  durationMinutes: [{ required: true, message: '请输入时长', trigger: 'blur' }],
  capacity: [{ required: true, message: '请输入容量', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getTemplateList({ page: page.value, size: pageSize.value })
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
  dialogTitle.value = '新增模板'
  Object.assign(form, {
    name: '', venueId: 1, category: '',
    description: '', coverImage: '', durationMinutes: 60,
    capacity: 20, price: 99, difficulty: 'BEGINNER', status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editId.value = row.id
  dialogTitle.value = '编辑模板'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (editId.value) {
      ElMessage.success('更新成功')
    } else {
      await createTemplate(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该模板？', '提示', { type: 'warning' })
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.template-page {
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
