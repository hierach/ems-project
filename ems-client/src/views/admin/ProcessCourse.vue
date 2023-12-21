<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { formatTime } from '@/utils/format'
import { useUserStore } from '@/stores/user'
import {
  getWaitOpenListService,
  permitCourseService,
  deleteCourseService
} from '@/api/admin'

//定义数据
const openList = ref([])
const isLoading = ref(false)
const userStore = useUserStore()

//1.查询本管理员管辖的，status为0的可成
const getWaitOpenList = async () => {
  isLoading.value = true
  const resp = await getWaitOpenListService(userStore.userId)
  isLoading.value = false
  openList.value = resp.data
}
getWaitOpenList()

//2.批准课程
const permitCourse = async (row) => {
  await permitCourseService(row)

  ElMessage.success('操作成功')
  getWaitOpenList()
}
//3.撤销课程
const deleteCourse = async (row) => {
  ElMessageBox.confirm('确认撤销这门课程的申请吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      await deleteCourseService(row)
      ElMessage({
        type: 'success',
        message: '操作成功'
      })
      //3.再次发送请求获取数据
      getWaitOpenList()
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
  <PageContainer title="处理开课申请页面">
    <template #extra>
      <div class="header"></div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="openList" style="width: 100%">
      <el-table-column
        prop="courseNum"
        label="课程号"
        width="100"
      ></el-table-column>
      <el-table-column prop="courseName" label="课程名"></el-table-column>
      <el-table-column prop="startTime" label="开课时间" width="100">
        <template #default="{ row }">
          {{ formatTime(row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结课时间" width="100">
        <template #default="{ row }">
          {{ formatTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="score" label="学分"></el-table-column>
      <el-table-column prop="maxPeople" label="最大人数"></el-table-column>
      <el-table-column prop="place" label="地点"></el-table-column>
      <el-table-column prop="selWeek" label="上课星期"></el-table-column>
      <el-table-column prop="selStart" label="第一节课">
        <template #default="{ row }"> 第{{ row.selStart }}节 </template>
      </el-table-column>
      <el-table-column prop="selEnd" label="最后一节">
        <template #default="{ row }"> 第{{ row.selEnd }}节 </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <div style="display: flex">
            <el-button type="primary" @click="permitCourse(row)"
              >批准</el-button
            >
            <el-button type="danger" @click="deleteCourse(row)">撤销</el-button>
          </div>
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>

    <!-- 修改/新增表单 -->
    <CourseEdit ref="editRef" @success="OnSubmit"></CourseEdit>
  </PageContainer>
</template>

<style scoped></style>
