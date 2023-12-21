<script setup>
import { ref } from 'vue'
import { User, Lock, Eleme } from '@element-plus/icons-vue'
import { loginService } from '@/api/login.js'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

// 加载状态
const loading = ref(false)

const fromData = ref({
  userNum: '',
  password: '',
  type: 2
})

const rules = {
  userNum: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 12, message: '账号必须是3-12位的字符', trigger: 'blur' }
  ],
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
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (value !== fromData.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback() //校验成功也得callback
        }
      },
      trigger: 'blur'
    }
  ]
}

const form = ref(null)
//登录功能
const userStore = useUserStore()
const router = useRouter()
const login = async () => {
  await form.value.validate()
  try {
    loading.value = true

    const resp = await loginService(fromData.value)

    // console.log(data)
    loading.value = false
    ElMessage.success('登录成功')
    userStore.setToken(resp.data.token)
    userStore.setUserId(resp.data.userId)
    userStore.setUserName(resp.data.userName)

    //根据类型进行跳转
    if (fromData.value.type === 2) {
      router.push('/student')
    } else if (fromData.value.type === 1) {
      router.push('/admin')
    } else if (fromData.value.type === 3) {
      router.push('/teacher')
    }
  } catch (err) {
    loading.value = false
    throw new Error(err)
  }
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12">
      <el-carousel :interval="3500" height="100vh" trigger="click">
        <el-carousel-item>
          <img src="@/assets/bg.png" alt="" class="img1" />
        </el-carousel-item>
        <el-carousel-item>
          <img src="@/assets/kx.jpg" alt="" class="img2" />
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="6" :offset="3" class="form">
      <el-form
        ref="form"
        :model="fromData"
        :rules="rules"
        size="large"
        autocomplete="off"
        class="realForm"
      >
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="userNum">
          <el-input
            :prefix-icon="User"
            v-model="fromData.userNum"
            placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            v-model="fromData.password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="mb-2 flex items-center text-sm">
            <el-radio-group v-model="fromData.type" class="ml-4">
              <el-radio :label="1" size="large">管理员</el-radio>
              <el-radio :label="2" size="large">学生</el-radio>
              <el-radio :label="3" size="large">教师</el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            :loading-icon="Eleme"
            :loading="loading"
            class="button"
            type="primary"
            auto-insert-space
            @click="login"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.login-page {
  .img1 {
    width: 100%;
    height: 100%;
    object-fit: fill;
  }
  .img2 {
    width: 100%;
    height: 100%;
    object-fit: cover; /* 或其他适当的值 */
  }

  // elementui布局横向全部填充满，纵向需要用户设置
  height: 100vh;
  background-color: #fff;
  // .bg {
  //   background:
  //     url('https://jwxt2.ahu.edu.cn/logo/logo_school.png') no-repeat 60% center /
  //       240px auto,
  //     url('https://jwxt2.ahu.edu.cn/logo/loginbgbg.png') no-repeat left / cover;
  //   border-radius: 0 20px 20px 0;
  // }
  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;
    .title {
      margin: 0 auto;
    }
    .button {
      width: 100%;
    }
    //表单项下面只能有一个元素，设置布局在这个元素身上设置。比如最后“注册”，如果想再设置需要用div
    .flex {
      //这个100%也有助于组件分开位置
      width: 100%;
      display: flex;
      justify-content: space-around;
    }
  }
}
</style>
