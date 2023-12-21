<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { formatTime } from '@/utils/format'
import { getMarkListService, markOperateService } from '@/api/teacher'

const courseName = ref('')
const markList = ref([])
const isLoading = ref(false)
const userStore = useUserStore()
const dialogFormVisible = ref(false)

//得到打分列表
const getMarkList = async () => {
  isLoading.value = true
  const resp = await getMarkListService(userStore.userId)
  markList.value = resp.data
  isLoading.value = false
}
getMarkList()

//根据课程名得到打分列表
const selectByCourseName = async (courseName) => {
  isLoading.value = true
  const resp = await getMarkListService(userStore.userId, courseName)
  markList.value = resp.data
  isLoading.value = false
}

//弹出来打分框操作
const formData = ref({
  mark: '',
  fullMark: '',
  courseNum: '',
  stuNum: '',
  techNum: ''
})

const rules = {
  mark: [
    {
      required: true,
      message: '分数不能为空',
      trigger: 'blur'
    },
    {
      pattern: /^[1-9]\d*$/,
      message: '分数不能为负数或小数',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (formData.value.mark > formData.value.fullMark) {
          callback(new Error('设置分数不能超过满分分数'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  fullMark: {
    required: true,
    message: '满分不能为空',
    trigger: 'blur'
  }
}

const form = ref(null)
//打开打分对话框
const openDialog = (row) => {
  dialogFormVisible.value = true
  formData.value = { ...row }
  formData.value.techNum = userStore.userId
}

//进行打分
const submitData = async () => {
  //1.表单校验
  await form.value.validate()

  await markOperateService(formData.value)
  ElMessage.success('打分成功')
  getMarkList()
  dialogFormVisible.value = false
}
</script>

<template>
  <PageContainer title="打分页面">
    <template #extra>
      <div class="header">
        <el-input placeholder="请输入课程名" v-model="courseName"></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="selectByCourseName(courseName)"
          >查询</el-button
        >
      </div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="markList" style="width: 100%">
      <el-table-column prop="courseNum" label="课程号"></el-table-column>
      <el-table-column prop="courseName" label="课程名"></el-table-column>
      <el-table-column prop="stuNum" label="学生学号"></el-table-column>
      <el-table-column prop="stuName" label="学生姓名"></el-table-column>
      <el-table-column prop="startTime" label="开课时间">
        <template #default="{ row }">
          {{ formatTime(row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结课时间">
        <template #default="{ row }">
          {{ formatTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="fullMark" label="课程满分"></el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button type="primary" @click="openDialog(row)">打分</el-button>
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>

    <!-- 编辑打分内容 -->
    <!-- 消息对话框  打分内容 -->
    <el-dialog v-model="dialogFormVisible" title="打分页面">
      <el-form :rules="rules" :model="formData" ref="form">
        <el-form-item label="满分" prop="fullMark">
          <el-text type="primary">{{ formData.fullMark }}分</el-text>
        </el-form-item>
        <!-- 设置分数 -->
        <el-form-item label="设置分数" prop="mark">
          <el-input placeholder="请输入分数" v-model="formData.mark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitData"> 提交 </el-button>
        </span>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<style scoped>
.header {
  display: flex;
}
</style>
