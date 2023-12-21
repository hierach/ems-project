import axios from 'axios'
import { useUserStore } from '@/stores/user'
import router from '@/router/index.js'

//配置地址
const baseURL = 'http://localhost:8080/'
//允许跨域
axios.defaults.withCredentials = true

const instance = axios.create({
  baseURL,
  timeout: 5000
})

// 在发送请求之前修改token
instance.interceptors.request.use(
  function (config) {
    // 在发送请求之前修改token
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = userStore.token
    }

    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    // console.log('response.data.code:' + response.data.code)

    //200是正常状态
    if (response.data.code === 200) {
      return response.data
    }

    //否则的话给前台提示，并且给前端控制台放回Promise异常
    ElMessage({
      message: response.data.message || '服务异常',
      type: 'error'
    })

    return Promise.reject(response.data)
  },
  function (err) {
    //错误的默认情况，只给提示
    ElMessage({
      message: err.response.data.message || '服务异常',
      type: 'error'
    })

    //错误的特殊情况，拦截到登录
    if (err.response?.status === 401) {
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
