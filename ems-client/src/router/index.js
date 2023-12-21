import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: () => import('@/views/login/LoginPage.vue') },
    { path: '/', redirect: '/login' },
    {
      path: '/student',
      component: () => import('@/views/student/layout/StuLayout.vue'),
      redirect: '/student/info',
      children: [
        {
          path: 'info',
          component: () => import('@/views/student/StuInfo.vue')
        },
        {
          path: 'selectCourse',
          component: () => import('@/views/student/SelectCourse.vue')
        },
        {
          path: 'querySelectedCourse',
          component: () => import('@/views/student/QuerySelectedCourse.vue')
        },
        {
          path: 'applyLeave',
          component: () => import('@/views/student/ApplyLeave.vue')
        }
      ]
    },
    {
      path: '/teacher',
      component: () => import('@/views/teacher/layout/TeaLayout.vue'),
      redirect: '/teacher/info',
      children: [
        {
          path: 'info',
          component: () => import('@/views/teacher/TeaInfo.vue')
        },
        {
          path: 'courseManage',
          component: () => import('@/views/teacher/CourseManager.vue')
        },
        {
          path: 'courseMark',
          component: () => import('@/views/teacher/CourseMark.vue')
        },
        {
          path: 'processCourse',
          component: () => import('@/views/teacher/ProcessLeave.vue')
        }
      ]
    },
    {
      path: '/admin',
      component: () => import('@/views/admin/layout/AdmLayout.vue'),
      redirect: '/admin/info',
      children: [
        {
          path: 'info',
          component: () => import('@/views/admin/AdmInfo.vue')
        },
        {
          path: 'stuManage',
          component: () => import('@/views/admin/StuManage.vue')
        },
        {
          path: 'teaManage',
          component: () => import('@/views/admin/TeaManage.vue')
        },
        {
          path: 'processCourse',
          component: () => import('@/views/admin/ProcessCourse.vue')
        }
      ]
    }
  ]
})

export default router

//路由前置守卫
router.beforeEach((to) => {
  const userStore = useUserStore()

  //如果没有token，且访问非登录页，就跳转回来
  if (!userStore.token && to.path !== '/login') {
    ElMessage({
      message: '登录状态过期，请先登录',
      type: 'error'
    })
    router.push('/login')
  }
})
