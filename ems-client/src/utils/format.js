import { dayjs } from 'element-plus'

export const formatTime = (time) => dayjs(time).format('YYYY.MM.DD')

export const formatLeaveTime = (time) => {
  if (time) {
    return dayjs(time).format('YYYY.MM.DD HH:mm')
  } else {
    return '暂无请假时间'
  }
}
