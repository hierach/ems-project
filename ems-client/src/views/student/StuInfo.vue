<script setup>
import {
  studentGetInfo,
  studentChangePassword,
  getHasScoresService,
  getTimeTableListService
} from '@/api/student.js'
import PageContainer from '@/components/PageContainer.vue'
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import {
  Iphone,
  Location,
  OfficeBuilding,
  User,
  HomeFilled,
  UserFilled,
  Position,
  WarningFilled
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import TimeTable from '@/views/student/components/TimeTable.vue'

const studentInfo = ref({
  stuNum: '',
  name: '',
  password: '', //修改密码的时候用到
  repassword: '', //修改密码的时候用到
  sex: '',
  year: '',
  major: '',
  needScore: ''
})

//获得学生信息，赋值
const getInfo = async () => {
  const userStore = useUserStore()
  const resp = await studentGetInfo(userStore.userId)
  studentInfo.value = resp.data
}
getInfo()

//处理学生信息中的年级
const transYear = (num) => {
  const arr = ['大一', '大二', '大三', '大四']
  return arr[num - 1]
}

//修改密码
const dialogVisible = ref(false)
const formRef = ref(null)
const rules = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{3,15}$/,
      message: '密码必须是3-15位的非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{3,15}$/,
      message: '密码必须是3-15位的非空字符',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (value !== studentInfo.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback() //校验成功也得callback
        }
      },
      trigger: 'blur'
    }
  ]
}
const changePassword = async () => {
  //2.表单校验后，向后端发送请求
  try {
    await formRef.value.validate()
    const userStore = useUserStore()

    studentChangePassword(userStore.userId, studentInfo.value.password)
    ElMessage.success('修改密码成功')
    //3.关闭对话框
    dialogVisible.value = false
    //4.重新发送请求
    getInfo()
  } catch (err) {
    return new Error(err)
  }
}

const OnSubmit = () => {
  changePassword()
}

//展示饼状图
/// 声明定义一下echart
const echart = echarts

//获取已获得结果
const userStore = useUserStore()
const sum = ref(0)
const getHasScores = async () => {
  const resp = await getHasScoresService(userStore.userId)
  await getInfo()
  sum.value = resp.data
  const hasGetScore = sum.value
  const notHasGetScore = studentInfo.value.needScore - sum.value
  initChart(hasGetScore, notHasGetScore)
}

onMounted(() => {
  getHasScores()
})

// 基础配置一下Echarts
function initChart(hasGetScore, notHasGetScore) {
  let chart = echart.init(document.getElementById('myEcharts'))
  // 把配置和数据放这里
  chart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      left: 'center'
    },
    series: [
      {
        name: '学分',
        type: 'pie',
        radius: '50%',
        data: [
          {
            value: hasGetScore,
            name: '已获得学分',
            itemStyle: { color: '#91cd77' }
          },
          {
            value: notHasGetScore,
            name: '未获得学分',
            itemStyle: { color: '#5470c6' }
          }
        ]
        // emphasis: {
        //   itemStyle: {
        //     shadowBlur: 10,
        //     shadowOffsetX: 0,
        //     shadowColor: 'rgba(0, 0, 0, 0.5)'
        //   }
        // }
      }
    ]
  })
  // window.onresize = function () {
  //   window.removeEventListener('resize', this.initEchart, 20)
  // }
}

//课程表所需要的数据
const events = ref([])
const getTimeTableList = async () => {
  const resp = await getTimeTableListService(userStore.userId)
  events.value = resp.data
}
getTimeTableList()
</script>

<template>
  <PageContainer title="学生个人信息">
    <!-- 修改个人信息按钮 -->
    <template #extra>
      <el-button type="primary" @click="dialogVisible = true"
        >修改个人密码</el-button
      >
    </template>

    <!-- 项目声明 -->
    <el-card style="width: 30%; margin-bottom: 30px; height: 150px">
      <div style="display: flex; flex-direction: column">
        <div class="stateItem">
          <el-icon><HomeFilled /></el-icon>
          <div>项目名: 仿安大教务管理系统</div>
        </div>
        <div style="margin-top: 5px" class="stateItem">
          <el-icon><UserFilled /></el-icon>
          <div>开发人员: weifengqin</div>
        </div>
        <div style="margin-top: 5px" class="stateItem">
          <el-icon><Location /></el-icon>
          <div>安徽大学</div>
        </div>
        <div style="margin-top: 5px" class="stateItem">
          <el-icon><Position /></el-icon>
          <div>Github: hierach</div>
        </div>
        <div style="margin-top: 5px" class="stateItem">
          <el-icon><WarningFilled /></el-icon>
          <div>仅供学习，禁止抄袭!</div>
        </div>
      </div>
    </el-card>

    <!-- 展示个人信息 -->
    <el-descriptions
      class="margin-top"
      :column="3"
      :size="size"
      border
      title="基本信息"
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <user />
            </el-icon>
            姓名
          </div>
        </template>
        {{ studentInfo.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <iphone />
            </el-icon>
            学号
          </div>
        </template>
        {{ studentInfo.stuNum }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <location />
            </el-icon>
            性别
          </div>
        </template>
        {{ studentInfo.sex }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <office-building />
            </el-icon>
            年级
          </div>
        </template>
        {{ transYear(studentInfo.year) }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <office-building />
            </el-icon>
            专业
          </div>
        </template>
        {{ studentInfo.major }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <office-building />
            </el-icon>
            毕业所需学分
          </div>
        </template>
        {{ studentInfo.needScore }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 对话框 -->
    <el-dialog v-model="dialogVisible" title="修改密码" width="30%">
      <el-form
        ref="formRef"
        :model="studentInfo"
        :rules="rules"
        style="margin-top: 10px"
      >
        <el-form-item label="修改密码" prop="password">
          <el-input
            v-model="studentInfo.password"
            placeholder="请输入密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="repassword">
          <el-input
            v-model="studentInfo.repassword"
            placeholder="请输入确认密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="OnSubmit"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 图表 -->
    <div style="margin-top: 20px; font-weight: bold">已获得学分信息</div>
    <div class="echarts-box" style="margin-top: 20px">
      <div id="myEcharts" :style="{ width: '900px', height: '300px' }"></div>
    </div>

    <!-- 课程表 -->
    <div class="timetable">
      <div style="margin-top: 20px; font-weight: bold">学生已选课程表</div>
      <TimeTable
        style="width: 80%; margin-top: 30px"
        :afternoon-length="5"
        :length="13"
        :events="events"
      ></TimeTable>
    </div>
  </PageContainer>
</template>

<style scoped lang="scss">
.stateItem {
  display: flex;
  align-items: center;
  div {
    margin-left: 10px;
  }
}
.info {
  height: 450px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  .el-row {
    margin-bottom: 20px;
  }
}

.timetable {
  display: flex;
  flex-direction: column;
  // align-items: center;
}
</style>
