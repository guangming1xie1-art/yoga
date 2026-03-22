<template>
  <div class="coach-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>教练列表</span>
          <el-button type="primary" @click="handleCreate">新增教练</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="specialty" label="专长" />
        <el-table-column prop="yearsExp" label="从教年限" width="100" />
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
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="场馆ID" prop="venueId">
          <el-input-number v-model="form.venueId" :min="1" />
        </el-form-item>
        <el-form-item label="专长">
          <el-input v-model="form.specialty" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input v-model="form.bio" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="头像URL">
          <el-input v-model="form.avatar" />
        </el-form-item>
        <el-form-item label="从教年限">
          <el-input-number v-model="form.yearsExp" :min="0" />
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
import { getCoachList, createCoach, updateCoach } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增教练')
const formRef = ref(null)
const editId = ref(null)

const form = reactive({
  realName: '',
  venueId: 1,
  specialty: '',
  bio: '',
  avatar: '',
  yearsExp: 0,
  status: 1
})

const rules = {
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  venueId: [{ required: true, message: '请输入场馆ID', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCoachList({ page: page.value, size: pageSize.value })
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
  dialogTitle.value = '新增教练'
  Object.assign(form, {
    realName: '', venueId: 1, specialty: '',
    bio: '', avatar: '', yearsExp: 0, status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editId.value = row.id
  dialogTitle.value = '编辑教练'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (editId.value) {
      await updateCoach(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createCoach(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该教练？', '提示', { type: 'warning' })
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.coach-page {
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
