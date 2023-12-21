import request from '@/utils/request'

//教师个人信息页面
export const getInfoService = (userId) => {
  return request.get('/teacher/info', {
    params: {
      userId
    }
  })
}

export const teacherChangePasswordService = (userId, password) => {
  request.put('/teacher/changePassword', {
    userId: userId,
    password: password
  })
}

//教师管理课程页面
export const getOpenListService = (userId, courseName) => {
  return request.get('/teacher/getOpenList', {
    params: {
      userId,
      courseName
    }
  })
}

export const openCourseService = (userId, FormData) => {
  return request.post('/teacher/openCourse', {
    techNum: userId,
    ...FormData
  })
}

export const editCourseService = (FormData) => {
  return request.put('/teacher/editCourse', {
    ...FormData
  })
}

export const delCourseService = (techNum, courseNum, adminNum) => {
  return request.delete('/teacher/deleteCourse', {
    params: {
      techNum,
      courseNum,
      adminNum
    }
  })
}

//打分页面
export const getMarkListService = (techNum, courseName) => {
  return request.get('/teacher/getMarkList', {
    params: {
      techNum,
      courseName
    }
  })
}

export const markOperateService = (cst) => {
  return request.put('/teacher/markOperate', cst)
}

//请假管理页面
export const getLeaveListService = (userId) => {
  return request.get('/teacher/getLeaveList', {
    params: {
      techNum: userId
    }
  })
}

export const allowApplyLeaveService = (cst) => {
  return request.post('/teacher/allowApplyLeave', cst)
}

export const rejectApplyLeaveService = (cst) => {
  return request.delete('/teacher/rejectApplyLeave', {
    params: {
      courseNum: cst.courseNum,
      techNum: cst.techNum,
      stuNum: cst.stuNum
    }
  })
}
