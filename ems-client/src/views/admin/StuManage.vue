<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { Edit, Delete } from '@element-plus/icons-vue'
import { getStudentListService, deleteStudentService } from '@/api/admin.js'
import StuEdit from './components/StuEdit.vue'

const stuName = ref('')
const studentList = ref([])
const isLoading = ref(false)

// 查询开课表 单表查询
const getStuList = async () => {
  isLoading.value = true
  const resp = await getStudentListService()
  studentList.value = resp.data
  isLoading.value = false
}
getStuList()

// 按课程名查询开的课
const selectByStuName = async (stuName) => {
  const resp = await getStudentListService(stuName)
  studentList.value = resp.data
}
//修改组件提交之后，进行刷新
const OnSubmit = () => {
  getStuList()
}

//添加课程
const editRef = ref(null)
const addStudent = () => {
  editRef.value.open()
}
//修改课程
const editStudent = (row) => {
  editRef.value.open(row)
}
//删除课程
const removeStudent = (row) => {
  ElMessageBox.confirm('确认删除这个学生信息吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      //1.发送删除学生信息
      await deleteStudentService(row)
      //2.如果能拿到数据，返回选课成功
      ElMessage({
        type: 'success',
        message: '删除成功'
      })
      //3.再次发送请求获取数据
      getStuList()
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
  <PageContainer title="学生管理页面">
    <template #extra>
      <div class="header">
        <el-input placeholder="请输入课程名" v-model="stuName"></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="selectByStuName(stuName)"
          >查询</el-button
        >
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="addStudent()"
          >新增学生</el-button
        >
      </div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="studentList" style="width: 100%">
      <el-table-column
        prop="stuNum"
        label="学生学号"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="password"
        label="学生密码"
        width="100"
      ></el-table-column>
      <el-table-column prop="name" label="学生姓名"></el-table-column>
      <el-table-column prop="sex" label="性别"> </el-table-column>
      <el-table-column prop="year" label="年级" width="100"> </el-table-column>
      <el-table-column prop="major" label="专业"></el-table-column>
      <el-table-column prop="needScore" label="最大分数"></el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button
            type="primary"
            :icon="Edit"
            circle
            plain
            @click="editStudent(row)"
          ></el-button>
          <el-button
            type="danger"
            :icon="Delete"
            circle
            plain
            @click="removeStudent(row)"
          ></el-button>
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>

    <!-- 修改/新增表单 -->
    <StuEdit ref="editRef" @success="OnSubmit"></StuEdit>
  </PageContainer>
</template>

<style scoped lang="scss">
.header {
  display: flex;
}
</style>
