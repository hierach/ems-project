import request from '@/utils/request'

//登录请求
export const loginService = (user) => {
  return request.post('/login', user)
}

//登出请求
export const logoutService = (token) => {
  request.delete('/logout', {
    params: {
      token
    }
  })
}
