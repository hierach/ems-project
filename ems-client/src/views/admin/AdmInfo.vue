<script setup>
import { getInfoService, adminChangePasswordService } from '@/api/admin.js'
import PageContainer from '@/components/PageContainer.vue'
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import {
  Iphone,
  Location,
  User,
  HomeFilled,
  UserFilled,
  Position,
  WarningFilled
} from '@element-plus/icons-vue'

const adminInfo = ref({
  adminNum: '',
  name: '',
  password: '', //修改密码的时候用到
  repassword: '' //修改密码的时候用到
})

//获得教师信息，赋值
const getInfo = async () => {
  const userStore = useUserStore()
  const resp = await getInfoService(userStore.userId)
  adminInfo.value = resp.data
}
getInfo()

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
        if (value !== adminInfo.value.password) {
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

    adminChangePasswordService(userStore.userId, adminInfo.value.password)
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
</script>

<template>
  <PageContainer title="管理员个人信息">
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
        {{ adminInfo.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <iphone />
            </el-icon>
            工号
          </div>
        </template>
        {{ adminInfo.adminNum }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 对话框 -->
    <el-dialog v-model="dialogVisible" title="修改密码" width="30%">
      <el-form
        ref="formRef"
        :model="adminInfo"
        :rules="rules"
        style="margin-top: 10px"
      >
        <el-form-item label="修改密码" prop="password">
          <el-input
            v-model="adminInfo.password"
            placeholder="请输入密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="repassword">
          <el-input
            v-model="adminInfo.repassword"
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
</style>
