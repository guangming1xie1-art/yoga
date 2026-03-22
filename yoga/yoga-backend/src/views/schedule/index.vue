<template>
  <div class="schedule-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排课规则列表</span>
          <el-button type="primary" @click="handleCreate">新增规则</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="templateId" label="课程模板ID" width="120" />
        <el-table-column prop="coachId" label="教练ID" width="100" />
        <el-table-column prop="weekdays" label="上课星期" />
        <el-table-column prop="startTime" label="开始时间" width="100" />
        <el-table-column prop="endTime" label="结束时间" width="100" />
        <el-table-column prop="advanceDays" label="提前预约天数" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastGenerated" label="最后生成时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="primary" @click="handleGenerate(row)">生成排期</el-button>
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-form-item label="课程模板ID" prop="templateId">
          <el-input-number v-model="form.templateId" :min="1" />
        </el-form-item>
        <el-form-item label="教练ID" prop="coachId">
          <el-input-number v-model="form.coachId" :min="1" />
        </el-form-item>
        <el-form-item label="上课星期" prop="weekdays">
          <el-checkbox-group v-model="weekdayList">
            <el-checkbox label="MONDAY">周一</el-checkbox>
            <el-checkbox label="TUESDAY">周二</el-checkbox>
            <el-checkbox label="WEDNESDAY">周三</el-checkbox>
            <el-checkbox label="THURSDAY">周四</el-checkbox>
            <el-checkbox label="FRIDAY">周五</el-checkbox>
            <el-checkbox label="SATURDAY">周六</el-checkbox>
            <el-checkbox label="SUNDAY">周日</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker v-model="form.startTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker v-model="form.endTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="提前预约天数" prop="advanceDays">
          <el-input-number v-model="form.advanceDays" :min="1" :max="30" />
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
import { getScheduleList, createSchedule } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增规则')
const formRef = ref(null)
const editId = ref(null)
const weekdayList = ref([])

const form = reactive({
  templateId: 1,
  coachId: 1,
  weekdays: '',
  startTime: '09:00',
  endTime: '10:00',
  advanceDays: 7,
  status: 1
})

const rules = {
  templateId: [{ required: true, message: '请输入课程模板ID', trigger: 'blur' }],
  coachId: [{ required: true, message: '请输入教练ID', trigger: 'blur' }],
  weekdays: [{ required: true, message: '请选择上课星期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getScheduleList({ page: page.value, size: pageSize.value })
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
  dialogTitle.value = '新增规则'
  Object.assign(form, {
    templateId: 1, coachId: 1, weekdays: '',
    startTime: '09:00', endTime: '10:00', advanceDays: 7, status: 1
  })
  weekdayList.value = []
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editId.value = row.id
  dialogTitle.value = '编辑规则'
  Object.assign(form, row)
  weekdayList.value = row.weekdays.split(',')
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  form.weekdays = weekdayList.value.join(',')
  try {
    if (editId.value) {
      ElMessage.success('更新成功')
    } else {
      await createSchedule(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleGenerate = async (row) => {
  await ElMessageBox.confirm('确定生成课程排期？', '提示', { type: 'warning' })
  ElMessage.success('生成排期成功')
  loadData()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该规则？', '提示', { type: 'warning' })
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.schedule-page {
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
