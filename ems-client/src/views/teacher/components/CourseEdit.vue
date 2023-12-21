<script setup>
import { ref } from 'vue'
import { getAdminListService } from '@/api/admin.js'
import { useUserStore } from '@/stores/user'
import { openCourseService, editCourseService } from '@/api/teacher.js'
const dialogFormVisible = ref(false)
// 表单数据
const formData = ref({
  adminNum: '', //需要查询出来
  courseNum: '', //用来区别是否是新增还是修改功能
  courseName: '', //课程名
  score: '', //学分
  fullMark: '', //满分多少分
  maxPeople: '', //最大选课人数
  startTime: '',
  endTime: '',
  place: '',
  selWeek: '',
  selStart: '',
  selEnd: ''
})

// 定义打开函数
const open = (row) => {
  //如果row有值表示修改，否则表示新增
  if (row) {
    formData.value = { ...row }
  } else {
    //新增的时候获得审核人编号
    formData.value = {}
    getAdminList()
  }
  dialogFormVisible.value = true
}

defineExpose({ open })

// 定义星期数组
const weekArray = ref([
  { id: 1, name: '星期一' },
  { id: 2, name: '星期二' },
  { id: 3, name: '星期三' },
  { id: 4, name: '星期四' },
  { id: 5, name: '星期五' },
  { id: 6, name: '星期六' },
  { id: 7, name: '星期日' }
])

// 定义选课数组
const courseSectionsStart = ref(
  Array.from({ length: 13 }, (_, index) => ({
    id: index + 1,
    name: `第${index + 1}节课`
  }))
)
// 定义课数数组
const courseSectionsEnd = ref(
  Array.from({ length: 13 }, (_, index) => ({
    id: index + 1,
    name: `第${index + 1}节课`
  }))
)

// 表单校验规则
const form = ref(null)

const rules = {
  adminNum: {
    required: true,
    message: '审核人不能为空',
    trigger: 'blur'
  },
  courseName: {
    required: true,
    message: '课程名不能为空',
    trigger: 'blur'
  },
  score: {
    required: true,
    message: '学分不能为空',
    trigger: 'blur'
  },
  fullMark: {
    required: true,
    message: '满分不能为空',
    trigger: 'blur'
  },
  maxPeople: {
    required: true,
    message: '最大选课人数不能为空',
    trigger: 'blur'
  },
  startTime: {
    required: true,
    message: '开课时间不能为空',
    trigger: 'blur'
  },
  endTime: [
    { required: true, message: '结课时间不能为空', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          new Date(formData.value.startTime) >= new Date(formData.value.endTime)
        ) {
          callback(new Error('开课时间应小于结课时间'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  place: {
    required: true,
    message: '上课地点不能为空',
    trigger: 'blur'
  },
  selWeek: {
    required: true,
    message: '上课星期不能为空',
    trigger: 'blur'
  },
  selStart: {
    required: true,
    message: '第一节课不能为空',
    trigger: 'blur'
  },
  selEnd: [
    { required: true, message: '最后一节课不能为空', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (formData.value.selStart > formData.value.selEnd) {
          callback(new Error('第一节课应小于等于最后一节课'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

//定义成功通知事件
const emit = defineEmits(['success'])

//处理逻辑
// 1.获得审核人号列表
const adminList = ref([])
const getAdminList = async () => {
  const resp = await getAdminListService()
  adminList.value = resp.data
}

// 2.添加 / 修改数据
const userStore = useUserStore()
const submitData = async () => {
  //1.先进行校验
  await form.value.validate()

  //2.根据courseNum来判断是新增还是修改
  try {
    if (formData.value.courseNum) {
      //修改
      await editCourseService(formData.value)
      ElMessage.success('修改课程成功')
    } else {
      //新增
      await openCourseService(userStore.userId, formData.value)
      ElMessage.success('新增课程成功')
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
    :title="formData.courseNum ? '修改课程' : '新增课程'"
  >
    <el-form
      ref="form"
      :rules="rules"
      :model="formData"
      :inline="true"
      class="form-inline"
    >
      <!-- 选择审核人 -->
      <el-form-item
        v-if="!formData.courseNum"
        label="选择审核人"
        prop="adminNum"
      >
        <el-select v-model="formData.adminNum" placeholder="请选择审核人">
          <el-option
            v-for="admin in adminList"
            :key="admin.id"
            :label="admin.name"
            :value="admin.adminNum"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="课程名" prop="courseName">
        <el-input
          v-model="formData.courseName"
          placeholder="请输入课程名"
        ></el-input>
      </el-form-item>
      <el-form-item label="此课程学分" prop="score">
        <el-input v-model="formData.score" placeholder="请输入学分"></el-input
      ></el-form-item>
      <el-form-item label="满分" prop="fullMark">
        <el-input
          v-model="formData.fullMark"
          placeholder="请输入满分"
        ></el-input>
      </el-form-item>
      <el-form-item label="最大选课人数" prop="maxPeople" style="width: 100%">
        <el-input
          v-model="formData.maxPeople"
          placeholder="请输入最大选课人数"
        ></el-input>
      </el-form-item>
      <el-form-item label="开课时间" prop="startTime">
        <el-date-picker
          v-model="formData.startTime"
          value-format="YYYY-MM-DDTHH:mm:ss"
          type="datetime"
          placeholder="开课时间"
          style="width: 100%"
          format="YYYY-MM-DD HH:mm:ss"
          date-format="MMM DD, YYYY"
          time-format="HH:mm"
        />
      </el-form-item>
      <el-form-item label="结课时间" prop="endTime">
        <el-date-picker
          v-model="formData.endTime"
          value-format="YYYY-MM-DDTHH:mm:ss"
          type="datetime"
          placeholder="结课时间"
          style="width: 100%"
          format="YYYY-MM-DD HH:mm:ss"
          date-format="MMM DD, YYYY"
          time-format="HH:mm"
        />
      </el-form-item>
      <el-form-item label="上课地点" prop="place">
        <el-input
          v-model="formData.place"
          placeholder="请输入上课地点"
        ></el-input>
      </el-form-item>
      <el-form-item label="上课星期" prop="selWeek">
        <el-select v-model="formData.selWeek" placeholder="请输入上课星期">
          <el-option
            v-for="week in weekArray"
            :key="week.id"
            :label="week.name"
            :value="week.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="第一节课" prop="selStart">
        <el-select v-model="formData.selStart" placeholder="请输入第一节课">
          <el-option
            v-for="courseSection in courseSectionsStart"
            :key="courseSection.id"
            :label="courseSection.name"
            :value="courseSection.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="最后一节课" prop="selEnd">
        <el-select v-model="formData.selEnd" placeholder="请输入最后一节课">
          <el-option
            v-for="courseSection in courseSectionsEnd"
            :key="courseSection.id"
            :label="courseSection.name"
            :value="courseSection.id"
          ></el-option>
        </el-select>
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
