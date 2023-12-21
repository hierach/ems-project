import request from '@/utils/request'

export const getAdminListService = () => {
  return request.get('/admin/getAdminList')
}

//管理员个人信息
export const getInfoService = (userId) => {
  return request.get('/admin/info', {
    params: {
      userId
    }
  })
}

export const adminChangePasswordService = (userId, password) => {
  request.put('/admin/changePassword', {
    userId: userId,
    password: password
  })
}

//学生管理
export const getStudentListService = (stuName) => {
  return request.get('/admin/getStudentList', {
    params: {
      stuName
    }
  })
}

export const addStudentService = (student) => {
  return request.post('/admin/addStudent', student)
}

export const editStudentService = (student) => {
  return request.put('/admin/editStudent', student)
}

export const deleteStudentService = (row) => {
  return request.delete('/admin/deleteStudent', {
    params: {
      stuNum: row.stuNum
    }
  })
}

//教师管理
export const getTeacherListService = (techName) => {
  return request.get('/admin/getTeacherList', {
    params: {
      name: techName
    }
  })
}

export const addTeacherService = (teacher) => {
  return request.post('/admin/addTeacher', teacher)
}

export const editTeacherService = (teacher) => {
  return request.put('/admin/editTeacher', teacher)
}

export const deleteTeacherService = (row) => {
  return request.delete('/admin/deleteTeacher', {
    params: {
      techNum: row.techNum
    }
  })
}

//处理开课请求

//获取open表列表
export const getWaitOpenListService = (userId) => {
  return request.get('/admin/getWaitOpenList', {
    params: {
      userId
    }
  })
}

//批准课程
export const permitCourseService = (row) => {
  return request.post('/admin/permitCourse', row)
}

//撤销课程
export const deleteCourseService = (row) => {
  return request.delete('/admin/deleteCourse', {
    params: {
      adminNum: row.adminNum,
      courseNum: row.courseNum,
      techNum: row.techNum
    }
  })
}
