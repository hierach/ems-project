<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { formatTime } from '@/utils/format'
import { Edit, Delete } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getOpenListService, delCourseService } from '@/api/teacher'
import CourseEdit from '@/views/teacher/components/CourseEdit.vue'

const courseName = ref('')
const openList = ref([])
const isLoading = ref(false)
const userStore = useUserStore()

// 查询开课表 单表查询
const getOpenList = async () => {
  isLoading.value = true
  const resp = await getOpenListService(userStore.userId)
  openList.value = resp.data
  isLoading.value = false
}
getOpenList()

// 按课程名查询开的课
const selectByCourseName = async (courseName) => {
  const resp = await getOpenListService(userStore.userId, courseName)
  openList.value = resp.data
}
//修改组件提交之后，进行刷新
const OnSubmit = () => {
  getOpenList()
}

//添加课程
const editRef = ref(null)
const addCourse = () => {
  editRef.value.open()
}
//修改课程
const editCourse = (row) => {
  editRef.value.open(row)
}
//删除课程
const userstore = useUserStore()
const removeCourse = async (row) => {
  ElMessageBox.confirm('确认删除这门课吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      //1.发送删除课程请求  参数带上学生学号 + 课程号
      await delCourseService(userstore.userId, row.courseNum, row.adminNum)
      //2.如果能拿到数据，返回选课成功
      ElMessage({
        type: 'success',
        message: '删除成功'
      })
      //3.再次发送请求获取数据
      getOpenList()
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
  <PageContainer title="教师课程管理页面">
    <template #extra>
      <div class="header">
        <el-input placeholder="请输入课程名" v-model="courseName"></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="selectByCourseName(courseName)"
          >查询</el-button
        >
        <el-button type="primary" style="margin-left: 20px" @click="addCourse()"
          >新增课程</el-button
        >
      </div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="openList" style="width: 100%">
      <el-table-column
        prop="adminNum"
        label="审批人工号"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="courseNum"
        label="课程号"
        width="100"
      ></el-table-column>
      <el-table-column prop="courseName" label="课程名"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-text type="success" v-if="row.status == 1">审批通过</el-text>
          <el-text type="primary" v-if="row.status == 0">审批中</el-text>
          <el-text type="danger" v-if="row.status == -1">审批未通过</el-text>
        </template>
      </el-table-column>
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
          <el-button
            type="primary"
            :icon="Edit"
            circle
            plain
            v-if="row.status != -1"
            @click="editCourse(row)"
          ></el-button>
          <el-button
            type="danger"
            :icon="Delete"
            circle
            plain
            @click="removeCourse(row)"
          ></el-button>
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

<style scoped lang="scss">
.header {
  display: flex;
}
</style>
