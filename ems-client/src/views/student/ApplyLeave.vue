<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { formatLeaveTime } from '@/utils/format'
import {
  getLeaveListService,
  applyLeaveService,
  removeLeaveService
} from '@/api/student'

const leaveList = ref([])
const isLoading = ref(false)
const userStore = useUserStore()
const dialogFormVisible = ref(false)

//得到请假列表
const getLeaveList = async () => {
  isLoading.value = true
  const resp = await getLeaveListService(userStore.userId)
  leaveList.value = resp.data
  isLoading.value = false
}
getLeaveList()

//申请请假
const formData = ref({
  courseNum: '',
  techNum: '',
  stuNum: '',
  leaveStart: '',
  leaveEnd: ''
})

const form = ref(null)

const rules = {
  leaveStart: {
    required: true,
    message: '开始请假时间不能为空',
    trigger: 'blur'
  },
  leaveEnd: [
    { required: true, message: '结束请假时间不能为空', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          new Date(formData.value.leaveStart) >=
          new Date(formData.value.leaveEnd)
        ) {
          callback(new Error('开始请假时间应小于结束请假时间'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

//打开对话框
const openDialog = (row) => {
  dialogFormVisible.value = true
  formData.value = { ...row }
  formData.value.stuNum = userStore.userId
}
//处理申请
const applyLeave = async () => {
  //校验表单
  await form.value.validate()
  await applyLeaveService(formData.value)
  ElMessage.success('操作成功')
  dialogFormVisible.value = false
  getLeaveList()
}

const removeLeave = async (row) => {
  ElMessageBox.confirm('确认撤销这个请假申请吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      //1.发送撤销请求
      row.stuNum = userStore.userId
      await removeLeaveService(row)
      ElMessage({
        type: 'success',
        message: '撤销成功'
      })
      //3.再次发送请求获取数据
      getLeaveList()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '删除取消'
      })
    })
}
</script>

<template>
  <PageContainer title="请假申请页面">
    <template #extra>
      <div class="header"></div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="leaveList" style="width: 100%">
      <el-table-column prop="courseNum" label="课程号"></el-table-column>
      <el-table-column prop="courseName" label="课程名"></el-table-column>
      <el-table-column prop="techNum" label="教师号"></el-table-column>
      <el-table-column prop="techName" label="教师名"></el-table-column>
      <el-table-column prop="selWeek" label="星期"></el-table-column>
      <el-table-column prop="selStart" label="第一节课">
        <template #default="{ row }"> 第{{ row.selStart }}节 </template>
      </el-table-column>
      <el-table-column prop="selEnd" label="最后一节">
        <template #default="{ row }"> 第{{ row.selEnd }}节 </template>
      </el-table-column>
      <el-table-column prop="leaveStart" label="请假时间">
        <template #default="{ row }">
          {{ formatLeaveTime(row.leaveStart) }}
        </template>
      </el-table-column>
      <el-table-column prop="leaveEnd" label="结束时间">
        <template #default="{ row }">
          {{ formatLeaveTime(row.leaveEnd) }}
        </template>
      </el-table-column>
      <el-table-column prop="leaveStatus" label="请假状态">
        <template #default="{ row }">
          <el-text type="info" v-if="row.leaveStatus == 0">未申请</el-text>
          <el-text type="primary" v-if="row.leaveStatus == 1">审批中</el-text>
          <el-text type="success" v-if="row.leaveStatus == 2">审批通过</el-text>
          <el-text type="danger" v-if="row.leaveStatus == -1"
            >审批未通过</el-text
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button
            type="primary"
            @click="openDialog(row)"
            v-if="row.leaveStatus == 0 || row.leaveStatus == 2"
            >申请</el-button
          >
          <el-button
            type="danger"
            @click="removeLeave(row)"
            v-if="row.leaveStatus == -1 || row.leaveStatus == 1"
            >撤销申请</el-button
          >
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>

    <!-- 请假内容 -->
    <!-- 消息对话框  打分内容 -->
    <el-dialog v-model="dialogFormVisible" title="申请请假页面">
      <el-form :rules="rules" :model="formData" ref="form">
        <el-form-item label="请假开始时间" prop="leaveStart">
          <el-date-picker
            value-format="YYYY-MM-DDTHH:mm:ss"
            v-model="formData.leaveStart"
            type="datetime"
            placeholder="请假开始时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            date-format="MMM DD, YYYY"
            time-format="HH:mm"
          />
        </el-form-item>
        <el-form-item label="请假结束时间" prop="leaveEnd">
          <el-date-picker
            value-format="YYYY-MM-DDTHH:mm:ss"
            v-model="formData.leaveEnd"
            type="datetime"
            placeholder="请假结束时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            date-format="MMM DD, YYYY"
            time-format="HH:mm"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="applyLeave"> 提交 </el-button>
        </span>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<style scoped lang="scss">
.header {
  display: flex;
}
</style>
