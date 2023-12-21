<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import {
  getLeaveListService,
  allowApplyLeaveService,
  rejectApplyLeaveService
} from '@/api/teacher'
import { formatTime } from '@/utils/format'

const leaveList = ref([])
const isLoading = ref(false)
const userStore = useUserStore()

//得到请假列表
const getLeaveList = async () => {
  isLoading.value = true
  const resp = await getLeaveListService(userStore.userId)
  leaveList.value = resp.data
  isLoading.value = false
}
getLeaveList()

//处理申请
const allowApplyLeave = async (row) => {
  await allowApplyLeaveService(row)
  ElMessage.success('操作成功')
  getLeaveList()
}

const rejectApplyLeave = async (row) => {
  ElMessageBox.confirm('确认不批准这个请假申请吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      //1.发送不批准请求
      await rejectApplyLeaveService(row)
      ElMessage({
        type: 'success',
        message: '操作成功'
      })
      //3.再次发送请求获取数据
      getLeaveList()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '操作取消'
      })
    })
}
</script>

<template>
  <PageContainer title="请假管理页面">
    <template #extra>
      <div class="header"></div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="leaveList" style="width: 100%">
      <!-- 不展示教师号，只留着传输数据用 -->
      <el-table-column
        prop="techNum"
        label="教师号"
        v-if="false"
      ></el-table-column>
      <el-table-column prop="courseNum" label="课程号"></el-table-column>
      <el-table-column prop="courseName" label="课程名"></el-table-column>
      <el-table-column prop="stuNum" label="学号"></el-table-column>
      <el-table-column prop="stuName" label="学生名"></el-table-column>
      <el-table-column prop="selWeek" label="星期"></el-table-column>
      <el-table-column prop="selStart" label="第一节课">
        <template #default="{ row }"> 第{{ row.selStart }}节 </template>
      </el-table-column>
      <el-table-column prop="selEnd" label="最后一节">
        <template #default="{ row }"> 第{{ row.selEnd }}节 </template>
      </el-table-column>
      <el-table-column prop="leaveStart" label="请假时间">
        <template #default="{ row }">
          {{ formatTime(row.leaveStart) }}
        </template>
      </el-table-column>
      <el-table-column prop="leaveEnd" label="结束时间">
        <template #default="{ row }">
          {{ formatTime(row.leaveEnd) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <div style="display: flex">
            <el-button type="primary" @click="allowApplyLeave(row)"
              >批准</el-button
            >
            <el-button type="danger" @click="rejectApplyLeave(row)"
              >驳回</el-button
            >
          </div>
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>
  </PageContainer>
</template>

<style scoped lang="scss">
.header {
  display: flex;
}
</style>
