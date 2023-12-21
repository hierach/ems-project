import request from '@/utils/request'

// 学生信息页面
export const studentGetInfo = (userId) => {
  return request.get('/student/info', {
    params: {
      userId
    }
  })
}

export const studentChangePassword = (userId, password) => {
  request.put('/student/changePassword', {
    userId: userId,
    password: password
  })
}

//学生选课页面
export const getCourseListService = (userId, courseName) => {
  return request.get('/student/getCourseList', {
    params: {
      userId,
      courseName
    }
  })
}

export const selectCourseService = (userId, courseNum, techNum) => {
  return request.post('/student/selectCourse', {
    userId,
    courseNum,
    techNum
  })
}

//已选课程页面
export const getHasSelectCourseListService = (userId, courseName) => {
  return request.get('/student/getHasSelectCourseList', {
    params: {
      userId,
      courseName
    }
  })
}

export const delCourseService = (userId, courseNum, techNum) => {
  return request.delete('/student/delCourse', {
    params: {
      userId,
      courseNum,
      techNum
    }
  })
}

//请假申请功能
export const getLeaveListService = (userId) => {
  return request.get('/student/getLeaveList', {
    params: {
      stuNum: userId
    }
  })
}

export const applyLeaveService = (cst) => {
  return request.post('/student/applyLeave', cst)
}

export const removeLeaveService = (cst) => {
  return request.delete('/student/removeLeave', {
    params: {
      courseNum: cst.courseNum,
      techNum: cst.techNum,
      stuNum: cst.stuNum
    }
  })
}

//展示学分数据
export const getHasScoresService = (stuNum) => {
  return request.get('/student/getHasScores', {
    params: {
      stuNum
    }
  })
}

//获取课程表
export const getTimeTableListService = (stuNum) => {
  return request.get('/student/getTimeTableList', {
    params: {
      stuNum
    }
  })
}
