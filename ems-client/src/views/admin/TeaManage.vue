<script setup>
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { Edit, Delete } from '@element-plus/icons-vue'
import { getTeacherListService, deleteTeacherService } from '@/api/admin.js'
import TechEdit from './components/TechEdit.vue'

const techName = ref('')
const teacherList = ref([])
const isLoading = ref(false)

// 查询教师表 单表查询
const getTechList = async () => {
  isLoading.value = true
  const resp = await getTeacherListService()
  teacherList.value = resp.data
  isLoading.value = false
}
getTechList()

// 按教师名名查询开的课
const selectByTechName = async (techName) => {
  const resp = await getTeacherListService(techName)
  teacherList.value = resp.data
}
//修改组件提交之后，进行刷新
const OnSubmit = () => {
  getTechList()
}

//添加教师信息
const editRef = ref(null)
const addTeacher = () => {
  editRef.value.open()
}
//修改教师信息
const editTeacher = (row) => {
  editRef.value.open(row)
}
//删除教师信息
const removeTeacher = (row) => {
  ElMessageBox.confirm('确认删除这个教师信息吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
    .then(async () => {
      //1.发送删除教师信息
      await deleteTeacherService(row)
      //2.如果能拿到数据，返回成功
      ElMessage({
        type: 'success',
        message: '删除成功'
      })
      //3.再次发送请求获取数据
      getTechList()
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
  <PageContainer title="教师管理页面">
    <template #extra>
      <div class="header">
        <el-input placeholder="请输入教师名" v-model="techName"></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="selectByTechName(techName)"
          >查询</el-button
        >
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="addTeacher()"
          >新增教师</el-button
        >
      </div>
    </template>

    <!-- 查询课程表格 -->
    <el-table v-loading="isLoading" :data="teacherList" style="width: 100%">
      <el-table-column prop="techNum" label="教师号"></el-table-column>
      <el-table-column prop="password" label="教师密码"></el-table-column>
      <el-table-column prop="name" label="教师姓名"></el-table-column>
      <el-table-column prop="sex" label="性别"> </el-table-column>
      <el-table-column prop="faculty" label="学院"> </el-table-column>
      <el-table-column prop="jobTitle" label="职称"></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button
            type="primary"
            :icon="Edit"
            circle
            plain
            @click="editTeacher(row)"
          ></el-button>
          <el-button
            type="danger"
            :icon="Delete"
            circle
            plain
            @click="removeTeacher(row)"
          ></el-button>
        </template>
      </el-table-column>

      <template #empty>
        <el-empty description="没有数据"></el-empty>
      </template>
    </el-table>

    <!-- 修改/新增表单 -->
    <TechEdit ref="editRef" @success="OnSubmit"></TechEdit>
  </PageContainer>
</template>

<style scoped lang="scss">
.header {
  display: flex;
}
</style>
