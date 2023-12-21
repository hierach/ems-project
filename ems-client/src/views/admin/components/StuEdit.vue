<script setup>
import { ref } from 'vue'
import {
  addStudentService,
  editStudentService,
  getStudentListService
} from '@/api/admin.js'
const dialogFormVisible = ref(false)
// 表单数据
const formData = ref({
  id: '',
  stuNum: '',
  password: '',
  name: '',
  sex: '',
  year: '',
  major: '',
  needScore: ''
})

//记录新增/修改状态，0为新增
const status = ref(0)

//学生学号集合，用于判断是否有重复学号
const stuNumList = ref([])
const getStuNumList = async () => {
  const resp = await getStudentListService()
  const studentList = resp.data
  // console.log(studentList)
  stuNumList.value = studentList.map((student) => student.stuNum)
}
getStuNumList()

// 定义打开函数
const open = (row) => {
  //如果row有值表示修改，否则表示新增
  if (row) {
    formData.value = { ...row }
    status.value = 1
  } else {
    //新增的时候清空表单数据
    formData.value = {}
    status.value = 0
  }
  dialogFormVisible.value = true
}

defineExpose({ open })

// 表单校验规则
const form = ref(null)

const rules = {
  stuNum: [
    {
      required: true,
      message: '学生学号不能为空',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (
          stuNumList.value.includes(formData.value.stuNum) &&
          status.value == 0
        ) {
          callback(new Error('学生学号不应该重复'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  password: {
    required: true,
    message: '密码不能为空',
    trigger: 'blur'
  },
  name: {
    required: true,
    message: '名字不能为空',
    trigger: 'blur'
  },
  sex: {
    required: true,
    message: '性别不能为空',
    trigger: 'blur'
  },
  year: {
    required: true,
    message: '年级不能为空',
    trigger: 'blur'
  },
  major: {
    required: true,
    message: '专业不能为空',
    trigger: 'blur'
  },
  needScore: {
    required: true,
    message: '最大学分不能为空',
    trigger: 'blur'
  }
}

//定义成功通知事件
const emit = defineEmits(['success'])

//处理逻辑

const yearArray = ref([
  { id: 1, name: '大一' },
  { id: 2, name: '大二' },
  { id: 3, name: '大三' },
  { id: 4, name: '大四' }
])

// 2.添加 / 修改数据
const submitData = async () => {
  //1.先进行校验
  await form.value.validate()

  //2.根据stuNum来判断是新增还是修改
  try {
    if (status.value == 1) {
      //修改
      await editStudentService(formData.value)
      ElMessage.success('修改学生信息成功')
    } else {
      //新增
      await addStudentService(formData.value)
      ElMessage.success('新增学生信息成功')
    }
  } catch (err) {
    return new Error(err)
  } finally {
    //通知父组件，刷新数据
    emit('success')
    dialogFormVisible.value = false
  }
}
</script>

<template>
  <!-- 消息对话框  新增/修改功能 -->
  <el-dialog
    v-model="dialogFormVisible"
    :title="status == 1 ? '修改学生信息' : '新增学生信息'"
  >
    <el-form
      ref="form"
      :rules="rules"
      :model="formData"
      :inline="true"
      class="form-inline"
    >
      <!-- 选择审核人 -->
      <el-form-item label="学号" prop="stuNum" v-if="status == 0">
        <el-input v-model="formData.stuNum" placeholder="请输入学号"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input
          v-model="formData.password"
          placeholder="请输入密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="formData.name" placeholder="请输入姓名"></el-input
      ></el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="formData.sex" class="ml-4">
          <el-radio label="男" size="large">男</el-radio>
          <el-radio label="女" size="large">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="年级" prop="year" style="width: 100%">
        <el-select v-model="formData.year" placeholder="请输入年级">
          <el-option
            v-for="yearObj in yearArray"
            :key="yearObj.id"
            :label="yearObj.name"
            :value="yearObj.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input v-model="formData.major" placeholder="请输入专业"></el-input>
      </el-form-item>
      <el-form-item label="毕业所需分数" prop="needScore">
        <el-input
          v-model="formData.needScore"
          placeholder="请输入分数"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitData"> 提交 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.form-inline .el-input {
  --el-input-width: 220px;
}
</style>
